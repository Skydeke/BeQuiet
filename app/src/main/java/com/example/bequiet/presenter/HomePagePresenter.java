package com.example.bequiet.presenter;

import android.content.Context;

import com.example.bequiet.model.database.Database;
import com.example.bequiet.model.dataclasses.Rule;

import java.util.List;

public class HomePagePresenter {

    private final ViewInterface viewInterface;

    public HomePagePresenter(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void getRulesAndDraw(Context context) {
        Database database = new Database(context);
        database.getAllRulesInCallback(rules -> {
            viewInterface.setEmptyListTextShown(rules.size() == 0);
            viewInterface.redrawRules(rules);
        });
    }

    public interface ViewInterface {
        void redrawRules(List<Rule> r);

        void setEmptyListTextShown(boolean shown);
    }
}
