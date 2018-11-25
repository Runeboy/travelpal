package com.rune.travelpal.data.dto.response;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CheckOutResponse {

    @SerializedName("from")
    @Expose
    private String checkInCity;
    @SerializedName("to")
    @Expose
    private String checkOutCity;
    @SerializedName("cost")
    @Expose
    private Integer cost;

    /**
     *
     * @return
     * The checkInCity
     */
    public String getCheckInCity() {
        return checkInCity;
    }

    /**
     *
     * @param checkInCity
     * The checkInCity
     */
    public void setCheckInCity(String checkInCity) {
        this.checkInCity = checkInCity;
    }

    /**
     *
     * @return
     * The checkOutCity
     */
    public String getCheckOutCity() {
        return checkOutCity;
    }

    /**
     *
     * @param checkOutCity
     * The checkOutCity
     */
    public void setCheckOutCity(String checkOutCity) {
        this.checkOutCity = checkOutCity;
    }

    /**
     *
     * @return
     * The cost
     */
    public Integer getCost() {
        return cost;
    }

    /**
     *
     * @param cost
     * The cost
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

}