package com.example.bequiet.model.database;

import com.example.bequiet.model.dataclasses.AreaRule;
import com.example.bequiet.model.dataclasses.WlanRule;

import java.util.List;

public interface AreaRuleListCallback {

    void loaded(List<AreaRule> l);
}
