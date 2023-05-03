package com.example.bequiet.presenter;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.bequiet.model.AppDatabase;
import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.Rule;
import com.example.bequiet.model.WlanRule;

import java.util.LinkedList;
import java.util.List;

public class HomePagePresenter {

    private ViewInterface viewInterface;

    public HomePagePresenter(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void updateRules(List<Rule> rules, Context context) {

        Thread thread = new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "rules").build();

            List<WlanRule> wlanRules = db.ruleDAO().loadAllWlanRules();
            List<AreaRule> areaRules = db.ruleDAO().loadAllAreaRules();
            rules.addAll(wlanRules);
            rules.addAll(areaRules);

            if (rules.size() == 0) {
                viewInterface.setEmptyListTextShown(true);
            } else {
                viewInterface.setEmptyListTextShown(false);
            }
            Log.i("database", db.ruleDAO().loadAllAreaRules().toString());
            viewInterface.updateRules(rules);
        });

        thread.start();
    }

    public interface ViewInterface {
        public void updateRules(List<Rule> r);

        public void setEmptyListTextShown(boolean shown);
    }
}
