package com.rune.travelpal.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.rune.travelpal.R;
import rune.formatting.StringExpert;
import rune.fragment.AbstractFragment;
import com.rune.travelpal.data.dto.Travel;


// Fragment for showing history
public class HistoryFragment extends AbstractFragment {

    // Fields

    private TravelItemAdapter mChatMessageAdapter;

    // Constructor

    public static HistoryFragment newInstance() {
        return  new HistoryFragment();
    }

    public HistoryFragment() {
        super(R.layout.fragment_history, HistoryFragment.class);
    }

    // Overrides

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshHistoryItems();
    }

    // Methods

    private void refreshHistoryItems() {
        List<Travel> history = getDataEnabledActivity().getDataGateway().getHistoryItems();

        mChatMessageAdapter = new TravelItemAdapter(getActivity(), history);
        mChatMessageAdapter.notifyDataSetChanged();

        ((TextView)findViewById(R.id.historyCountTextView)).setText(
                StringExpert.format("{0} travel(s), {1} USD spent", history.size(), getTotalCost(history))
        );
        ((ListView)findViewById(R.id.itemsListView)).setAdapter(mChatMessageAdapter);
    }

    private double getTotalCost(List<Travel> history) {
        double result = 0;
        for (Travel t : history)
            result += t.cost;

        return result;
    }

}
