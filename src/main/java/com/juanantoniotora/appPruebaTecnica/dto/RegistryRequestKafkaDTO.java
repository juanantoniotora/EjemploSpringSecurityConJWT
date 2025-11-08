package com.juanantoniotora.appPruebaTecnica.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RegistryRequestKafkaDTO {

    @JsonProperty("hotelId")
    private String hotelId;

    @JsonProperty("checkIn")
    private String checkIn;

    @JsonProperty("checkOut")
    private String checkOut;

    @JsonProperty("ages")
    private List<Integer> ages;

    @JsonProperty("hotelId")
    public String getHotelId() {
        return hotelId;
    }

    @JsonProperty("hotelId")
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    @JsonProperty("checkIn")
    public String getCheckIn() {
        return checkIn;
    }

    @JsonProperty("checkIn")
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    @JsonProperty("checkOut")
    public String getCheckOut() {
        return checkOut;
    }

    @JsonProperty("checkOut")
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    @JsonProperty("ages")
    public List<Integer> getAges() {
        return ages;
    }

    @JsonProperty("ages")
    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }
}
