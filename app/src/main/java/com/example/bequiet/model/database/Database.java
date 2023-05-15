package com.example.bequiet.model.database;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.room.Room;

import com.example.bequiet.model.Haversine;
import com.example.bequiet.model.RuleTimer;
import com.example.bequiet.model.VolumeManager;
import com.example.bequiet.model.dataclasses.AreaRule;
import com.example.bequiet.model.dataclasses.Rule;
import com.example.bequiet.model.dataclasses.WlanRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {

    private final AppDatabase dbRef;
    private final Context context;

    public Database(Context context) {
        this.context = context;
        dbRef = Room.databaseBuilder(context,
                AppDatabase.class, "rules").build();
    }


    public void addRuleIntoDb(Rule rule) {
        Thread thread = new Thread(() -> {
            if (rule instanceof AreaRule) {
                dbRef.ruleDAO().insertAreaRule((AreaRule) rule);
            } else if (rule instanceof WlanRule) {
                dbRef.ruleDAO().insertWlanRule((WlanRule) rule);
            }
            dbRef.close();
        });
        thread.start();
    }

    public void getAllRulesInCallback(RuleListCallback r) {
        Thread thread = new Thread(() -> {
            List<Rule> rules = new ArrayList<>();
            List<WlanRule> wlanRules = dbRef.ruleDAO().loadAllWlanRules();
            List<AreaRule> areaRules = dbRef.ruleDAO().loadAllAreaRules();
            rules.addAll(wlanRules);
            rules.addAll(areaRules);
            r.loaded(rules);
            dbRef.close();
        });
        thread.start();
    }

    public void getAreaRulesInCallback(AreaRuleListCallback r) {
        Thread thread = new Thread(() -> {
            List<AreaRule> areaRules = dbRef.ruleDAO().loadAllAreaRules();
            r.loaded(areaRules);
            dbRef.close();
        });
        thread.start();
    }

    public void getWlanRulesInCallback(WlanRuleListCallback r) {
        Thread thread = new Thread(() -> {
            List<WlanRule> wlanRules = dbRef.ruleDAO().loadAllWlanRules();
            r.loaded(wlanRules);
            dbRef.close();
        });
        thread.start();
    }

    public void updateDBAreaRule(AreaRule a) {
        Thread thread = new Thread(() -> {
            dbRef.ruleDAO().updateAreaRule(a);
            dbRef.close();
        });
        thread.start();
    }

    public void updateDBWlanRule(WlanRule w) {
        Thread thread = new Thread(() -> {
            dbRef.ruleDAO().updateWlanRule(w);
            dbRef.close();
        });
        thread.start();
    }
}
