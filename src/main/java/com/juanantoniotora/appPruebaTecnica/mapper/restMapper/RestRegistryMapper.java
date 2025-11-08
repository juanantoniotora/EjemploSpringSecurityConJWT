package com.juanantoniotora.appPruebaTecnica.mapper.restMapper;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryAndCounterResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestDTO;
import com.juanantoniotora.appPruebaTecnica.dto.SearchIdResponseDTO;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;

public interface RestRegistryMapper {

    /**
     * Transforma un RegistryAndCounterResponse --> en RegistryAndCounterResponseDTO
     * @param src
     * @return obj. tipo RegistryAndCounterResponseDTO
     */
    RegistryAndCounterResponseDTO asRegistryAndCounterResponseDTO(RegistryAndCounterResponse src);


    /**
     * Transforma un RegistryRequestDTO --> a RegistryRequest
     * @param src
     * @return obj. tipo RegistryRequest
     */
    public RegistryRequest asRegistryRequest(RegistryRequestDTO src);

    /**
     * Transforma un SearchIdResponse --> a SearchIdResponseDTO
     * @param src
     * @return obj. tipo SearchIdResponseDTO
     */
    public SearchIdResponseDTO asSearchIdResponseDTO(SearchIdResponse src);
}
