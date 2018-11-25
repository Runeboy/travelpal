package rune.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import rune.logging.Logger;


public class LocationExpert {

    // Fields

    private static final Logger log = new Logger(LocationExpert.class);

    private Context mContext;

    // Constructor

    public LocationExpert(Context context) {
        mContext = context;
    }

    public void unsubscribeToLocationUpdates(LocationListener listener) {
        LocationManager locationManager = getLocationManager();
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(listener);

                log.v("removing location update");
            }
        }
    }

    public LocationManager getLocationManager() {
        Object service =  mContext.getSystemService(Context.LOCATION_SERVICE);

        return (service == null)? null : (LocationManager)service;
    }

    public void setLocationSubscriptionStatus(boolean isEnabled, LocationListener listener) throws Exception  {
        if (isEnabled)
            subscribeToLocationUpdates(listener);
        else
            unsubscribeToLocationUpdates(listener);
    }

    public void subscribeToLocationUpdates(LocationListener listener) throws Exception {
        log.v("beginning to subscribe to location updates..");

        LocationManager locationManager = getLocationManager();

        if (locationManager == null)
            throw new Exception("Sorry, it appears that you cannot retrieve locations on this device.");

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            throw new Exception("Sorry, it appears that the application does not have permission to access fine locations.");

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
        catch (Exception e) {
            throw new Exception("Sorry, it appears that you cannot retrieve locations on this device.", e);
        }
    }

}
