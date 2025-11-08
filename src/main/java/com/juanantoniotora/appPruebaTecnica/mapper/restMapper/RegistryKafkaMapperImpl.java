package com.juanantoniotora.appPruebaTecnica.mapper.restMapper;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestKafkaDTO;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import org.springframework.stereotype.Component;

@Component
public class RegistryKafkaMapperImpl implements RegistryKafkaMapper{

    @Override
    public RegistryRequestKafkaDTO asRegistryRequestJsonKafka(RegistryRequest src) {
        if(src == null){
            return null;
        }
        RegistryRequestKafkaDTO registryRequestKafkaDTO = new RegistryRequestKafkaDTO();
        registryRequestKafkaDTO.setHotelId(src.hotelId());
        registryRequestKafkaDTO.setCheckIn(src.checkIn());
        registryRequestKafkaDTO.setCheckOut(src.checkOut());
        registryRequestKafkaDTO.setAges(src.ages());
        return registryRequestKafkaDTO;
    }
}
