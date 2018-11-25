package com.rune.travelpal.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.rune.travelpal.data.Broadcasts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rune.location.StrictlyImprovingLocationListener;
import rune.logging.Logger;
import rune.service.ServiceExpert;
import rune.service.StickyService;

// A sticky service, that continously retrieves tracking info
public class LocationService extends StickyService implements StrictlyImprovingLocationListener.Listener {

    // Fields

    private static final Logger log = new Logger(LocationService.class);

    private LocationListener mLocationListener = new StrictlyImprovingLocationListener(this);

    private ServiceExpert mServices = new ServiceExpert(this);

    private List<Location> mLocations = new ArrayList<>();

    private Date mStartTime = new Date();

    private static LocationService mInstance;

    // Constructor

    public LocationService() {
        mInstance = this;
    }

    // Interface implementations

    @Override
    public void onBetterLocation(Location location) {
        log.d("Received a (better) location");

        mLocations.add(location);

        sendBroadcast(new Intent(Broadcasts.BROADCAST_TRACKING_DATA_CHANGED));
    }

    // Overrides

    @Override
    public void onCreate() {
        super.onCreate();

        log.v("onCreate called");

        requestLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        log.v("onDestroy called");

        removeLocationUpdate();

        mInstance = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log.v("onBind called");

        return  super.onBind(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.v("onStartCommand called");

        return super.onStartCommand(intent, flags, startId);
    }

    // Methods

    public List<Location> getTrackedLocations() {
        return mLocations;
    }

    public static void ensureIsStarted(Context context) {
        log.d("Starting service via intent");

        context.startService(new Intent(context, LocationService.class));
    }

    public static void stop(Context context) {
        log.d("Stopping service via intent");

//        mInstance.stopSelf();
        context.stopService(new Intent(context, LocationService.class));
    }


    public int getUptimeSeconds() {
        return mStartTime.getSeconds() - new Date().getSeconds();
    }

    public Date getStartTime() {
        return mStartTime;
    }

    private void requestLocationUpdates() {
        log.i("requesting location update..");


        LocationManager locationManager = mServices.getLocationManager();

        if (locationManager == null) {
            log.e("Cannot request location updates, it appears that you cannot use locations on this device");
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            log.e("Cannot request location updates, it appears that the application does not have permission to access fine locations, whic is required");
            return;
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        catch (Exception e) {
            log.e("Exception during location update request: " +  e.getMessage());
        }
    }

    private void removeLocationUpdate() {
        LocationManager locationManager = mServices.getLocationManager();
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                log.d("removing location update");

                locationManager.removeUpdates(mLocationListener);
            }
        }
    }

}
