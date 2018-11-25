package com.rune.travelpal.data.dto.response;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Generated("org.jsonschema2pojo")
public class GetUserStatusResponse {

    @SerializedName("from")
    @Expose
    private String checkInCity;
    @SerializedName("isCheckedIn")
    @Expose
    private Boolean isCheckedIn;

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
     * The isCheckedIn
     */
    public Boolean getIsCheckedIn() {
        return isCheckedIn;
    }

    /**
     *
     * @param isCheckedIn
     * The isCheckedIn
     */
    public void setIsCheckedIn(Boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }

}