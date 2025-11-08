package com.juanantoniotora.appPruebaTecnica;

import com.juanantoniotora.appPruebaTecnica.controller.CoincidenciasApi;
import com.juanantoniotora.appPruebaTecnica.controller.CoincidenciasApiController;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryAndCounterResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.SearchIdResponseDTO;
import com.juanantoniotora.appPruebaTecnica.mapper.restMapper.RestRegistryMapperImpl;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import com.juanantoniotora.appPruebaTecnica.model.RegistryResponse;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import com.juanantoniotora.appPruebaTecnica.service.PruebaTecnicaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PruebaTecnicaApiControllerTest {
    @InjectMocks
    private CoincidenciasApiController coincidenciasApiController;

    @Mock
    private Logger logger;

    @Mock
    PruebaTecnicaService pruebaTecnicaService;

    @Spy
    RestRegistryMapperImpl restRegistryMapperImpl;

    @Spy
    private CoincidenciasApi coincidenciasApi;

    RegistryAndCounterResponse registryAndCounterResponse;

    RegistryAndCounterResponseDTO registryAndCounterResponseDTO;

    RegistryResponse registryResponse;

    RegistryResponseDTO registryResponseDTO;

    RegistryRequestDTO registryRequestDTO;

    RegistryRequest registryRequest;

    SearchIdResponse searchIdResponse = new SearchIdResponse();

    SearchIdResponseDTO searchIdResponseDTO = new SearchIdResponseDTO();

    Long searchId = 502L;

    @BeforeEach
    void SetUp() throws NoSuchFieldException, IllegalAccessException {

        this.logger = Mockito.mock(Logger.class);

        final Field loggerField = CoincidenciasApiController.class.getDeclaredField(("logger"));
        loggerField.setAccessible(true);
        loggerField.set(this.coincidenciasApiController, this.logger);

        this.registryResponse = new RegistryResponse();
        this.registryResponse.setHotelId("1234aBc");
        this.registryResponse.setCheckIn("29/12/2023");
        this.registryResponse.setCheckOut("31/12/2023");
        this.registryResponse.setAges(List.of(30,29,1,3));

        this.registryAndCounterResponse = new RegistryAndCounterResponse();
        this.registryAndCounterResponse.setSearchId(502L);
        this.registryAndCounterResponse.setRegistry(this.registryResponse);
        this.registryAndCounterResponse.setCount(4L);

        this.registryResponseDTO = new RegistryResponseDTO();
        this.registryResponseDTO.setHotelId("1234aBc");
        this.registryResponseDTO.setCheckIn("29/12/2023");
        this.registryResponseDTO.setCheckOut("31/12/2023");
        this.registryResponseDTO.setAges(List.of(30,29,1,3));

        this.registryAndCounterResponseDTO = new RegistryAndCounterResponseDTO();
        this.registryAndCounterResponseDTO.setSearchId(502L);
        this.registryAndCounterResponseDTO.setRegistryResponseDTO(this.registryResponseDTO);
        this.registryAndCounterResponseDTO.setCount(4L);

        // incializamos varios objetos RegistryRequestDTO para probar distintos casos del RECORD
        this.registryRequestDTO = new RegistryRequestDTO("1234aBc","29/12/2023","31/12/2023",List.of(30,29,1,3));

        // Seguimos con el resto de inicializaciones
        this.registryRequest = new RegistryRequest("1234aBc","29/12/2023","31/12/2023",List.of(30,29,1,3));

        this.searchIdResponse.setSearchId(this.searchId);

        this.searchIdResponseDTO.setSearchId(this.searchId);
    }

    /**
     * Here start test for the Interfaces
     */
    @Test
    void getHomeInterfaceTest(){
        Assertions.assertEquals(HttpStatus.NOT_IMPLEMENTED, this.coincidenciasApi.getHome().getStatusCode());
    }

    @Test
    void getCountInterfaceTest(){
        Assertions.assertEquals(HttpStatus.NOT_IMPLEMENTED, this.coincidenciasApi.getCountSimilarRegistrations(1L).getStatusCode());
    }

    @Test
    void createSearchInterfaceTest(){
        Assertions.assertEquals(HttpStatus.NOT_IMPLEMENTED, this.coincidenciasApi.createRegistration(null).getStatusCode());
    }

    /**
     * Here start test for the RestMappers
     */
    @Test
    void asRegistryAndCounterResponseDTOOkTest() {
        final RegistryAndCounterResponseDTO result =
                this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(this.registryAndCounterResponse);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getSearchId(), result.getSearchId());
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getCount(), result.getCount());
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getRegistryResponseDTO().getHotelId(),
                result.getRegistryResponseDTO().getHotelId());
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getRegistryResponseDTO().getCheckIn(),
                result.getRegistryResponseDTO().getCheckIn());
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getRegistryResponseDTO().getCheckOut(),
                result.getRegistryResponseDTO().getCheckOut());
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getRegistryResponseDTO().getAges(),
                result.getRegistryResponseDTO().getAges());
    }

    @Test
    void asRegistryAndCounterResponseDTONullTest() {
        final RegistryAndCounterResponseDTO result =
                this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(null);

        Assertions.assertNull(result);
    }

    @Test
    void asRegistryRequestOkTest() {
        final RegistryRequest result =
                this.restRegistryMapperImpl.asRegistryRequest(this.registryRequestDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(this.registryRequest.hotelId(), result.hotelId());
        Assertions.assertEquals(this.registryRequest.checkIn(), result.checkIn());
        Assertions.assertEquals(this.registryRequest.checkOut(), result.checkOut());
        Assertions.assertEquals(this.registryRequest.ages(), result.ages());
    }

    @Test
    void asRegistryRequestNullTest() {
        final RegistryRequest result =
                this.restRegistryMapperImpl.asRegistryRequest(null);

        Assertions.assertNull(result);
    }

    @Test
    void asSearchIdResponseDTOOkTest() {
        final SearchIdResponseDTO result = this.restRegistryMapperImpl.asSearchIdResponseDTO(searchIdResponse);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(this.searchIdResponseDTO.getSearchId(), result.getSearchId());
    }
    @Test
    void asSearchIdResponseDTONullTest() {
        final SearchIdResponseDTO result = this.restRegistryMapperImpl.asSearchIdResponseDTO(null);

        Assertions.assertNull(result);
    }

    /* Probamos el RECORD RegistryRequestDTO con datos invalidos */
    @Test
    void RegistryRequestDTOFailTest() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            RegistryRequestDTO registryRequestDTO1 = new RegistryRequestDTO("","29/12/2023","31/12/2023",List.of(30,29,1,3));
        });
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            RegistryRequestDTO registryRequestDTO2 = new RegistryRequestDTO("1234aBc","","31/12/2023",List.of(30,29,1,3));
        });
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            RegistryRequestDTO registryRequestDTO3 = new RegistryRequestDTO("1234aBc","29/12/2023","",List.of(30,29,1,3));
        });

        RegistryRequestDTO registryRequestDTO4 = new RegistryRequestDTO("1234aBc","29/12/2023","31/12/2023",null);
        Assertions.assertEquals(new ArrayList<>(), registryRequestDTO4.ages());
    }

    /**
     * Here start test for the Controller
     */
    @Test
    void getHomeControllerOkTest() {
        ResponseEntity<String> result = this.coincidenciasApiController.getHome();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals("Bienvenido a la HomePage, sin securizar.", result.getBody());
        Mockito.verify(logger).info("INICIO [AppController.home] - Devolvemos datos de la home.");
        Mockito.verify(logger).info("FIN [AppController.home] - Devolvemos datos de la home.");
    }

    @Test
    void createSearchAppApiControllerOkTest() {
        Mockito.when(this.restRegistryMapperImpl.asRegistryRequest(registryRequestDTO)).thenReturn(this.registryRequest);
        Mockito.when(this.pruebaTecnicaService.search(this.registryRequest)).thenReturn(this.searchIdResponse);

        ResponseEntity<SearchIdResponseDTO> result = this.coincidenciasApiController.createRegistration(this.registryRequestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(this.searchIdResponseDTO.getSearchId(), result.getBody().getSearchId());
        Mockito.verify(logger).info("FIN [AppController.search] - Creando una nueva búsqueda en BBDD.");
    }

    @Test
    void createSearchAppApiControllerBadRequestTest(){
        RegistryRequestDTO registryRequestDTO1 = new RegistryRequestDTO("1234aBc","29/12/2023","31/12/2023",List.of());
        RegistryRequestDTO registryRequestDTO2 = null;

        // probamos con una lista de edades vacía
        ResponseEntity<SearchIdResponseDTO> result1 = this.coincidenciasApiController.createRegistration(registryRequestDTO1);
        Assertions.assertEquals(400, result1.getStatusCode().value());

        // probamos con un objeto nulo
        ResponseEntity<SearchIdResponseDTO> result2 = this.coincidenciasApiController.createRegistration(registryRequestDTO1);
        Assertions.assertEquals(400, result2.getStatusCode().value());
    }

    @Test
    void createSearchAppApiControllerDataAccessResourceFailureExceptionTest(){
        Mockito.when(this.restRegistryMapperImpl.asRegistryRequest(registryRequestDTO)).thenReturn(this.registryRequest);
        Mockito.when(this.pruebaTecnicaService.search(this.registryRequest)).thenThrow(new DataAccessResourceFailureException("Database not reachable"));

        ResponseEntity<SearchIdResponseDTO> result = this.coincidenciasApiController.createRegistration(this.registryRequestDTO);

        Assertions.assertEquals(503, result.getStatusCode().value());
    }

    @Test
    void createSearchAppApiControllerExceptionTest(){
        Mockito.when(this.restRegistryMapperImpl.asRegistryRequest(registryRequestDTO)).thenReturn(this.registryRequest);
        Mockito.when(this.pruebaTecnicaService.search(this.registryRequest)).thenThrow(new NullPointerException());

        ResponseEntity<SearchIdResponseDTO> result = this.coincidenciasApiController.createRegistration(this.registryRequestDTO);

        Assertions.assertEquals(500, result.getStatusCode().value());
    }

    @Test
    void getCountAppApiControllerOkTest() {
        Mockito.when(this.pruebaTecnicaService.count(this.searchId)).thenReturn(this.registryAndCounterResponse);
        Mockito.when(this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(registryAndCounterResponse)).thenReturn(this.registryAndCounterResponseDTO);

        ResponseEntity<RegistryAndCounterResponseDTO> result = this.coincidenciasApiController.getCountSimilarRegistrations(this.searchId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(this.registryAndCounterResponseDTO.getSearchId(), result.getBody().getSearchId());
        Mockito.verify(logger).info("FIN [AppController.count] - Devolviendo la cantidad de busquedas similares a partir del 'searchId'.");
    }

    @Test
    void getCountAppApiControllerNotFoundTest() {
        Mockito.when(this.pruebaTecnicaService.count(this.searchId)).thenReturn(this.registryAndCounterResponse);
        Mockito.when(this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(registryAndCounterResponse)).thenReturn(null);

        ResponseEntity<RegistryAndCounterResponseDTO> result = this.coincidenciasApiController.getCountSimilarRegistrations(this.searchId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Mockito.verify(logger).error("ERROR [AppController.count] - No se han encontrado búsquedas similares para el 'searchId' proporcionado.");
    }

    @Test
    void getCountAppApiControllerBadRequestTest() {
        ResponseEntity<RegistryAndCounterResponseDTO> result1 = this.coincidenciasApiController.getCountSimilarRegistrations(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result1.getStatusCode());

        ResponseEntity<RegistryAndCounterResponseDTO> result2 = this.coincidenciasApiController.getCountSimilarRegistrations(0L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result2.getStatusCode());

        ResponseEntity<RegistryAndCounterResponseDTO> result3 = this.coincidenciasApiController.getCountSimilarRegistrations(-1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result3.getStatusCode());
    }

    @Test
    void getCountAppApiControllerDataAccessResourceFailureExceptionTest() {
        Mockito.when(this.pruebaTecnicaService.count(this.searchId)).thenReturn(this.registryAndCounterResponse);
        Mockito.when(this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(registryAndCounterResponse)).thenThrow(new DataAccessResourceFailureException(""));

        ResponseEntity<RegistryAndCounterResponseDTO> result = this.coincidenciasApiController.getCountSimilarRegistrations(this.searchId);

        Assertions.assertEquals(503, result.getStatusCode().value());
    }

    @Test
    void getCountAppApiControllerExceptionTest() {
        Mockito.when(this.pruebaTecnicaService.count(this.searchId)).thenReturn(this.registryAndCounterResponse);
        Mockito.when(this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(registryAndCounterResponse)).thenThrow(new NullPointerException());

        ResponseEntity<RegistryAndCounterResponseDTO> result = this.coincidenciasApiController.getCountSimilarRegistrations(this.searchId);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
