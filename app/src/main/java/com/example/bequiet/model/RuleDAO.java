package com.example.bequiet.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RuleDAO {

    @Query("SELECT * FROM WLANRULE")
    public List<WlanRule> loadAllWlanRules();

    @Query("SELECT * FROM AreaRule")
    public List<AreaRule> loadAllAreaRules();

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
