package com.example.bequiet.view.home;


import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.bequiet.R;
import com.example.bequiet.model.AppDatabase;
import com.example.bequiet.model.dataclasses.AreaRule;
import com.example.bequiet.model.NoiseType;
import com.example.bequiet.model.dataclasses.Rule;
import com.example.bequiet.model.dataclasses.WlanRule;
import com.example.bequiet.presenter.HomePagePresenter;
import com.example.bequiet.view.edit.SelectAreaFragment;

import org.osmdroid.config.Configuration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RulesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Rule> localDataSet;
    private HashMap<Integer, Fragment> localFrags;
    private HomePagePresenter homePagePresenter;

    public RulesAdapter(List<Rule> dataSet, HomePagePresenter pres) {
        localDataSet = dataSet;
        localFrags = new HashMap<>();
        this.homePagePresenter = pres;
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

    public void clearFragments() {
        for (Iterator<Integer> iterator = localFrags.keySet().iterator(); iterator.hasNext(); ) {
            Integer position = iterator.next();
            localFrags.get(position).onDetach();
            localFrags.get(position).onDestroy();
            iterator.remove();
        }
        Log.i("CLEARED", "Frags: " + localFrags.keySet().size());
    }

    public void setLocalDataSet(List<Rule> dataSet) {
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
        WlanRule wr = null;
        AreaRule ar = null;
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
                localFrags.get(position).onDetach();
                localFrags.get(position).onDestroy();
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

        WlanRule finalWr = wr;
        AreaRule finalAr = ar;
        viewHolder.getRadioButtonSilence().setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (rule instanceof  WlanRule){
                    finalWr.setReactionType(NoiseType.SILENT);
                    this.updateDBWlanRule(viewHolder.getContext(), finalWr);
                } else if (rule instanceof  AreaRule) {
                    finalAr.setReactionType(NoiseType.SILENT);
                    this.updateDBAreaRule(viewHolder.getContext(), finalAr);
                }
            }
        });
        viewHolder.getRadioButtonVibrate().setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (rule instanceof  WlanRule){
                    finalWr.setReactionType(NoiseType.VIBRATE);
                    this.updateDBWlanRule(viewHolder.getContext(), finalWr);
                } else if (rule instanceof  AreaRule) {
                    finalAr.setReactionType(NoiseType.VIBRATE);
                    this.updateDBAreaRule(viewHolder.getContext(), finalAr);
                }
            }
        });
        viewHolder.getRadioButtonNoise().setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (rule instanceof  WlanRule){
                    finalWr.setReactionType(NoiseType.NOISE);
                    this.updateDBWlanRule(viewHolder.getContext(), finalWr);
                } else if (rule instanceof  AreaRule) {
                    finalAr.setReactionType(NoiseType.NOISE);
                    this.updateDBAreaRule(viewHolder.getContext(), finalAr);
                }
            }
        });
    }

    private void updateDBAreaRule(Context context, AreaRule a){
        Thread thread = new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "rules").build();
            db.ruleDAO().updateAreaRule(a);
            Log.i("database", db.ruleDAO().loadAllAreaRules().toString());
            db.close();
        });
        thread.start();
    }

    private void updateDBWlanRule(Context context, WlanRule w){
        Thread thread = new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "rules").build();
            db.ruleDAO().updateWlanRule(w);
            db.close();
        });
        thread.start();
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

