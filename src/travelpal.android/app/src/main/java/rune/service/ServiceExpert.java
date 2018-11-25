package rune.service;

import android.app.Service;
import android.content.Context;
import android.location.LocationManager;

// Helper class to get android services
public class ServiceExpert {

    // Fields

    private Context mContext;

    // Constructor

    public ServiceExpert(Context context) {
        mContext = context;
    }

    // Methods

    public LocationManager getLocationManager() {
        return (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
    }
}
