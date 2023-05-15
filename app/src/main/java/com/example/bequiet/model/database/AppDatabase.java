package com.example.bequiet.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bequiet.model.dataclasses.AreaRule;
import com.example.bequiet.model.dataclasses.WlanRule;

@Database(entities = {WlanRule.class, AreaRule.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RuleDAO ruleDAO();
}