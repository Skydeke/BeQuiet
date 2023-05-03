package com.example.bequiet.view;


import static com.example.bequiet.model.NoiseType.SILENT;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.Rule;
import com.example.bequiet.model.WlanRule;

import java.util.List;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.ViewHolder> {

    private final List<Rule> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewRuleTitle;
        private final TextView textViewStart;
        private final TextView textViewEnd;

        private final RadioButton radioButtonSilence;
        private final RadioButton radioButtonVibrate;
        private final RadioButton radioButtonNoise;
        private final FragmentContainerView fragmentRule;
        private final View view;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            this.view = view;

            textViewRuleTitle = (TextView) view.findViewById(R.id.textViewRuleTitle);
            textViewStart = (TextView) view.findViewById(R.id.textViewStartTime);
            textViewEnd = (TextView) view.findViewById(R.id.textViewEndTime);
            radioButtonSilence = (RadioButton) view.findViewById(R.id.radioButtonSilence);
            radioButtonVibrate = (RadioButton) view.findViewById(R.id.radioButtonVibrate);
            radioButtonNoise = (RadioButton) view.findViewById(R.id.radioButtonFullVolume);
            fragmentRule = (FragmentContainerView) view.findViewById(R.id.fragmentRule);
        }

        public View getView() {
            return view;
        }

        public FragmentContainerView getFragmentRule() {
            return fragmentRule;
        }

        public RadioButton getRadioButtonSilence() {
            return radioButtonSilence;
        }

        public RadioButton getRadioButtonVibrate() {
            return radioButtonVibrate;
        }

        public RadioButton getRadioButtonNoise() {
            return radioButtonNoise;
        }

        public TextView getTextViewRuleTitle() {
            return textViewRuleTitle;
        }

        public TextView getTextViewStart() {
            return textViewStart;
        }

        public TextView getTextViewEnd() {
            return textViewEnd;
        }
    }

    public RulesAdapter(List<Rule> dataSet) {
        localDataSet = dataSet;
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

        if (rule instanceof WlanRule) {
            viewHolder.getTextViewRuleTitle().setText(rule.getRuleName() + " - WLAN-Regel");
        } else if (rule instanceof AreaRule) {
            viewHolder.getTextViewRuleTitle().setText(rule.getRuleName() + " - Area-Regel");
        }

        viewHolder.getTextViewStart().setText(rule.getStartHour() + ":" + rule.getStartMinute());
        viewHolder.getTextViewEnd().setText(rule.getEndHour() + ":" + rule.getEndMinute());

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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

