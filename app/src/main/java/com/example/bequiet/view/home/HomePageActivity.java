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

import androidx.annotation.Nullable;
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

    private RulesAdapter adapter;

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
        ArrayList<Rule> rules = new ArrayList<>();
        homePagePresenter.updateRules(rules, HomePageActivity.this);
        adapter = new RulesAdapter(rules);
        rulesList.setAdapter(adapter);
        rulesList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateRules(List<Rule> r) {
        this.runOnUiThread(() -> {
            adapter.clearFragments();
            adapter = new RulesAdapter(r);
            rulesList.setAdapter(adapter);
            rulesList.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePagePresenter.updateRules(new ArrayList<>(), getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homePagePresenter.updateRules(new ArrayList<>(), getApplicationContext());
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