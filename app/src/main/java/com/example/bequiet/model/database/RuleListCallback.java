package com.example.bequiet.model.database;

import com.example.bequiet.model.dataclasses.Rule;

import java.util.List;

public interface RuleListCallback {

    void loaded(List<Rule> l);
}
