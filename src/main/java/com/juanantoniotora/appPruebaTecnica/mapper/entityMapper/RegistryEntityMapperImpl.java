package com.juanantoniotora.appPruebaTecnica.mapper.entityMapper;

import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryResponse;
import org.springframework.stereotype.Component;

@Component
public class RegistryEntityMapperImpl implements RegistryEntityMapper {

    @Override
    public RegistryAndCounterResponse asRegistryAndCounterResponse(Long searchId, RegistryAndCounterEntity registryAndCounterEntity, Long count) {
        if(searchId == null || registryAndCounterEntity == null || count == null){
            return null;
        }
        RegistryAndCounterResponse registryAndCounterResponse = new RegistryAndCounterResponse();
        registryAndCounterResponse.setSearchId(searchId);
        registryAndCounterResponse.setRegistry(this.asRegsitryResponse(registryAndCounterEntity));
        registryAndCounterResponse.setCount(registryAndCounterEntity.getCount());
        return registryAndCounterResponse;
    }

    private RegistryResponse asRegsitryResponse(RegistryAndCounterEntity src) {
        if(src == null){
            return null;
        }
        RegistryResponse registryResponse = new RegistryResponse();
        registryResponse.setHotelId(src.getHotelId());
        registryResponse.setCheckIn(src.getCheckIn());
        registryResponse.setCheckOut(src.getCheckOut());
        registryResponse.setAges(src.getAges());
        // omitimos el count que vienen en la entidad porque no forma parte del RegistryResponse modelo.

        return registryResponse;
    }
}
