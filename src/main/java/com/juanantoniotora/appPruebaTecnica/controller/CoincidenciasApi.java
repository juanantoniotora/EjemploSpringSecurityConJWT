package com.juanantoniotora.appPruebaTecnica.controller;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryAndCounterResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestDTO;
import com.juanantoniotora.appPruebaTecnica.dto.SearchIdResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
public interface CoincidenciasApi {

    /**
     * GET /home : getHome
     * Devuelve un mensaje de bienvenida para la home.
     * Este endpoint no esta securizado bajo JWT.
     * @return OK (status code 200)
     *         or Bad Request (status code 400)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     *         or NOT_IMPLEMENTED (status code 501)
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/home",
            produces = { "application/json" }
    )
    default ResponseEntity<String> getHome() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    /**
     * POST /v1/registrations/registration : createSearch
     * Crea una entrada en BBDD con los datos de registro.
     * Endpoint SI securizado bajo basic-auth.
     * @return OK (status code 200)
     *         or Bad Request (status code 400)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     *         or NOT_IMPLEMENTED (status code 501)
     *         or Service Unavailable (status code 503)
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/v1/registrations/registration",
            produces = { "application/json" }
    )
    default ResponseEntity<SearchIdResponseDTO> createRegistration(@NotNull @Valid @RequestBody RegistryRequestDTO registryRequestDTO) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    /**
     * GET /v1/registrations/count : getCount
     * Lee las entradas de BBDD, y devuelve la cantidad de entradas similares que hay similares a la que se introdujo.
     * Endpoint SI securizado bajo basic-auth.
     * @return OK (status code 200)
     *         or Bad Request (status code 400)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     *         or NOT_IMPLEMENTED (status code 501)
     *         or Service Unavailable (status code 503)
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/v1/registrations/count",
            produces = { "application/json" }
    )
    default ResponseEntity<RegistryAndCounterResponseDTO> getCountSimilarRegistrations(@RequestParam @NotNull @Positive Long searchId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


}
