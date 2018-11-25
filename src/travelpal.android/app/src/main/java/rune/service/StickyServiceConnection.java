package rune.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import rune.logging.Logger;

// Helper class for connection to the sticky-service service
public class StickyServiceConnection<StickyServiceType extends StickyService> implements ServiceConnection {

    // Fields

    private Logger log = new Logger(StickyServiceConnection.class);

    protected StickyServiceType mService;

    private Class<StickyServiceType> mServiceClass;

    public interface OnServiceConnect {
        void onConnect();
    }

    private OnServiceConnect mOnConnectListener;

    // Constructor

    public StickyServiceConnection(Class<StickyServiceType> serviceClass) {
        log.d("construct");

        mServiceClass = serviceClass;
    }

    // Interface implementations

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        log.v("onServiceConnected");

        mService = (StickyServiceType)((StickyService.LocalBinder)binder).getService();

        if (mOnConnectListener != null)
            mOnConnectListener.onConnect();
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
        log.v("onServiceDisconnected");

        mService = null;
    }

    // Methods

    public StickyServiceType getService() {
        return mService;
    }

    public void setOnConnectListener(OnServiceConnect listener) {
        mOnConnectListener = listener;
    }

    public boolean bindService(Context context, OnServiceConnect listener) {
        setOnConnectListener(listener);

        boolean isBound = context.bindService(  // service is now running at this point, we can bind
                new Intent(context, mServiceClass), // LocationService.class),
                this,
                0
                //Context.BIND_AUTO_CREATE
        );

        return isBound;
    }

//    public StickyService getService() {
//        return mService;
//    }
}
