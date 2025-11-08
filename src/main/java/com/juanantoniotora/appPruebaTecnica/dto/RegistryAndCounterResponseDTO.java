package com.juanantoniotora.appPruebaTecnica.dto;

/**
 * Modelo que contiene la información combinada de búsqueda y contador.
 * Es un modelo de respuesta (response).
 */
public class RegistryAndCounterResponseDTO {

    Long searchId;

    RegistryResponseDTO registryResponseDTO;

    Long count;

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public RegistryResponseDTO getRegistryResponseDTO() {
        return registryResponseDTO;
    }

    public void setRegistryResponseDTO(RegistryResponseDTO registryResponseDTO) {
        this.registryResponseDTO = registryResponseDTO;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
