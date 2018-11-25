package rune.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import rune.logging.Logger;

import com.rune.travelpal.data.DataEnabled;

/**
 * Fragment base class for shared functionality
 */
public abstract class AbstractFragment extends Fragment {

    // Fields

    protected static Logger log;

    private int mLayoutId;

    private AttributeSet mAttrs;

    private Map<String, BroadcastReceiver> mBroadcastReceivers;

    // Constructors

    public AbstractFragment(int layoutId, Class logClass) {
        mLayoutId = layoutId;
        log = new Logger(logClass);
    }

    // Event handlers

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(mLayoutId, container, false);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        mAttrs = attrs;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

        mAttrs = attrs;
    }


    @Override
    public void onResume() {
        super.onResume();

        registerBroadcastReceivers();
    }

    private void registerBroadcastReceivers() {
        if (mBroadcastReceivers != null)
            for (String key : mBroadcastReceivers.keySet())
                getActivity().registerReceiver(mBroadcastReceivers.get(key), new IntentFilter(key));
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterBroadcastReceivers();
    }

    private void unregisterBroadcastReceivers() {
        if (mBroadcastReceivers != null)
            for (String key : mBroadcastReceivers.keySet())
                getActivity().unregisterReceiver(mBroadcastReceivers.get(key));
    }

    // Methods

    protected View findViewById(int id) {
        if (getView() == null)
            throw new RuntimeException("Fragment view has not been set yet");

        return getView().findViewById(id);
    }

    protected boolean getAttributeValueAsBoolean(int[] idSet, int id) {
        if (mAttrs == null)
            throw new RuntimeException("Attribute set not initialized yet.");

        TypedArray a = getActivity().obtainStyledAttributes(mAttrs, idSet);

        boolean result = a.getBoolean(id, false);

//        log.d("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + result );

        a.recycle();

        return result;
    }

    protected DataEnabled getDataEnabledActivity() {
        Activity activity = getActivity();

        return (activity == null)? null : (DataEnabled)getActivity();
    }

    protected void listenForBroadcast(String key, BroadcastReceiver receiver) {
        if (mBroadcastReceivers == null)
            mBroadcastReceivers = new HashMap<String, BroadcastReceiver>();

        mBroadcastReceivers.put(key, receiver);
    }


}
