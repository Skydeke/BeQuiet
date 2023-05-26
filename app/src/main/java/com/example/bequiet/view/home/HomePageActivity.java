package com.example.bequiet.view.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.databinding.ActivityHomePageBinding;
import com.example.bequiet.model.dataclasses.Rule;
import com.example.bequiet.presenter.HomePagePresenter;
import com.example.bequiet.view.edit.AddRuleActivity;

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

        // Set the color of the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_gray_600));
        }

        homePagePresenter = new HomePagePresenter(this);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);


        emptyListHint = findViewById(R.id.textViewNoRulesCreated);
        rulesList = findViewById(R.id.rulesList);

        findViewById(R.id.addRuleFab).setOnClickListener(view -> {
            final Intent i = new Intent(HomePageActivity.this, AddRuleActivity.class);
            startActivity(i);
        });
        ArrayList<Rule> rules = new ArrayList<>();
        homePagePresenter.getRulesAndDraw(HomePageActivity.this);
        adapter = new RulesAdapter(rules, homePagePresenter);
        rulesList.setAdapter(adapter);
        rulesList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void redrawRules(List<Rule> r) {
        this.runOnUiThread(() -> {
            adapter.clearFragments();
            adapter = new RulesAdapter(r, homePagePresenter);
            rulesList.setAdapter(adapter);
            rulesList.setLayoutManager(new LinearLayoutManager(this));
            rulesList.scrollToPosition(0);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePagePresenter.getRulesAndDraw(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homePagePresenter.getRulesAndDraw(getApplicationContext());
    }

    @Override
    public void setEmptyListTextShown(boolean shown) {
        this.runOnUiThread(() -> {
            if (shown) {
                emptyListHint.setVisibility(View.VISIBLE);
            } else {
                emptyListHint.setVisibility(View.INVISIBLE);
            }
        });
    }
}