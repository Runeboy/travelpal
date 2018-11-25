package com.rune.travelpal.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import com.rune.travelpal.R;
import rune.logging.Logger;
import com.rune.travelpal.data.dto.Travel;

// An adapter for a list view
public class TravelItemAdapter extends ArrayAdapter<Travel> {

    // Fields

    private static final int mResource = R.layout.list_element;

    private Logger log = new Logger(TravelItemAdapter.class);

    // Constructor

    public TravelItemAdapter(Context context, List<Travel> items) {
        super(context, mResource, items);
    }

    // Overrides

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout layout;

        Travel chatMessage = getItem(position);

        layout = (convertView == null)? inflateNew() : (FrameLayout) convertView;

        initListItemUI(layout, chatMessage);

        return layout;
    }

    private void initListItemUI(FrameLayout layout, Travel travel) {
        log.d("Initializing a list item");

        ((TextView) layout.findViewById(R.id.listItemFromTextView)).setText(travel.from);
        ((TextView) layout.findViewById(R.id.listItemToTextView)).setText(travel.to);
        ((TextView) layout.findViewById(R.id.costTextView)).setText(travel.cost + "");
    }

    // Methods

    @NonNull
    private FrameLayout inflateNew() {
        log.d("Inflating new list item");

        FrameLayout layout;
        layout = new FrameLayout(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(mResource, layout, true);
        return layout;
    }


}
