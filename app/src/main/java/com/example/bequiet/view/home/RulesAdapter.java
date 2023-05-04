package com.example.bequiet.view.home;


import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.Rule;
import com.example.bequiet.model.WlanRule;
import com.example.bequiet.view.edit.SelectAreaFragment;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RulesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Rule> localDataSet;
    private HashMap<Integer, Fragment> localFrags;

    public RulesAdapter(List<Rule> dataSet) {
        localDataSet = dataSet;
        localFrags = new HashMap<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // Create a new view, which defines the UI of the list item
        View view = inflater.inflate(R.layout.rule_row_item, viewGroup, false);

        // Add the Fragment to the View using a FragmentManager
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        return new ViewHolder(view, context);
    }

    public void clearFragments(){
        for (Iterator<Integer> iterator = localFrags.keySet().iterator(); iterator.hasNext();) {
            Integer position = iterator.next();
            localFrags.get(position).onDetach();
            localFrags.get(position).onDestroy();
            iterator.remove();
        }
        Log.i("CLEARED", "Frags: " + localFrags.keySet().size());
    }

    public void setLocalDataSet(List<Rule> dataSet){
        this.localDataSet.clear();
        this.localDataSet.addAll(dataSet);
//        this.notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Rule rule = localDataSet.get(position);
        Resources res = viewHolder.getView().getResources();
        viewHolder.setDataPos(position);

        if (rule instanceof WlanRule) {
            WlanRule r = (WlanRule) rule;
            String text = String.format(res.getString(R.string.wlan_rule), r.getRuleName());
            viewHolder.getTextViewRuleTitle().setText(text);
            if (localFrags.get(position) != null){
                Objects.requireNonNull(localFrags.get(position)).onDetach();
                Objects.requireNonNull(localFrags.get(position)).onDestroy();
                localFrags.remove(position);
            }
            WlanRuleFragment wlanRuleFragment = new WlanRuleFragment(r.getWlanName());
            viewHolder.setFrag(wlanRuleFragment);
            localFrags.put(position, wlanRuleFragment);
        } else if (rule instanceof AreaRule) {
            AreaRule r = (AreaRule) rule;
            String text = String.format(res.getString(R.string.area_rule), r.getRuleName());
            viewHolder.getTextViewRuleTitle().setText(text);
            if (localFrags.get(position) != null){
                localFrags.get(position).onDetach();
                localFrags.get(position).onDestroy();
                localFrags.remove(position);
            }
            Configuration.getInstance().load(
                    viewHolder.getView().getContext(),
                    PreferenceManager.getDefaultSharedPreferences(viewHolder.getView().getContext()));
            SelectAreaFragment selectAreaFragment = new SelectAreaFragment(
                    r.getCenterLatitude(),
                    r.getCenterLongitude(),
                    r.getRadius(),
                    r.getZoom() - 1,
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

