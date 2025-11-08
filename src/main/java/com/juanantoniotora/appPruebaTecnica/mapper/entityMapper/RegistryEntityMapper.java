package com.juanantoniotora.appPruebaTecnica.mapper.entityMapper;

import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;

public interface RegistryEntityMapper {

    /**
     * Transforma un searchId, searchResponse y count --> em CountResponse.
     *
     * Cambia un poco la estructura respecto a Entity para preparar el modelo de respuesta:
     *      {
     *          "searchId": "xxxxx",
     *          "search": {
     *              "hotelId": "1234aBc",
     *              "checkIn": "29/12/2023",
     *              "checkOut": "31/12/2023",
     *              "ages": [3, 29, 30, 1]
     *          }
     *          "count": 100
     *      }
     * @param searchId
     * @param registryAndCounterEntity
     * @param count
     * @return obj. tipo CountResponseDTO
     */
    RegistryAndCounterResponse asRegistryAndCounterResponse(Long searchId, RegistryAndCounterEntity registryAndCounterEntity, Long count);

}
