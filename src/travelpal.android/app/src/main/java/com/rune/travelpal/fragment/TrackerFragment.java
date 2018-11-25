package com.rune.travelpal.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import rune.logging.Logger;
import rune.service.StickyServiceConnection;
import com.rune.travelpal.R;
import com.rune.travelpal.data.Broadcasts;
import com.rune.travelpal.service.LocationService;

public class TrackerFragment extends TabFragment<TrackerFragment.State> {

    // Fields

    private static final Logger log = new Logger(TrackerFragment.class);
    private static final float DEFAULT_ZOOM_LEVEL = 17;

    public static class State {
        boolean isMapZoomedYet = false;
    }

    protected StickyServiceConnection mServiceConnection;

    private LocationService mLocationService;

    protected GoogleMap mGoogleMap;

    // Constructors

    public static TrackerFragment newInstance() {
        TrackerFragment fragment = new TrackerFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    public TrackerFragment() {
        super(R.layout.fragment_tracker, TrackerFragment.class, State.class);
    }

    // Abstract implementations

    @Override
    public String getTabName() {
        return "Tracking";
    }

    // Event handlers

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        handleServiceToggleButtonClick();

        listenForBroadcast(Broadcasts.BROADCAST_TRACKING_DATA_CHANGED, new BroadcastReceiver() {public void onReceive(Context context, Intent intent) {
            if (mLocationService == null)
                return;

            drawRoute(mLocationService.getTrackedLocations());
        }});
    }

    private void handleServiceToggleButtonClick() {
        final ToggleButton serviceToggleButton = getTrackingToggleButton();
        serviceToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View buttonView) {
                Context context = getActivity();

                boolean isTrackingToBeEnabed = serviceToggleButton.isChecked();
                setTrackingToggleButtonState(isTrackingToBeEnabed);
                if (isTrackingToBeEnabed)
                    LocationService.ensureIsStarted(context);
                else {
                    LocationService.stop(context);
                }
            }
        });
    }

    private void setTrackingToggleButtonState(boolean isOn) {
        getTrackingToggleButton().setText(isOn ? "On" : "Off");
        getTrackingToggleButton().setChecked(isOn);
    }

    private ToggleButton getTrackingToggleButton() {
        return (ToggleButton)findViewById(R.id.toggleTrackingButton);
    }

    @Override
    public void onResume() {
        super.onResume();

        log.v("onResume called");

        tryConnectToService(getActivity()); //onResume is called after onCreate, so we may fetch the activity
    }

    // Methods

    @Override
    public void onPause() {
        super.onPause();

        if (mServiceConnection != null) {
            getActivity().unbindService(mServiceConnection); // onPause is called before onDetach, so we can get the activity
            mServiceConnection = null;
        }
    }

    // Helper function for connecting to AirWavesService.
    private void tryConnectToService(Context context) {
        // Now connect to it
        mServiceConnection = new StickyServiceConnection<>(LocationService.class);//StickyService.class);
        boolean isBound = mServiceConnection.bindService(
                context,
                new StickyServiceConnection.OnServiceConnect() { public void onConnect() {
                    mLocationService = (LocationService) mServiceConnection.getService();

                    log.d("Location service uptime seconds: " + mLocationService.getUptimeSeconds());
                    log.d("Location service start time: " +mLocationService.getStartTime());

                    setTrackingToggleButtonState(true);
                }}
        );

        if (isBound) {
            log.d("Is now bound with location service");
        } else {
            log.e("Unable to bind with location service");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocationService.stop(getActivity()); // onDestroy is called before onDetach
    }

    // Methods

    private boolean isLocationServiceAvailable() {
        return mServiceConnection != null;
    }

    private void drawRoute(List<Location> locations) {
        if (locations.size() <= 0)
            return;

        LatLng[] latLngs = locationsToLatLngs(locations);
        
        mGoogleMap.addPolyline((new PolylineOptions())
                .add(latLngs).width(5).color(Color.BLUE)
                .geodesic(true));

        if (!getState().isMapZoomedYet) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[0], DEFAULT_ZOOM_LEVEL));
            getState().isMapZoomedYet = true;
        }
    }

    private LatLng[] locationsToLatLngs(List<Location> locations) {
        LatLng[] route = new LatLng[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            route[i] = new LatLng(location.getLatitude(), location.getLongitude());
        }

        return route;
    }
}
