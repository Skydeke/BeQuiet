package com.example.bequiet.view.home;

import android.content.Intent;
import android.os.Bundle;

import com.example.bequiet.R;
import com.example.bequiet.databinding.ActivityHomePageBinding;
import com.example.bequiet.model.AppDatabase;
import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.Rule;
import com.example.bequiet.model.WlanRule;
import com.example.bequiet.presenter.HomePagePresenter;
import com.example.bequiet.view.edit.AddRuleActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements HomePagePresenter.ViewInterface {

    private ActivityHomePageBinding binding;
    private HomePagePresenter homePagePresenter;

    private TextView emptyListHint;

    private RecyclerView rulesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePagePresenter = new HomePagePresenter(this);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        emptyListHint = findViewById(R.id.textViewNoRulesCreated);
        rulesList = findViewById(R.id.rulesList);

        findViewById(R.id.addRuleFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(HomePageActivity.this, AddRuleActivity.class);
                startActivity(i);
            }
        });
        homePagePresenter.updateRules(new ArrayList<>(), HomePageActivity.this);
    }

    @Override
    public void updateRules(List<Rule> r) {
        RulesAdapter adapter = new RulesAdapter(r, getSupportFragmentManager());
        rulesList.setAdapter(adapter);
        rulesList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void setEmptyListTextShown(boolean shown) {
        if (shown) {
            emptyListHint.setVisibility(View.VISIBLE);
        } else {
            emptyListHint.setVisibility(View.INVISIBLE);
        }
    }
}