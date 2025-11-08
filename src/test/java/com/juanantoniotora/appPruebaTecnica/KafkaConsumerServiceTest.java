package com.juanantoniotora.appPruebaTecnica;

import com.juanantoniotora.appPruebaTecnica.dto.*;
import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import com.juanantoniotora.appPruebaTecnica.kafka.KafkaConsumerServiceImpl;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import com.juanantoniotora.appPruebaTecnica.repository.SearchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import java.lang.reflect.Field;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {
    @InjectMocks
    private KafkaConsumerServiceImpl kafkaConsumerServiceImpl;

    @Mock
    private Logger logger;

    @Mock
    SearchRepository searchRepository;

    RegistryRequestKafkaDTO registryRequestKafkaDTO = new RegistryRequestKafkaDTO();

    RegistryAndCounterEntity entity = new RegistryAndCounterEntity();

    SearchIdResponse searchIdResponse = new SearchIdResponse();

    @BeforeEach
    void SetUp() throws NoSuchFieldException, IllegalAccessException {
        this.logger = Mockito.mock(Logger.class);

        final Field loggerField = KafkaConsumerServiceImpl.class.getDeclaredField(("logger"));
        loggerField.setAccessible(true);
        loggerField.set(this.kafkaConsumerServiceImpl, this.logger);

        this.searchIdResponse.setSearchId(502L);

        this.registryRequestKafkaDTO.setHotelId("1234aBc");
        this.registryRequestKafkaDTO.setCheckIn("29/12/2023");
        this.registryRequestKafkaDTO.setCheckOut("31/12/2023");
        this.registryRequestKafkaDTO.setAges(List.of(30,29,1,3));

        this.entity.setSearchId(502L);
        this.entity.setHotelId(this.registryRequestKafkaDTO.getHotelId());
        this.entity.setCheckIn(this.registryRequestKafkaDTO.getCheckIn());
        this.entity.setCheckOut(this.registryRequestKafkaDTO.getCheckOut());
        this.entity.setAges(this.registryRequestKafkaDTO.getAges());
    }

    /**
     * Here start test for the Mappers y EntityMappers
     */


    /**
     * Here start test for the Services
     */
//    @Test
//    void searchListenerOkTest(){
//        Mockito.when(this.searchRepository.save(Mockito.any(RegistryAndCounterEntity.class)))
//                .thenAnswer(invocation -> {
//                    RegistryAndCounterEntity arg = invocation.getArgument(0);
//                    // Simular que el repositorio establece el searchId al guardar
//                    arg.setSearchId(this.searchIdResponse.getSearchId());
//                    return arg;
//                });
//
//        SearchIdResponse result = this.kafkaConsumerServiceImpl.searchListener(this.registryRequestKafkaDTO);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(this.searchIdResponse.getSearchId(), result.getSearchId());
//        Mockito.verify(this.searchRepository).save(Mockito.argThat(saved ->
//                saved.getHotelId().equals(this.registryRequestKafkaDTO.getHotelId()) &&
//                        saved.getCheckIn().equals(this.registryRequestKafkaDTO.getCheckIn()) &&
//                        saved.getCheckOut().equals(this.registryRequestKafkaDTO.getCheckOut()) &&
//                        saved.getAges().equals(this.registryRequestKafkaDTO.getAges())
//        ));
//    }
//
//    @Test
//    void searchListenerDataAccessResourceFailureExceptionTest(){
//        Mockito.when(this.searchRepository.save(Mockito.any(RegistryAndCounterEntity.class)))
//                .thenThrow(new DataAccessResourceFailureException("Error al guardar en BBDD"));
//
//        Assertions.assertThrows(DataAccessResourceFailureException.class, () -> {
//            this.kafkaConsumerServiceImpl.searchListener(this.registryRequestKafkaDTO);
//        });
//    }
}
