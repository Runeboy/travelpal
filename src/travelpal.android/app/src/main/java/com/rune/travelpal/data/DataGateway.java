package com.rune.travelpal.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import rune.data.AbstractDataGateway;
import rune.logging.Logger;

import com.rune.travelpal.R;
import com.rune.travelpal.data.dto.Travel;


/**
 * Serves as an interface to the app data. Implements persistent storage using appropriate methods, depending on the nature of the data.
 */
public class DataGateway extends AbstractDataGateway<DatabaseHelper> {

    // Fields

    private Logger log = new Logger(DataGateway.class);


    private DatabaseHelper mDb; // a database system will be used for large (= slow) data

    private SharedPreferences mSharedPrefs; // shared prefs will be used for small (= speedy) data values

    private enum PrefKey {
        ClientId,
        CheckInLocation, IsCheckedIn
    }

    // Constructor

    public DataGateway(Context context) {
        super(context);
    }

    // Interface implementation

    public String getServiceUrl() {
        return mContext.getResources().getString(R.string.service_url);
    }

    public boolean isUserCheckedIn() {
        return getSharedPref(PrefKey.IsCheckedIn, Boolean.class, false);
    }

    public void setUserCheckedIn(boolean value) {
//        log.d("Setting user checked in: " + value);
        setSharedPref(PrefKey.IsCheckedIn, value);
    }

    public void setCheckInLocation(String value) {
//        log.d("Setting check in location: " + value);
        setSharedPref(PrefKey.CheckInLocation, value);
    }

    public boolean isClientIdSet() {
        return isSharedPrefSet(PrefKey.ClientId);
    }

    public void setClientId(String id) {
        setSharedPref(PrefKey.ClientId, id);
    }

    public String getClientId() {
        return getSharedPref(PrefKey.ClientId);
    }

    public String getCheckInLocation() {
        return getSharedPref(PrefKey.CheckInLocation);
    }

    public List<Travel> getHistoryItems() {
         return getDb().getTravels();
    }

    public boolean addHistoryItem(String from, String to, double cost) {
        return getDb().createTravel(from, to, (int)cost);
    }

    // Helper methods

    @Override
    protected DatabaseHelper createDbHelper() {
        return new DatabaseHelper(mContext);
    }

    private void setSharedPref(PrefKey key, Object value) {
        setSharedPref(key.toString(), value);
    }

    public String getUsername() {
        return getSharedPref(mContext.getResources().getString(R.string.prefs_username));
    }


    private <T> T getSharedPref(PrefKey key, Class<T> valueClass, T def) {
        return getSharedPref(key.toString(), valueClass, def);
    }

    private String getSharedPref(PrefKey key) {
        String value =  getSharedPref(key.toString());

        log.d("Returning shared pref value ({0}:{1}), is null: {2}", key, value, value == null);

        return value;
    }

    private boolean isSharedPrefSet(PrefKey key) {
        return isSharedPrefSet(key.toString());
    }

}
