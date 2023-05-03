package com.example.bequiet.view.home;


import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.Rule;
import com.example.bequiet.model.WlanRule;
import com.example.bequiet.view.edit.SelectAreaFragment;

import org.osmdroid.config.Configuration;

import java.util.List;

public class RulesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Rule> localDataSet;
    private final FragmentManager manager;

    public RulesAdapter(List<Rule> dataSet, FragmentManager manager) {
        localDataSet = dataSet;
        this.manager = manager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rule_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Rule rule = localDataSet.get(position);
        Resources res = viewHolder.getView().getResources();

        if (rule instanceof WlanRule) {
            WlanRule r = (WlanRule) rule;
            String text = String.format(res.getString(R.string.wlan_rule), r.getRuleName());
            viewHolder.getTextViewRuleTitle().setText(text);
            WlanRuleFragment wlanRuleFragment = WlanRuleFragment.newInstance(r.getWlanName());
            manager.beginTransaction()
                    .replace(viewHolder.getFragmentRule().getId(), wlanRuleFragment)
                    .commit();
        } else if (rule instanceof AreaRule) {
            AreaRule r = (AreaRule) rule;
            String text = String.format(res.getString(R.string.area_rule), r.getRuleName());
            viewHolder.getTextViewRuleTitle().setText(text);
            Configuration.getInstance().load(
                    viewHolder.getView().getContext(),
                    PreferenceManager.getDefaultSharedPreferences(viewHolder.getView().getContext()));
            SelectAreaFragment selectAreaFragment = SelectAreaFragment.newInstance(
                    r.getCenterLatitude(),
                    r.getCenterLongitude(),
                    19,
                    true);
            manager.beginTransaction()
                    .replace(viewHolder.getFragmentRule().getId(), selectAreaFragment)
                    .commit();
        }

        String startHour = getLeadingZeroString(rule.getStartHour());
        String startMin = getLeadingZeroString(rule.getStartMinute());
        String startString = String.format(res.getString(R.string.time_string), startHour, startMin);
        viewHolder.getTextViewStart().setText(startString);

        String endHour = getLeadingZeroString(rule.getEndHour());
        String endMin = getLeadingZeroString(rule.getEndMinute());
        String endString = String.format(res.getString(R.string.time_string), endHour, endMin);
        viewHolder.getTextViewEnd().setText(endString);

        switch (rule.getReactionType()) {
            case SILENT:
                viewHolder.getRadioButtonSilence().setChecked(true);
                break;
            case VIBRATE:
                viewHolder.getRadioButtonVibrate().setChecked(true);
                break;
            case NOISE:
                viewHolder.getRadioButtonNoise().setChecked(true);
                break;
        }
    }

    private String getLeadingZeroString(int time) {
        if (time < 10) {
            return "0" + time;
        } else
            return String.valueOf(time);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

