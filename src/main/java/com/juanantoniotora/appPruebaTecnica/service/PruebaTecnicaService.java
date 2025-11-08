package com.juanantoniotora.appPruebaTecnica.service;

import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;

public interface PruebaTecnicaService {

    /**
     * Procesa la búsqueda y devuelve un SearchIdResponse
     * @param registryRequest
     * @return SearchIdResponse
     */
    public SearchIdResponse search(RegistryRequest registryRequest);


    /**
     * Recibe un searchId y devuelve la cantidad de búsquedas similares encontradas
     * @param searchId
     * @return CountResponseDTO
     */
    public RegistryAndCounterResponse count(Long searchId);



    public void home();

}

