package com.juanantoniotora.appPruebaTecnica.service;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestKafkaDTO;
import com.juanantoniotora.appPruebaTecnica.mapper.restMapper.RegistryKafkaMapperImpl;
import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import com.juanantoniotora.appPruebaTecnica.mapper.entityMapper.RegistryEntityMapperImpl;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import com.juanantoniotora.appPruebaTecnica.repository.SearchRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

@Service
public class PruebaTecnicaServiceImpl implements PruebaTecnicaService {

    public static final String TOPIC = "topic_request";

    public static final String REQUEST_REPLY_TOPIC = "topic_response";

    final Logger logger = LoggerFactory.getLogger(PruebaTecnicaServiceImpl.class);

    @Autowired
    ReplyingKafkaTemplate<String, RegistryRequestKafkaDTO, SearchIdResponse> kafkaTemplate;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    RegistryEntityMapperImpl registryEntityMapperImpl;

    @Autowired
    AsynchronousService asynchronousService;

    @Autowired
    RegistryKafkaMapperImpl registryKafkaMapperImpl;

    @Override
    public void home() {
        logger.info("INICIO [AppServiceImpl.home] - Realizando logica de la home.");

        // Sacamos una llamada asincrona a un metodo privado para simplificar los TEST.
        // Podremos usar .Join() en los TEST para esperar a que termine si es necesario pero sin usarlo en la Implementacion.
        // Asi controlamos el tiempo de espera en los TEST y mantemos una Implementacion asincrona real.
        this.doAsyncTask();

        logger.info("FIN [AppServiceImpl.home] - Realizando logica de la home.");
    }

    @Override
    public SearchIdResponse search(RegistryRequest payload) {
        logger.info("INICIO [AppServiceImpl.search] - Haciendo la logica de search.");
        try {

            // Comunicacion con Kafka para escribir en BBDD y obtener el searchId de vuelta
            ProducerRecord<String, RegistryRequestKafkaDTO> record = new ProducerRecord<>(TOPIC, this.registryKafkaMapperImpl.asRegistryRequestJsonKafka(payload));
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, REQUEST_REPLY_TOPIC.getBytes()));
            RequestReplyFuture<String, RegistryRequestKafkaDTO, SearchIdResponse> sendAndReceive =
                    kafkaTemplate.sendAndReceive(record);
            SendResult<String, RegistryRequestKafkaDTO> sendResult = sendAndReceive.getSendFuture().get();
            sendResult.getProducerRecord().headers().forEach(header -> logger.debug("{}:{}", header.key(), header.value().toString()));

            // obtengo la respuesta del consumer kafka
            ConsumerRecord<String, SearchIdResponse> consumerRecord = sendAndReceive.get();

            logger.info("FIN [AppServiceImpl.search] - Haciendo la logica de search.");
            return consumerRecord.value();
        } catch (DataAccessResourceFailureException e) {
            logger.error("Error relacionado con escritura en BBDD: " + e.getMessage());
            throw new DataAccessResourceFailureException("Error relacionado con escritura en BBDD.");
        } catch (Exception e) {
            logger.error("Error en request-reply Kafka: " + e.getMessage());
            return null;
        }
    }

    @Override
    public RegistryAndCounterResponse count(Long searchId) {
        logger.info("INICIO [AppServiceImpl.count] - Haciendo la logica de count.");

        try {
            Optional<RegistryAndCounterEntity> registryAndCounterEntity = this.searchRepository.findById(searchId);
            if (registryAndCounterEntity.isEmpty()) {
                this.logger.info("FIN [AppServiceImpl.count] - No se han encontrado búsquedas similares para el 'searchId' proporcionado.");
                return null;
            }
            RegistryAndCounterEntity entity = registryAndCounterEntity.get();


            // si encuentra la entidad en BBDD, obtiene el contador de busquedas similares
            Long countMatches = this.searchRepository.countMatchingById(searchId);
            entity.setCount(countMatches); // setteo el contador en la entity
            // mapea la entidad al modelo de respuesta
            RegistryAndCounterResponse registryAndCounterResponse = this.registryEntityMapperImpl.asRegistryAndCounterResponse(
                    searchId,
                    entity,
                    entity != null ? entity.getCount() : 0
            );

            logger.info("FIN [AppServiceImpl.count] - Haciendo la logica de count.");
            return registryAndCounterResponse;


        } catch (DataAccessResourceFailureException e){
            logger.error("ERROR [AppServiceImpl.count] - Error accediendo a BBDD.");
            throw new DataAccessResourceFailureException("Error accediendo a BBDD.");
        }


    }

    private CompletableFuture<Void> doAsyncTask() {
        return CompletableFuture.runAsync(() -> {
            // Simula una tarea asíncrona que toma tiempo
            try {
                Thread.sleep(2000); // Simula una tarea que tarda 2 segundos
                this.asynchronousService.asyncSimulateProcess(); // aquí se llama a lo que simula el proceso silencioso
                logger.info("Tarea asíncrona completada.");
            } catch (Exception e) {
                logger.error("Error en la tarea asíncrona");
            }
        });
    }
}
