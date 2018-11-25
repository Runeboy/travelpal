package com.rune.travelpal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import rune.logging.Logger;
import com.rune.travelpal.data.DataEnabled;
import com.rune.travelpal.data.DataGateway;
import com.rune.travelpal.data.remote.RemoteDataGateway;

// Base class for an activity, provides app-specific common functionality
 public abstract class AbstractActivity extends Activity implements DataEnabled {

    // Fields

    protected Logger log;

    private DataGateway mDataGateway;
    private RemoteDataGateway mRemoteDataGateway;

    private int mLayoutId;

    // Constructor

    public AbstractActivity(int layoutId, Class logClass) {
        log = new Logger(logClass);

        mLayoutId = layoutId;
    }


    // Event handlers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mLayoutId);

    }

    // Methods

    // Allows calling finish in XML layout
    public void finish(View v) {
        super.finish();
    }

    public DataGateway getDataGateway() {
        if (mDataGateway == null)
            mDataGateway = new DataGateway(this);

        return mDataGateway;
    }

    public RemoteDataGateway getRemoteDataGateway() {
        if (mRemoteDataGateway == null)
            mRemoteDataGateway = new RemoteDataGateway(getDataGateway().getServiceUrl(), this);

        return mRemoteDataGateway;
    }

}
