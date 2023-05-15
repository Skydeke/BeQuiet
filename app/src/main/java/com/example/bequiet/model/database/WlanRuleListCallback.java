package com.example.bequiet.model.database;

import com.example.bequiet.model.dataclasses.WlanRule;

import java.util.List;

public interface WlanRuleListCallback {

    void loaded(List<WlanRule> l);
}
