package rune.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

// A location listener that emits updates for strictly improving locations
public class StrictlyImprovingLocationListener implements LocationListener {

    // Fields

    private Location mLastLocation;

    public interface Listener {
        void onBetterLocation(Location location);
    }

    private List<Listener> mListeners = new ArrayList<>();

    // Constructors

    public StrictlyImprovingLocationListener() {}

    public StrictlyImprovingLocationListener(Listener callback) {
        addListener(callback);
    }

    // Interface implementations

    @Override
    public void onLocationChanged(Location location) {
        long newTime = location.getTime();
        boolean isBetter = (
            (mLastLocation == null) ||
            location.getAccuracy() < (mLastLocation.getAccuracy() + (newTime - mLastLocation.getTime()))
            );

        if (isBetter) {
            mLastLocation = location;
            notifyListeners();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    // Methods

    public void addListener(Listener listener) {
        mListeners.add(listener);

        if (mLastLocation  != null && listener != null)
            listener.onBetterLocation(mLastLocation );

    }

    private void notifyListeners() {
        for(Listener listener : mListeners)
            if (listener != null)
                listener.onBetterLocation(mLastLocation);
    }

}
