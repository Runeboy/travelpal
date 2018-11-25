package com.rune.travelpal.data.dto.response;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Generated("org.jsonschema2pojo")
public class GetNewClientIdResponse {

    @SerializedName("clientId")
    @Expose
    private String clientId;

    /**
     *
     * @return
     * The clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     *
     * @param clientId
     * The clientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}