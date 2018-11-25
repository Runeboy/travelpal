package rune.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;

import com.google.gson.Gson;

import com.rune.travelpal.R;

// A dialog fragment for displaying an action's progress.
public class ProgressDialogFragment extends DialogFragment {

    // Fields

    private static final String STATE_KEY = ProgressDialogFragment.class.getName();
    private static final int ACTION_DELAY_SECONDS = 3;

    private State mState = new State();

    private ProgressAction mAction;

    private class State { String mTitle, mMessage; };

    private static int mNextDialogIndex = 0;

    public interface ProgressAction { void invoke(Dialog dialog); };

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
            if (json != null)//                .setPositiveButton("OK", null)
                mState = new Gson().fromJson(json, State.class);
        }

        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(mState.mTitle)
                .setMessage(mState.mMessage)
                .setCancelable(false)
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_progress, null))
                .create();

        new Thread(new Runnable() { public void run() {
            if (dialog != null && mAction != null) {
                trySleep(ACTION_DELAY_SECONDS);
                mAction.invoke(dialog);
            }
        }

            private void trySleep(int seconds) {
                try {
                    Thread.sleep(1000 * seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return dialog;
    }

    // Methods

    public ProgressDialogFragment initialize(String title, String message, final ProgressAction action) {
        mState.mTitle = title;
        mState.mMessage = message;
        mAction = action;

        return this;
    }

    public void show(Fragment fragment) {
        super.show(fragment.getFragmentManager(), ProgressDialogFragment.class.getSimpleName() + "_" + mNextDialogIndex++);
    }


}
