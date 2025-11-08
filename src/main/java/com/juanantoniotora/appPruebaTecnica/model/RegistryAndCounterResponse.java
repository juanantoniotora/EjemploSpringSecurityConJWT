package com.juanantoniotora.appPruebaTecnica.model;

/**
 * Modelo que contiene la información combinada de búsqueda y contador.
 * Es un modelo de respuesta (response).
 */
public class RegistryAndCounterResponse {

    Long searchId;

    RegistryResponse registry;

    Long count;

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public RegistryResponse getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryResponse registry) {
        this.registry = registry;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
