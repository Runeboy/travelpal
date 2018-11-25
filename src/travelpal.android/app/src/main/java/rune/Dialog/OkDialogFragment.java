package rune.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.gson.Gson;

// A fragment dialog with an ok button
public class OkDialogFragment extends DialogFragment {

    // Fields

    private static final String STATE_KEY = OkDialogFragment.class.getName();

    DialogInterface.OnClickListener onOkClick;

    private State mState = new State();
    private DialogInterface.OnClickListener mOnClick;

    private class State { String mTitle, mMessage; };

    // Overrides

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_KEY, new Gson().toJson(mState));

        super.onSaveInstanceState(outState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String json = savedInstanceState.getString(STATE_KEY);
            if (json != null)
                mState = new Gson().fromJson(json, State.class);
        }

        return MessageBoxExpert.makeDialogwithOkButton(mState.mTitle, mState.mMessage, mOnClick, getActivity());
    }

    // Methods

    public OkDialogFragment initialize(String title, String message) {
        mState.mTitle = title;
        mState.mMessage = message;

        return this;
    }

}
