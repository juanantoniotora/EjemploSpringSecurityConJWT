package com.juanantoniotora.appPruebaTecnica.mapper.restMapper;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestKafkaDTO;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;

public interface RegistryKafkaMapper {

    /**
     * Transforma un RegistryRequest  --> en RegistryRequestKafkaDTO
     * @param src
     * @return RegistryRequestKafkaDTO
     */
    RegistryRequestKafkaDTO asRegistryRequestJsonKafka(RegistryRequest src);
}
