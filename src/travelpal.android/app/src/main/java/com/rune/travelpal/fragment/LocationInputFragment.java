package com.rune.travelpal.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Locale;

import rune.dialog.MessageBoxExpert;
import com.rune.travelpal.R;
import rune.dialog.ProgressDialogFragment;
import rune.formatting.StringExpert;
import rune.fragment.AbstractStatefulFragment;
import rune.location.LocationExpert;
import rune.location.StrictlyImprovingLocationListener;
import com.rune.travelpal.data.Broadcasts;
import com.rune.travelpal.data.DataGateway;
import com.rune.travelpal.data.remote.RemoteDataGateway;
import com.rune.travelpal.data.dto.AbstractRemoteDataGateway;
import com.rune.travelpal.data.dto.response.CheckOutResponse;
import com.rune.travelpal.data.dto.response.GetUserStatusResponse;

// An input fragment for location.
public class LocationInputFragment extends AbstractStatefulFragment<LocationInputFragment.State> {

    // Fields

    private LocationListener mLocationListener;
    private LocationExpert mLocationExpert;

    public static class State {
        boolean isCheckInNotCheckOut;
    }

    // Constructor

    public LocationInputFragment() {
        super(R.layout.fragment_location_input, LocationInputFragment.class, State.class);
    }

    // Methods

    private LocationExpert getLocationExpert() {
        if (mLocationExpert == null)
            mLocationExpert = new LocationExpert(getActivity());

        return mLocationExpert;
    }

//    Overrides

    private void listenForCheckInOutChangeBroadcasts() {
        listenForBroadcast(
                Broadcasts.BROADCAST_CHECKIN_STATUS_CHANGE,
                new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        log.d("Broadcast receieved on check-in/out change");

                        popluateLocationFromStorageIfCheckIn();
                    }
                });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveLocationAndCityOnToggleButtonClick();

        getState().isCheckInNotCheckOut = isThisFragmentCheckInNotCheckout();

        setCheckInOutButtonTextAndClickAction(getState().isCheckInNotCheckOut);

        popluateLocationFromStorageIfCheckIn();

        listenForCheckInOutChangeBroadcasts();
    }

    private void setCheckInOutButtonTextAndClickAction(boolean isCheckInNotCheckOut) {
        Button button = ((Button)findViewById(R.id.checkInOutButton));

        button.setText("Check " + (isCheckInNotCheckOut? "in" : "out"));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getLocationSubscribeButton().setChecked(false);
                getLocationExpert().unsubscribeToLocationUpdates(mLocationListener);

                final boolean isCheckIn = getState().isCheckInNotCheckOut;
                new ProgressDialogFragment().initialize("Please wait", "Requesting check-" + (isCheckIn ? "in" : "out") + "..", new ProgressDialogFragment.ProgressAction() {
                    public void invoke(Dialog dialog) {
                        doCheckInOrCheckOut(isCheckIn, dialog);
                    }
                }).show(LocationInputFragment.this);

            }
        });
    }

    private void popluateLocationFromStorageIfCheckIn() {
        if (getState().isCheckInNotCheckOut) {
            String checkInLocation = getDataEnabledActivity().getDataGateway().getCheckInLocation();
//            if (checkInLocation != null && checkInLocation != null)
                getLocationEditText().setText(checkInLocation);
        }
    }

    private void doCheckInOrCheckOut(final boolean isCheckIn, final Dialog dialog) {
        RemoteDataGateway service = getDataEnabledActivity().getRemoteDataGateway();

        String inputLocation = getLocationEditText().getText().toString();
        log.d("Input location: " + inputLocation);
        if (inputLocation.trim().isEmpty()) {
            dialog.dismiss();
            MessageBoxExpert.showMessageFragmentWithOkButton("Error", "Location cannot be the empty string.", getActivity());
            return;
        }

        String clientId = getDataEnabledActivity().getDataGateway().getClientId();
        final DataGateway data = getDataEnabledActivity().getDataGateway();
        if (isCheckIn)
            service.checkIn(
                    clientId,
                    inputLocation,
                    new AbstractRemoteDataGateway.Listener<GetUserStatusResponse>() {
                        public void onResponse(GetUserStatusResponse response) {
                            dialog.dismiss();
                            MessageBoxExpert.showMessageFragmentWithOkButton("Checked in", "You have checked in at \"" + response.getCheckInCity() + "\".", getActivity());

                            data.setUserCheckedIn(true);
                            data.setCheckInLocation(response.getCheckInCity());

                            getActivity().sendBroadcast(new Intent(Broadcasts.BROADCAST_CHECKIN_STATUS_CHANGE));
                        }

                        public void onError(String message, Throwable... innerExceptions) {
                            dialog.dismiss();
                            MessageBoxExpert.showMessageFragmentWithOkButton("Error", "Failed to request check-in.", getActivity());
                        }
                    }
            );
        else
            service.checkOut(
                    clientId,
                    inputLocation,
                    new AbstractRemoteDataGateway.Listener<CheckOutResponse>() {
                        public void onResponse(CheckOutResponse response) {
                            dialog.dismiss();

                            String from = response.getCheckInCity();
                            String to = response.getCheckOutCity();
                            double cost = response.getCost();

                            boolean isTravelSaved = getDataEnabledActivity().getDataGateway().addHistoryItem(from, to, cost);
                            if (!isTravelSaved)
                                MessageBoxExpert.showMessageFragmentWithOkButton("Error", "Failed to saved travel in (local) history.", getActivity());

                            MessageBoxExpert.showMessageFragmentWithOkButton(
                                    "Checked out",
                                    StringExpert.format("Your fare cost from \"{0}\" to \"{1}\": {2}", from, to, cost),
                                    getActivity()
                            );

                            data.setUserCheckedIn(false);
                            data.setCheckInLocation(null);

                            getLocationEditText().setText("");

                            getActivity().sendBroadcast(new Intent(Broadcasts.BROADCAST_CHECKIN_STATUS_CHANGE));
                        }
                        public void onError(String message, Throwable... innerExceptions) {
                            dialog.dismiss();
                            MessageBoxExpert.showMessageFragmentWithOkButton("Error", "Failed to request check-out.", getActivity());
                        }
                    }
            );
    }

    private boolean isThisFragmentCheckInNotCheckout() {
        return getAttributeValueAsBoolean(R.styleable.LocationInputFragment, R.styleable.LocationInputFragment_isCheckInNotCheckOut);
    }

    private void retrieveLocationAndCityOnToggleButtonClick() {
        final ToggleButton locationSubscribeButton = getLocationSubscribeButton();

        mLocationListener = new StrictlyImprovingLocationListener(new StrictlyImprovingLocationListener.Listener() {
            public void onBetterLocation(Location location) {
                log.d("Location received");

                Activity activity = getActivity();
                if (activity == null)
                    return;

                try {
                    String city = getCityNameByLocation(location, activity);
                    getLocationEditText().setText(city);
                } catch (Exception e) {
                    MessageBoxExpert.showMessageFragmentWithOkButton("Error", e.toString(), activity);
                }
            }
        });

        locationSubscribeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View toggleButtonView) {
                try {
                    getLocationExpert().setLocationSubscriptionStatus(locationSubscribeButton.isChecked(), mLocationListener);

                } catch (Exception e) {
                    MessageBoxExpert.showMessageFragmentWithOkButton("Location error", e.toString(), getActivity());
                    if (locationSubscribeButton.isChecked())
                        locationSubscribeButton.setChecked(false);
                }

                getLocationEditText().setEnabled(!locationSubscribeButton.isChecked());
            }
        });
    }

    private ToggleButton getLocationSubscribeButton() {
        return (ToggleButton)findViewById(R.id.getLocationButton);
    }

    private EditText getLocationEditText() {
        return (EditText )findViewById(R.id.locationEditText);
    }

    private static String getCityNameByLocation(Location location, Context context) throws Exception {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = addressList.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = addressList.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            return builder.toString();
        } catch (Exception e) {
            throw new Exception("Failed to convert location to a city name.", e);
        }
    }


}
