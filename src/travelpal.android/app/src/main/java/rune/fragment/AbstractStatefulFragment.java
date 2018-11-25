package rune.fragment;

import android.os.Bundle;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;

public abstract class AbstractStatefulFragment<State> extends AbstractFragment {

    // Fields

    private State mState;
    private final Class<State> mStateClass;

    // Constructor

    public AbstractStatefulFragment(int layoutId, Class logClass, Class<State> stateClass) {
        super(layoutId, logClass);

        mStateClass = stateClass;
    }

    private void setStateToEmpty() {
        try {
            Constructor<State> c = mStateClass.getConstructor();
            c.setAccessible(true);
            mState = c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create instance of state class \"" + mStateClass.getSimpleName() + "\": " + e, e);
        }
    }



    // Overrides

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        savedState(outState, mState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            mState = restoreState(savedInstanceState, mStateClass);
        else
            setStateToEmpty();
    }

    // Methods

//    protected abstract State getState();
    protected State getState()
    {
        if (mState == null)
            throw new RuntimeException("Cannot retrieve state before is has been set.");

        return mState;
    }

    private  <T> T restoreState(Bundle instance, Class<T> stateClass)  {
        if (instance == null)
            return null;

        String json  = instance.getString("state");

        log.d("restoring state: " + json);

        return (json == null || json == "")? null : new Gson().fromJson(json, stateClass);
    }

    private void savedState(Bundle instance, Object state) {
        if (state == null) {
            log.d("no state to save");
            return;
        }

        String json = new Gson().toJson(state);

        log.d("saving state: " + json);

        instance.putString("state", json);
    }


}
