package com.rune.travelpal.data.dto;

import android.content.Context;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import rune.formatting.StringExpert;
import rune.logging.Logger;

/**
 * Base class for remote data
 */
public class AbstractRemoteDataGateway<RemoteService> {
    // Fields and definitions

    private static final boolean USE_HTTP_LOGGING = true;

    private static String mServiceUrl;

    private static final String UTF8 =  "UTF-8";
    private final Class<RemoteService> mRemoteServiceClass;

    public interface Listener<T> {
        public void onResponse(T response);
        public void onError(String message, Throwable... innerExceptions);
    }

    private Logger log = new Logger(AbstractRemoteDataGateway.class);

    // Constructor

    public AbstractRemoteDataGateway(String serviceUrl, Class<RemoteService> remoteServiceClass, Context context) {
//        mQueue = Volley.newRequestQueue(context);
        mServiceUrl = serviceUrl;
        mRemoteServiceClass = remoteServiceClass;
    }

    // Helper methods

    protected  <T> void invokeServiceMethod(Class<T> resultClass, Listener<T> listener, String method, String[]... args) {
        String url = getServiceUrl(method, args);
        log.v("Invoking service with url: " + url);

        throw new RuntimeException("not impl.");
    }


    private String getServiceUrl(String method, String[]... args) {
        String result = StringExpert.format("{0}/{1}?", mServiceUrl, method);
        for(String[] arg : args)
            result += StringExpert.format("{0}={1}&", URLEncoder.encode(arg[0]), URLEncoder.encode(arg[0])); // the deprecated method is actually preferred due to the possible exceptions incurred by the newer dynamic charset parameter

        return result;
    }

    protected RemoteService createRemoteServiceLink() {
        OkHttpClient.Builder clientBuild = new OkHttpClient.Builder();

        if (USE_HTTP_LOGGING) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            clientBuild = clientBuild.addInterceptor(logging);
        }
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mServiceUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuild.build())
                .build();

        return retrofit.create(mRemoteServiceClass);
    }

}
