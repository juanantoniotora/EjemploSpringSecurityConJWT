package com.juanantoniotora.appPruebaTecnica.entity;

import jakarta.persistence.*;
import java.util.List;


/**
 * Entity que representa una tupla de Entity de tabla "registry" y el contador de busquedas similares.
 * El id de la tupla viene dado como autogenerado por atributo 'searchId'.
 * El atributo 'count' viene despues de todos los atributos, como TRANSIENT para que no se guarde en BBDD.
 */
@Entity
@Table(name = "registry")
public class RegistryAndCounterEntity {

    /* Attribute that represents a tuple of the "registry" table entity and the counter of similar searches. */

    @Id
    @GeneratedValue
    @Column(name = "search_id")
    public Long searchId;

    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "check_in")
    private String checkIn;

    @Column(name = "check_out")
    private String checkOut;

    @Column(name = "ages")
    private List<Integer> ages;

    @Transient
    private Long count = 0L; // atributo que no se guarda en BBDD, solo para devolver el contador de busquedas similares

    /* Constructor no parametherized isn't visible, after getters and setters */

    public RegistryAndCounterEntity() {
    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
