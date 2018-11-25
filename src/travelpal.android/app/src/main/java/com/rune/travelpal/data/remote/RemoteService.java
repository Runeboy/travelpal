package com.rune.travelpal.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.rune.travelpal.data.dto.response.CheckOutResponse;
import com.rune.travelpal.data.dto.response.GetNewClientIdResponse;
import com.rune.travelpal.data.dto.response.GetUserStatusResponse;

// Retrofit interface to the remote service.
public interface RemoteService {

    @GET("getNewClientId")
    Call<GetNewClientIdResponse> getNewClientId();

    @GET("getUserStatus")
    Call<GetUserStatusResponse> getUserStatus(
            @Query("userId") String userId
    );

    @GET("checkIn")
    Call<GetUserStatusResponse> checkIn(
            @Query("userId") String userId,
            @Query("from") String checkInCity
    );

    @GET("checkOut")
    Call<CheckOutResponse> checkOut(
            @Query("userId") String userId,
            @Query("to") String checkOutCity
    );

    @GET("getPriceQuote")
    Call<CheckOutResponse> getPriceQuote(
            @Query("from") String from,
            @Query("to") String to
    );

}
