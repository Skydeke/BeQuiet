package com.example.bequiet.view.home;


import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.model.database.Database;
import com.example.bequiet.model.dataclasses.AreaRule;
import com.example.bequiet.model.dataclasses.NoiseType;
import com.example.bequiet.model.dataclasses.Rule;
import com.example.bequiet.model.dataclasses.WlanRule;
import com.example.bequiet.view.fragments.SelectAreaFragment;
import com.example.bequiet.view.fragments.WlanRuleFragment;

import org.osmdroid.config.Configuration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RulesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Rule> localDataSet;
    private final HashMap<Integer, Fragment> localFrags;

    public RulesAdapter(List<Rule> dataSet) {
        localDataSet = dataSet;
        localFrags = new HashMap<>();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // Create a new view, which defines the UI of the list item
        View view = inflater.inflate(R.layout.rule_row_item, viewGroup, false);
        return new ViewHolder(view, context);
    }

    public void clearFragments() {
        // We need to manually manage Fragments because we want to display them inside RecyclerView
        for (Iterator<Integer> iterator = localFrags.keySet().iterator(); iterator.hasNext(); ) {
            Integer position = iterator.next();
            Objects.requireNonNull(localFrags.get(position)).onDetach();
            Objects.requireNonNull(localFrags.get(position)).onDestroy();
            iterator.remove();
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Rule rule = localDataSet.get(position);
        WlanRule wr;
        AreaRule ar;
        Resources res = viewHolder.getView().getResources();
        viewHolder.setDataPos(position);

        if (rule instanceof WlanRule) {
            wr = (WlanRule) rule;
            String text = String.format(res.getString(R.string.wlan_rule), wr.getRuleName());
            viewHolder.getTextViewRuleTitle().setText(text);
            if (localFrags.get(position) != null) {
                Objects.requireNonNull(localFrags.get(position)).onDetach();
                Objects.requireNonNull(localFrags.get(position)).onDestroy();
                localFrags.remove(position);
            }
            WlanRuleFragment wlanRuleFragment = new WlanRuleFragment(wr.getWlanName());
            viewHolder.setFrag(wlanRuleFragment);
            localFrags.put(position, wlanRuleFragment);
        } else if (rule instanceof AreaRule) {
            ar = (AreaRule) rule;
            String text = String.format(res.getString(R.string.area_rule), ar.getRuleName());
            viewHolder.getTextViewRuleTitle().setText(text);
            if (localFrags.get(position) != null) {
                Objects.requireNonNull(localFrags.get(position)).onDetach();
                Objects.requireNonNull(localFrags.get(position)).onDestroy();
                localFrags.remove(position);
            }
            Configuration.getInstance().load(
                    viewHolder.getView().getContext(),
                    PreferenceManager.getDefaultSharedPreferences(viewHolder.getView().getContext()));
            SelectAreaFragment selectAreaFragment = new SelectAreaFragment(
                    ar.getCenterLatitude(),
                    ar.getCenterLongitude(),
                    ar.getRadius(),
                    ar.getZoom() - 1,
                    true);
            viewHolder.setFrag(selectAreaFragment);
            localFrags.put(position, selectAreaFragment);
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

        viewHolder.getRadioButtonSilence().setOnCheckedChangeListener((compoundButton, b) -> updateNoiseTypeTo(rule, NoiseType.SILENT, b, viewHolder.getContext()));
        viewHolder.getRadioButtonVibrate().setOnCheckedChangeListener((compoundButton, b) -> updateNoiseTypeTo(rule, NoiseType.VIBRATE, b, viewHolder.getContext()));
        viewHolder.getRadioButtonNoise().setOnCheckedChangeListener((compoundButton, b) -> updateNoiseTypeTo(rule, NoiseType.NOISE, b, viewHolder.getContext()));
    }

    private void updateNoiseTypeTo(Rule rule, NoiseType noiseType, boolean checked, Context context){
        if (checked) {
            Database db = new Database(context);
            if (rule instanceof  WlanRule){
                WlanRule finalWr = (WlanRule) rule;
                finalWr.setReactionType(noiseType);
                db.updateDBWlanRule(finalWr);
            } else if (rule instanceof  AreaRule) {
                AreaRule finalAr = (AreaRule) rule;
                finalAr.setReactionType(noiseType);
                db.updateDBAreaRule(finalAr);
            }
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

