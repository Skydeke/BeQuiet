package com.example.bequiet.presenter;

import com.example.bequiet.model.AreaRule;
import com.example.bequiet.model.Rule;
import com.example.bequiet.model.WlanRule;
import com.example.bequiet.view.HomePageActivity;

import java.util.List;

public class HomePagePresenter {

    private ViewInterface viewInterface;

    public HomePagePresenter(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void updateRules(List<Rule> rules){
        rules.add(new WlanRule("Name1Ru", 7, 30, 10, 30, "LanName"));
        rules.add(new WlanRule("Name2Ru", 7, 30, 15, 50, "2LanName"));
        rules.add(new AreaRule("Rav", 7, 30, 15, 50, 10, 47.8127457112777, 9.656508679012063));
        rules.add(new AreaRule("Greenwitch", 7, 30, 15, 50, 20, 46.8127457112777, 8.656508679012063));
        if (rules.size() == 0){
            viewInterface.setEmptyListTextShown(true);
        }else {
            viewInterface.setEmptyListTextShown(false);
        }
        viewInterface.updateRules(rules);
    }

    public interface ViewInterface{
        public void updateRules(List<Rule> r);
        public void setEmptyListTextShown(boolean shown);
    }
}
