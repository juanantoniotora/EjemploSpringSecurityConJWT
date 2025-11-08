package com.juanantoniotora.appPruebaTecnica.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RegistryRequest(@NotBlank String hotelId,
                              @NotBlank String checkIn,
                              @NotBlank String checkOut,
                              List<Integer> ages){

   // Constructor canónico no se puede crear manualmente, pero queda incluido automaticamente.


    /**
     * Constructor adicional que inicializa "hotelId" solamente.
     * Resto de atributos a null.
     */
    public RegistryRequest(String hotelId) {
        this(hotelId, null, null, null);
    }

}
