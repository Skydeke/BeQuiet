package com.example.bequiet.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WlanRule.class, AreaRule.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RuleDAO ruleDAO();
}