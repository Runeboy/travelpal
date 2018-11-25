package rune.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import rune.logging.Logger;

// A sticky service, encapsulates service syntax
public class StickyService extends Service {

    // Fields

    private static Logger log = new Logger(StickyService.class);

    private final LocalBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public StickyService getService() {
            return StickyService.this;
        }
    }

    // Overrides

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return android.app.Service.START_STICKY;
    }


}
