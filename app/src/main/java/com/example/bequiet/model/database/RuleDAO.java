package com.example.bequiet.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bequiet.model.dataclasses.AreaRule;
import com.example.bequiet.model.dataclasses.WlanRule;

import java.util.List;

@Dao
public interface RuleDAO {

    @Query("SELECT * FROM WLANRULE")
    List<WlanRule> loadAllWlanRules();

    @Query("SELECT * FROM AreaRule")
    List<AreaRule> loadAllAreaRules();

    @Insert
    void insertWlanRule(WlanRule wlanRule);

    @Insert
    void insertAreaRule(AreaRule areaRule);

    @Update
    void updateWlanRule(WlanRule wlanRule);

    @Update
    void updateAreaRule(AreaRule areaRule);

    @Delete
    void deleteWlanRule(WlanRule wlanRule);

    @Delete
    void deleteAreaRule(AreaRule areaRule);

}
