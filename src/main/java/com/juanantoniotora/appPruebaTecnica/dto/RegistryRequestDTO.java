package com.juanantoniotora.appPruebaTecnica.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

public record RegistryRequestDTO(@NotBlank String hotelId, @NotBlank String checkIn, @NotBlank String checkOut,
                                 List<Integer> ages) implements Serializable {

    /**
     * Constructor canónico compacto.
     * Sin parámetros, aunque trata de asignar valores a todos los atributos.
     * Realiza validaciones a los atributos convenientes.
     */
    public RegistryRequestDTO {
        Objects.requireNonNull(hotelId, "hotelId es null");
        if (hotelId.isBlank()) throw new IllegalArgumentException("hotelId vacío");

        Objects.requireNonNull(checkIn, "checkIn es null");
        if (checkIn.isBlank()) throw new IllegalArgumentException("checkIn vacío");

        Objects.requireNonNull(checkOut, "checkOut es null");
        if (checkOut.isBlank()) throw new IllegalArgumentException("checkOut vacío");

        ages = (ages == null) ? new ArrayList<>() : ages;
    }
}
