package com.juanantoniotora.appPruebaTecnica.mapper.restMapper;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryAndCounterResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.RegistryResponseDTO;
import com.juanantoniotora.appPruebaTecnica.dto.SearchIdResponseDTO;
import com.juanantoniotora.appPruebaTecnica.model.RegistryAndCounterResponse;
import com.juanantoniotora.appPruebaTecnica.model.RegistryRequest;
import com.juanantoniotora.appPruebaTecnica.model.RegistryResponse;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;
import org.springframework.stereotype.Component;

@Component
public class RestRegistryMapperImpl implements RestRegistryMapper {

    @Override
    public RegistryAndCounterResponseDTO asRegistryAndCounterResponseDTO(RegistryAndCounterResponse src) {
        if(src == null){
            return null;
        }
        RegistryAndCounterResponseDTO registryAndCounterResponseDTO = new RegistryAndCounterResponseDTO();
        registryAndCounterResponseDTO.setCount(src.getCount());
        registryAndCounterResponseDTO.setSearchId(src.getSearchId());
        registryAndCounterResponseDTO.setRegistryResponseDTO(this.asRegistryResponseDTO(src.getRegistry()));
        return registryAndCounterResponseDTO;
    }

    private RegistryResponseDTO asRegistryResponseDTO(RegistryResponse src) {
        if(src == null){
            return null;
        }
        RegistryResponseDTO registryResponseDTO = new RegistryResponseDTO();
        registryResponseDTO.setHotelId(src.getHotelId());
        registryResponseDTO.setCheckIn(src.getCheckIn());
        registryResponseDTO.setCheckOut(src.getCheckOut());
        registryResponseDTO.setAges(src.getAges());
        return registryResponseDTO;
    }

    @Override
    public RegistryRequest asRegistryRequest(RegistryRequestDTO src) {
        if(src == null){
            return null;
        }
        return new RegistryRequest(src.hotelId(), src.checkIn(), src.checkOut(), src.ages());
    }

    @Override
    public SearchIdResponseDTO asSearchIdResponseDTO(SearchIdResponse src) {
        if(src == null){
            return null;
        }
        SearchIdResponseDTO searchIdResponseDTO = new SearchIdResponseDTO();
        searchIdResponseDTO.setSearchId(src.getSearchId());
        return searchIdResponseDTO;
    }
}
