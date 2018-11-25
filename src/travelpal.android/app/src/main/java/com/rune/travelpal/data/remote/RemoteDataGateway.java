package com.rune.travelpal.data.remote;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rune.logging.Logger;
import com.rune.travelpal.data.dto.AbstractRemoteDataGateway;
import com.rune.travelpal.data.dto.response.CheckOutResponse;
import com.rune.travelpal.data.dto.response.GetNewClientIdResponse;
import com.rune.travelpal.data.dto.response.GetUserStatusResponse;

/**
 * A data gateway for remote data, uses Volley internally (in base class) to accomplish the communication
 */
public class RemoteDataGateway extends AbstractRemoteDataGateway<RemoteService> {

    // Fields and definitions

    private Logger log = new Logger(RemoteDataGateway.class);

    private static final boolean doesGoogleCloudSupportPostRequestAsRequiredByFirebase = false; // Just to make a work-around of specific technical challenges explicit: the backend web service can't post to firebase, because it's a free/constrained developer plan!

    // Constructor

    public RemoteDataGateway(String serviceUrl, Context context) {
        super(serviceUrl, RemoteService.class, context);
    }

    // Methods

    public void getClientStatus(String userId, Listener<GetUserStatusResponse> listener) {
        queryService(createRemoteServiceLink().getUserStatus(userId), listener);
    }


    public void checkOut(String userId, String toCity, Listener<CheckOutResponse> listener) {
        queryService(createRemoteServiceLink().checkOut(userId, toCity), listener);
    }

    public void checkIn(String userId, String fromCity, Listener<GetUserStatusResponse> listener) {
        queryService(createRemoteServiceLink().checkIn(userId, fromCity), listener);
    }

    public void requestNewClientId(Listener<GetNewClientIdResponse> listener) {
        //invokeServiceMethod(NewClientId.class, listener, "getNewClientId");// new String[][] { {"", ""}, {} }

        queryService(createRemoteServiceLink().getNewClientId(), listener);
    }

    private <Result> void queryService(Call<Result> call, final Listener<Result> listener) {
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response) {
                log.d("response received from remote service, success: " + response.isSuccess());
                if (!response.isSuccess())
                    listener.onError("Response from service query indicates that the call is not a success.");
                else
                    listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                log.e("Failed to query remote service");

                listener.onError("Failed to query service.", t);
            }
        });
    }

//    public void requestCheckIn(String clientId, Listener<Boolean> listener) {
//        if (doesGoogleCloudSupportPostRequestAsRequiredByFirebase)
//            throw new RuntimeException("This exception will not be thrown");
//
//        invokeServiceMethod(Boolean.class, listener, "checkIn");// new String[][] { {"", ""}, {} }
//    }


}
