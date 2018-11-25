package com.rune.travelpal.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rune.travelpal.R;
import rune.dialog.MessageBoxExpert;
import rune.dialog.ProgressDialogFragment;

import com.rune.travelpal.data.Broadcasts;
import com.rune.travelpal.data.CheckInStatusChangeListener;
import com.rune.travelpal.data.DataEnabled;
import com.rune.travelpal.data.DataGateway;
import com.rune.travelpal.data.dto.AbstractRemoteDataGateway;
import com.rune.travelpal.data.dto.response.GetUserStatusResponse;
import rune.view.ViewExpert;

// The main fragment that is seen when the app starts (and the user has already setup his travelpal-id)
public class MainFragment extends TabFragment<MainFragment.State> implements CheckInStatusChangeListener {

    // Fields

    public static class State {
        boolean isTicketStatusToBeRequestedOnStartup = true;
    }

    // Constructors

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
        super(R.layout.fragment_main, MainFragment.class, State.class);
    }

    // Interface implementations

    public void checkInStatusChanged() {
        setCheckInOutViewsEnabledBasedOnStatus();
    }

    // Event handlers

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCheckInOutViewsEnabledBasedOnStatus();

        if (savedInstanceState == null && getState().isTicketStatusToBeRequestedOnStartup) {
            showProgressDialogThatQueriesForTicketStatus();
        }

        listenForCheckInOutChangeBroadcasts();
        listenForReloadRequestBroadcasts();
    }

    // Methods


    private void setCheckInOutViewsEnabledBasedOnStatus() {
        boolean isCheckedIn = getDataEnabledActivity().getDataGateway().isUserCheckedIn();

        ViewExpert.setViewHierarchyEnabled(findViewById(R.id.checkInFragment), !isCheckedIn);
        ViewExpert.setViewHierarchyEnabled(findViewById(R.id.checkOutFragment), isCheckedIn);
    }

    private void listenForReloadRequestBroadcasts() {
        listenForBroadcast(
                Broadcasts.BROADCAST_RELOAD_TICKET_STATUS,
                new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        showProgressDialogThatQueriesForTicketStatus();
                    }
                });
    }

    private void listenForCheckInOutChangeBroadcasts() {
        listenForBroadcast(
                Broadcasts.BROADCAST_CHECKIN_STATUS_CHANGE,
                new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        log.d("Broadcast receieved on check-in/out change");
                        setCheckInOutViewsEnabledBasedOnStatus();
                    }
                });
    }

    private void showProgressDialogThatQueriesForTicketStatus() {
        new ProgressDialogFragment().initialize("Please wait", "Querying for ticket status..", new ProgressDialogFragment.ProgressAction() { public void invoke(Dialog dialog) {
            queryForTicketStatusAndCloseDialog(dialog);
        }}).show(this);
    }

    private void queryForTicketStatusAndCloseDialog(final Dialog dialog) {
        DataEnabled activity = getDataEnabledActivity();
        final DataGateway data = activity.getDataGateway();

        activity.getRemoteDataGateway().getClientStatus(
                data.getClientId(),
                new AbstractRemoteDataGateway.Listener<GetUserStatusResponse>() {
                    public void onResponse(GetUserStatusResponse response) {
                        data.setUserCheckedIn(response.getIsCheckedIn());

                        dialog.dismiss();

                        MainFragment.this.setCheckInOutViewsEnabledBasedOnStatus();

                        getState().isTicketStatusToBeRequestedOnStartup = false;
                    }
                    public void onError(String message, Throwable... innerExceptions) {
                        dialog.dismiss();

                        MessageBoxExpert.showMessageFragmentWithOkButton("Error", "Could not retrieve status.", getActivity());
                    }
                }
        );
    }


    // Abstract implementations

    @Override
    public String getTabName() {
        return "Itinerary";
    }


}
