package com.juanantoniotora.appPruebaTecnica;

import com.juanantoniotora.appPruebaTecnica.dto.*;
import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import com.juanantoniotora.appPruebaTecnica.mapper.entityMapper.RegistryEntityMapperImpl;
import com.juanantoniotora.appPruebaTecnica.mapper.restMapper.RegistryKafkaMapperImpl;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import com.juanantoniotora.appPruebaTecnica.model.RegistryResponse;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import com.juanantoniotora.appPruebaTecnica.repository.SearchRepository;
import com.juanantoniotora.appPruebaTecnica.service.PruebaTecnicaServiceImpl;
import com.juanantoniotora.appPruebaTecnica.service.AsynchronousService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class PruebaTecnicaServiceTest {
    @InjectMocks
    private PruebaTecnicaServiceImpl appServiceImpl;

    @Mock
    private Logger logger;

    @Mock
    ReplyingKafkaTemplate<String, RegistryRequestKafkaDTO, SearchIdResponse> kafkaTemplate;

    @Mock
    SearchRepository searchRepository;

    @Mock
    AsynchronousService asynchronousService;

    @Spy
    RegistryKafkaMapperImpl registryKafkaMapperImpl;

    @Mock
    RegistryRequestKafkaDTO registryRequestKafkaDTO;

    @Mock
    RequestReplyFuture<String, RegistryRequestKafkaDTO, SearchIdResponse> rrFutureMock;

    @Mock
    SendResult<String, RegistryRequestKafkaDTO> sendResultMock;

    @Mock
    CompletableFuture<SendResult<String, RegistryRequestKafkaDTO>> sendFuture;

    @Spy
    RegistryEntityMapperImpl registryEntityMapperImpl;

    RegistryAndCounterResponse registryAndCounterResponse;

    RegistryResponse registryResponse;

    RegistryRequestDTO registryRequestDTO;

    RegistryRequest registryRequest;

    SearchIdResponse searchIdResponse = new SearchIdResponse();

    SearchIdResponseDTO searchIdResponseDTO = new SearchIdResponseDTO();

    Long searchId = 502L;

    RegistryAndCounterEntity registryAndCounterEntity;

    Long count = 4L;

    @BeforeEach
    void SetUp() throws NoSuchFieldException, IllegalAccessException {
        this.logger = Mockito.mock(Logger.class);

        final Field loggerField = PruebaTecnicaServiceImpl.class.getDeclaredField(("logger"));
        loggerField.setAccessible(true);
        loggerField.set(this.appServiceImpl, this.logger);

        this.registryAndCounterEntity = new RegistryAndCounterEntity();
        this.registryAndCounterEntity.setSearchId(502L);
        this.registryAndCounterEntity.setHotelId("1234aBc");
        this.registryAndCounterEntity.setCheckIn("29/12/2023");
        this.registryAndCounterEntity.setCheckOut("31/12/2023");
        this.registryAndCounterEntity.setAges(List.of(30,29,1,3));
        this.registryAndCounterEntity.setCount(4L);

        this.registryResponse = new RegistryResponse();
        this.registryResponse.setHotelId("1234aBc");
        this.registryResponse.setCheckIn("29/12/2023");
        this.registryResponse.setCheckOut("31/12/2023");
        this.registryResponse.setAges(List.of(30,29,1,3));

        this.registryAndCounterResponse = new RegistryAndCounterResponse();
        this.registryAndCounterResponse.setSearchId(502L);
        this.registryAndCounterResponse.setRegistry(this.registryResponse);
        this.registryAndCounterResponse.setCount(4L);

        this.registryRequestDTO = new RegistryRequestDTO("1234aBc","29/12/2023","31/12/2023",List.of(30,29,1,3));

        this.searchIdResponseDTO.setSearchId(this.searchId);

        this.registryRequest = new RegistryRequest("1234aBc","29/12/2023","31/12/2023",List.of(30,29,1,3));

        this.searchIdResponse.setSearchId(this.searchId);
    }

    /**
     * Here start test for the Mappers y EntityMappers
     */
    @Test
    void asRegistryAndCounterResponseFromRegistryEntityMapperImplOkTest() {
        final RegistryAndCounterResponse result =
                this.registryEntityMapperImpl.asRegistryAndCounterResponse(this.searchId, this.registryAndCounterEntity, this.count);

        Assertions.assertEquals(this.registryAndCounterResponse.getSearchId(), result.getSearchId());
        Assertions.assertEquals(this.registryAndCounterResponse.getCount(), result.getCount());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getHotelId(), result.getRegistry().getHotelId());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getCheckIn(), result.getRegistry().getCheckIn());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getCheckOut(), result.getRegistry().getCheckOut());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getAges(), result.getRegistry().getAges());
    }

    @Test
    void asRegistryAndCounterResponseFromRegistryEntityMapperImplNullTest() {
        final RegistryAndCounterResponse result1 =
                this.registryEntityMapperImpl.asRegistryAndCounterResponse(null, this.registryAndCounterEntity, this.count);
        Assertions.assertNull(result1);

        final RegistryAndCounterResponse result2 =
                this.registryEntityMapperImpl.asRegistryAndCounterResponse(this.searchId, null, this.count);
        Assertions.assertNull(result2);

        final RegistryAndCounterResponse result3 =
                this.registryEntityMapperImpl.asRegistryAndCounterResponse(this.searchId, this.registryAndCounterEntity, null);
        Assertions.assertNull(result3);
    }

    @Test
    void asRegistryRequestJsonKafkaOKTest() {
        final RegistryRequestKafkaDTO result =
                this.registryKafkaMapperImpl.asRegistryRequestJsonKafka(this.registryRequest);

        Assertions.assertEquals(this.registryRequest.hotelId(), result.getHotelId());
        Assertions.assertEquals(this.registryRequest.checkIn(), result.getCheckIn());
        Assertions.assertEquals(this.registryRequest.checkOut(), result.getCheckOut());
        Assertions.assertEquals(this.registryRequest.ages(), result.getAges());
    }

    @Test
    void asRegistryRequestJsonKafkaNullTest() {
        final RegistryRequestKafkaDTO result = this.registryKafkaMapperImpl.asRegistryRequestJsonKafka(null);

        Assertions.assertNull(result);
    }

    /**
     * Here start test for the Services
     */

    /* Probamos el método home */
    @Test
    void homeAppServiceOkTest() {
        this.appServiceImpl.home();

        Mockito.verify(logger).info("INICIO [AppServiceImpl.home] - Realizando logica de la home.");
        Mockito.verify(logger).info("FIN [AppServiceImpl.home] - Realizando logica de la home.");
    }

    /* Probamos el método count */
    @Test
    void countServiceOkTest() {
        Optional<RegistryAndCounterEntity> registryAndCounterEntityOptional =
                Optional.of(this.registryAndCounterEntity);

        Mockito.when(this.searchRepository.findById(searchId)).thenReturn(registryAndCounterEntityOptional);
        Mockito.when(this.searchRepository.countMatchingById(this.searchId)).thenReturn(this.searchId);
        Mockito.when(this.registryEntityMapperImpl.asRegistryAndCounterResponse(
                Mockito.eq(this.searchId), Mockito.eq(this.registryAndCounterEntity), Mockito.eq(this.searchId)))
                .thenReturn(this.registryAndCounterResponse);

        RegistryAndCounterResponse result = this.appServiceImpl.count(this.searchId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(this.registryAndCounterResponse.getSearchId(), result.getSearchId());
        Assertions.assertEquals(this.registryAndCounterResponse.getCount(), result.getCount());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getHotelId(), result.getRegistry().getHotelId());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getCheckIn(), result.getRegistry().getCheckIn());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getCheckOut(), result.getRegistry().getCheckOut());
        Assertions.assertEquals(this.registryAndCounterResponse.getRegistry().getAges(), result.getRegistry().getAges());
        Mockito.verify(logger).info("FIN [AppServiceImpl.count] - Haciendo la logica de count.");
    }

    @Test
    void countServiceNullTest() {
        Optional<RegistryAndCounterEntity> registryAndCounterEntityOptional = Optional.empty();

        RegistryAndCounterResponse result = this.appServiceImpl.count(this.searchId);

        Assertions.assertNull(result);
        Mockito.verify(logger).info("FIN [AppServiceImpl.count] - No se han encontrado búsquedas similares para el 'searchId' proporcionado.");
    }

    @Test
    void countServiceDataAccessResourceFailureExceptionTest() {
        Optional<RegistryAndCounterEntity> registryAndCounterEntityOptional =
                Optional.of(this.registryAndCounterEntity);

        Mockito.when(this.searchRepository.findById(searchId)).thenThrow(new DataAccessResourceFailureException(""));

        Assertions.assertThrows(DataAccessResourceFailureException.class, () ->{
            this.appServiceImpl.count(this.searchId);
        });
    }

    /* Probamos el método privado doAsyncTask mediante reflexión */
    @Test
    void doAsyncTaskOkTest() throws Exception {
        Mockito.doNothing().when(this.asynchronousService).asyncSimulateProcess();

        // obtener y hacer accesible el método privado
        Method doAsync = PruebaTecnicaServiceImpl.class.getDeclaredMethod("doAsyncTask");
        doAsync.setAccessible(true);

        // invocar el método privado
        CompletableFuture<Void> future = (CompletableFuture<Void>) doAsync.invoke(this.appServiceImpl);
        future.join();  // Esperamos a que la tarea asíncrona termine antes de verificar

        Mockito.verify(logger).info("Tarea asíncrona completada.");
    }

    @Test
    void doAsyncTaskFailTest() throws Exception {
        Mockito.doThrow(new NullPointerException())
                .when(this.asynchronousService).asyncSimulateProcess();

        // obtener y hacer accesible el método privado
        Method doAsync = PruebaTecnicaServiceImpl.class.getDeclaredMethod("doAsyncTask");
        doAsync.setAccessible(true);

        // invocar el método privado
        CompletableFuture<Void> future = (CompletableFuture<Void>) doAsync.invoke(this.appServiceImpl);
        future.join();  // Esperamos a que la tarea asíncrona termine antes de verificar

        Mockito.verify(logger).error("Error en la tarea asíncrona");
    }

    /* llamadas de CreateSearch (con Kafka) */
    @Test
    void searchOkTest() throws Exception {
        // Objetos Kafka necesarios para el test
        ProducerRecord<String, RegistryRequestKafkaDTO> producedRecord =
                new ProducerRecord<>(PruebaTecnicaServiceImpl.TOPIC, this.registryRequestKafkaDTO);
        ConsumerRecord<String, SearchIdResponse> consumerRecord =
                new ConsumerRecord<>(PruebaTecnicaServiceImpl.REQUEST_REPLY_TOPIC, 0, 0L, null, this.searchIdResponse);

        // mockitos de los mapeos y llamadas kafka
        Mockito.when(this.registryKafkaMapperImpl.asRegistryRequestJsonKafka(Mockito.eq(this.registryRequest)))
                .thenReturn(this.registryRequestKafkaDTO);
        Mockito.when(sendResultMock.getProducerRecord()).thenReturn(producedRecord);
        Mockito.when(this.sendFuture.get()).thenReturn(this.sendResultMock);
        Mockito.when(this.rrFutureMock.getSendFuture()).thenReturn(this.sendFuture);
        Mockito.when(this.rrFutureMock.get()).thenReturn(consumerRecord);
        Mockito.when(this.kafkaTemplate.sendAndReceive(Mockito.any(ProducerRecord.class))).thenReturn(rrFutureMock);

        // ejecucion
        SearchIdResponse result = this.appServiceImpl.search(this.registryRequest);

        // Asertions y verificaciones
        Assertions.assertNotNull(result);
        Assertions.assertEquals(this.searchIdResponse.getSearchId(), result.getSearchId());
        Mockito.verify(logger).info("INICIO [AppServiceImpl.search] - Haciendo la logica de search.");
        Mockito.verify(logger).info("FIN [AppServiceImpl.search] - Haciendo la logica de search.");
    }

    @Test
    void searchDataAccessResourceFailureExceptionTest() {
        Mockito.when(this.registryKafkaMapperImpl.asRegistryRequestJsonKafka(Mockito.eq(this.registryRequest)))
                .thenReturn(Mockito.mock(RegistryRequestKafkaDTO.class));
        Mockito.when(this.kafkaTemplate.sendAndReceive(Mockito.any(ProducerRecord.class)))
                .thenThrow(new DataAccessResourceFailureException("kafka down"));

        Assertions.assertThrows(DataAccessResourceFailureException.class, () -> {
            this.appServiceImpl.search(this.registryRequest);
        });
    }

    @Test
    void searchGenericExceptionReturnsNullTest() {
        Mockito.when(this.registryKafkaMapperImpl.asRegistryRequestJsonKafka(Mockito.eq(this.registryRequest)))
                .thenReturn(Mockito.mock(RegistryRequestKafkaDTO.class));
        Mockito.when(this.kafkaTemplate.sendAndReceive(Mockito.any(ProducerRecord.class)))
                .thenThrow(new RuntimeException(""));

        SearchIdResponse result = this.appServiceImpl.search(this.registryRequest);

        Assertions.assertNull(result);
        Mockito.verify(logger).error(Mockito.startsWith("Error en request-reply Kafka"));
    }
}
