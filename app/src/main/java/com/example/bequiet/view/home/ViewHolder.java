package com.example.bequiet.view.home;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
public class ViewHolder extends RecyclerView.ViewHolder {
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

        fragmentRule = (FragmentContainerView) view.findViewById(R.id.fragmentRule);
        fragmentRule.setId(View.generateViewId());
        textViewRuleTitle = (TextView) view.findViewById(R.id.textViewRuleTitle);
        textViewStart = (TextView) view.findViewById(R.id.textViewStartTime);
        textViewEnd = (TextView) view.findViewById(R.id.textViewEndTime);
        radioButtonSilence = (RadioButton) view.findViewById(R.id.radioButtonSilence);
        radioButtonVibrate = (RadioButton) view.findViewById(R.id.radioButtonVibrate);
        radioButtonNoise = (RadioButton) view.findViewById(R.id.radioButtonFullVolume);
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

    public View getView() {
        return view;
    }
}