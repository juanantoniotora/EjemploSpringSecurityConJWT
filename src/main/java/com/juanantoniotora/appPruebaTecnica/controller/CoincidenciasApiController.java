package com.juanantoniotora.appPruebaTecnica.controller;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryAndCounterResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.SearchIdResponseDTO;
import com.juanantoniotora.appPruebaTecnica.mapper.restMapper.RestRegistryMapperImpl;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import com.juanantoniotora.appPruebaTecnica.service.PruebaTecnicaService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CoincidenciasApiController implements CoincidenciasApi {

    final Logger logger = LoggerFactory.getLogger(CoincidenciasApiController.class);

    @Autowired
    RestRegistryMapperImpl restRegistryMapperImpl;

    @Autowired
    PruebaTecnicaService pruebaTecnicaService;

    @Override
    public ResponseEntity<String> getHome(){
        logger.info("INICIO [AppController.home] - Devolvemos datos de la home.");

        this.pruebaTecnicaService.home();

        logger.info("FIN [AppController.home] - Devolvemos datos de la home.");
        return ResponseEntity.ok("Bienvenido a la HomePage, sin securizar.");
    }

    @Override
    public ResponseEntity<SearchIdResponseDTO> createRegistration(RegistryRequestDTO registryRequestDTO) {
        logger.info("INICIO [AppController.search] - Creando una nueva búsqueda en BBDD.");

        // Validación del request se realiza con @Valid y dentro de NewRegistryRequestDTO
        // Aun así, se puede añadir validaciones adicionales aquí si es necesario.
        // Por ejemplo, comprobar que ciertos campos cumplen condiciones específicas.
        if (registryRequestDTO == null || registryRequestDTO.ages().isEmpty()) {
            this.logger.error("ERROR [AppController.search] - El campo 'ages' no puede contener 0 elementos.");
            return ResponseEntity.badRequest().build();
        }

        try {
            SearchIdResponse searchIdResponse = this.pruebaTecnicaService.search(
                    this.restRegistryMapperImpl.asRegistryRequest(registryRequestDTO));

            logger.info("FIN [AppController.search] - Creando una nueva búsqueda en BBDD.");
            return ResponseEntity.ok(this.restRegistryMapperImpl.asSearchIdResponseDTO(searchIdResponse));
        } catch (DataAccessResourceFailureException e){
            logger.error("ERROR [AppController.search] - Error con BBDD al crear una nueva búsqueda: " + e.getMessage());
            return ResponseEntity.status(503).build();
        } catch (Exception e){
            logger.error("ERROR [AppController.search] - Error creando una nueva búsqueda en BBDD: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<RegistryAndCounterResponseDTO> getCountSimilarRegistrations(@RequestParam @NotNull @Positive Long searchId){
        logger.info("INICIO [AppController.count] - Devolviendo la cantidad de busquedas similares a partir del 'searchId'.");
        RegistryAndCounterResponseDTO response = null;

        // Validación defensiva que no debería de ocurrir nunca
        // Deberia validarse con @NotNull y @Positive en el parámetro de entrada, sin llegar aqui nunca
        if(searchId == null || searchId<=0){
            logger.error("ERROR [AppController.count] - El campo 'searchId' no puede venir nulo ni ser < 0.");
            return ResponseEntity.badRequest().build();
        }

        try {
            response = this.restRegistryMapperImpl.asRegistryAndCounterResponseDTO(this.pruebaTecnicaService.count(searchId));
            if(response == null){
                logger.error("ERROR [AppController.count] - No se han encontrado búsquedas similares para el 'searchId' proporcionado.");
                return ResponseEntity.notFound().build();
            }
            logger.info("FIN [AppController.count] - Devolviendo la cantidad de busquedas similares a partir del 'searchId'.");
            return ResponseEntity.ok(response);
        } catch (DataAccessResourceFailureException e) {
            logger.error("ERROR [AppController.search] - Error con BBDD al crear una nueva búsqueda: " + e.getMessage());
            return ResponseEntity.status(503).build();
        } catch (Exception e){
            logger.error("ERROR [AppController.count] - Error devolviendo las busquedas similares: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
