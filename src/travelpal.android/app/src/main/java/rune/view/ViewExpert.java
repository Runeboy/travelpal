package rune.view;

import android.view.View;
import android.view.ViewGroup;


// Helper class for view logic.
public class ViewExpert {

    public static void setViewHierarchyEnabled(View view, boolean isEnabled) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup)view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child  = parent.getChildAt(i);
                setViewHierarchyEnabled(child, isEnabled);
            }
        }
        else
            view.setEnabled(isEnabled);

    }

}
