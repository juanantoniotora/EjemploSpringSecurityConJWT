// Java
package com.juanantoniotora.appPruebaTecnica.kafka;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestKafkaDTO;
import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import com.juanantoniotora.appPruebaTecnica.repository.SearchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerServiceImpl {

    final Logger logger = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    @Autowired
    SearchRepository searchRepository;

    @KafkaListener(topics = "topic_request", groupId = "grupo1")
    @SendTo
    public SearchIdResponse searchListener(RegistryRequestKafkaDTO dtoConsumibleKafka) {
        logger.info("INICIO [KafkaConsumerServiceImpl.searchListener] - Persistiendo en bbdd via Kafka.");

        // Mapeo simple a entidad y persistencia. Solo escribe en BBDD, no responde.
        RegistryAndCounterEntity entity = new RegistryAndCounterEntity();
        entity.setHotelId(dtoConsumibleKafka.getHotelId());
        entity.setCheckIn(dtoConsumibleKafka.getCheckIn());
        entity.setCheckOut(dtoConsumibleKafka.getCheckOut());
        entity.setAges(dtoConsumibleKafka.getAges());

        SearchIdResponse response;

        try {
            RegistryAndCounterEntity result = searchRepository.save(entity);
            response = new SearchIdResponse();
            response.setSearchId(result.getSearchId());
        } catch (Exception e) {
            logger.error("ERROR [KafkaConsumerServiceImpl.searchListener] - Error al guardar en BBDD: " + e.getMessage());
            throw new DataAccessResourceFailureException("Error al guardar en BBDD");
        }

        logger.info("FIN [KafkaConsumerServiceImpl.searchListener] - Persistiendo en bbdd via Kafka.");
        return response;
    }
}