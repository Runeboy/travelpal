package rune.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import rune.logging.Logger;

public abstract class AbstractDataGateway<T extends SQLiteOpenHelper> {

    // Fields

    private Logger log = new Logger(AbstractDataGateway.class);


    private T mDb; // a database system will be used for large (= slow) data

    private SharedPreferences mSharedPrefs; // shared prefs will be used for small (= speedy) data values

    protected Context mContext;

    // Constructor

    public AbstractDataGateway(Context context)
    {
        mContext = context;
    }

    // Interface implementation

    protected T getDb() {
        if (mDb == null)
            mDb = createDbHelper();

        return mDb;
    }

    protected abstract T createDbHelper();

    protected void setSharedPref(String key, Object value) {
        String jsonValue = (value instanceof String)? (String)value : new Gson().toJson(value);

        log.d("Writing shared pref value ({0}:{1})", key, jsonValue);

        if (value == null)
            getSharedPrefs().edit().remove(key).commit();
        else
            getSharedPrefs().edit().putString(key, jsonValue).commit();
    }

    private SharedPreferences getSharedPrefs() {
        if (mSharedPrefs == null)
            mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        return mSharedPrefs;
    }

    protected  <T> T getSharedPref(String key, Class<T> valueClass, T def) {
        String json = getSharedPref(key);
        return json == null?
                def :
                (T)new Gson().fromJson(json, valueClass);
    }

    protected  String getSharedPref(String key) {
        return getSharedPrefs().getString(key, null);
    }

    protected  boolean isSharedPrefSet(String key) {
        return getSharedPrefs().contains(key);
    }


}
