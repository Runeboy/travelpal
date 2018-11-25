package com.rune.travelpal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;

import java.util.UUID;

import rune.dialog.MessageBoxExpert;
import com.rune.travelpal.R;

import com.rune.travelpal.data.remote.RemoteDataGateway;
import com.rune.travelpal.data.dto.response.GetNewClientIdResponse;

/**
 * An activity that allowd the user the setup his/her unique ID
 */
public class SetupClientIdActivity extends AbstractActivity {

    // Constructor

    public SetupClientIdActivity() {
        super(R.layout.activity_setup_client_id, SetupClientIdActivity.class);
    }

    // Event handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initiateAccountRequestOnButtonClick();
        closeActivityOnButtonClick();
    }

    // Methods

    private void closeActivityOnButtonClick() {
        findViewById(R.id.startUsingAccountButton).setOnClickListener(new View.OnClickListener() { public void onClick(View v) {
            startActivity(new Intent(SetupClientIdActivity.this, MainActivity.class));
            SetupClientIdActivity.this.finish();
        }});
    }

    private void initiateAccountRequestOnButtonClick() {
        findViewById(R.id.createNewClientIdButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    tryCreateClientId();
                } catch (JSONException e) {
                    log.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void tryCreateClientId() throws JSONException {
        findViewById(R.id.createNewClientIdButton).setEnabled(false);

        findViewById(R.id.checkNewClientIdRelativeLayout).setVisibility(View.VISIBLE);

        getRemoteDataGateway().requestNewClientId(new RemoteDataGateway.Listener<GetNewClientIdResponse>() {
            public void onResponse(GetNewClientIdResponse response) {
                log.d("New client id: " + response.getClientId());

                handleSetClientId(response.getClientId());
            }

            public void onError(String message, Throwable... innerExceptions) {
                MessageBoxExpert.showMessageFragmentWithOkButton("Error", "Could not reach service, you will get a dummy account", SetupClientIdActivity.this);

                handleSetClientId(UUID.randomUUID().toString());
                //findViewById(R.id.createNewClientIdButton).setEnabled(true);
            }
        });
    }

    private void handleSetClientId(String clientId) {
        getDataGateway().setClientId(clientId);

        findViewById(R.id.inputViewContainer).setVisibility(View.GONE);
        findViewById(R.id.userCreatedViewContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.checkNewClientIdRelativeLayout).setVisibility(View.GONE);
    }

}
