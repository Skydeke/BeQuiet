package com.example.bequiet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.bequiet.model.Haversine;

import org.junit.Test;

public class WithinCircleTests {
    @Test
    public void testIfPointIsCorrectlyClassifiedAsWithinRange() {

        //point in the middle of the circle
        double latCenter = 47.812848;
        double lonCenter = 9.651058;

        //point inside of the circle
        double latPoint = 47.812662;
        double lonPoint = 9.650020;

        assertTrue(Haversine.pointIsWithinCircle(latCenter, lonCenter, latPoint, lonPoint, 100));
    }


    @Test
    public void testIfPointIsCorrectlyClassifiedAsOutOfRange() {

        //point in the middle of the circle
        double latCenter = 47.812848;
        double lonCenter = 9.651058;

        //point outside of the circle
        double latPoint = 47.812662;
        double lonPoint = 9.650020;

        assertFalse(Haversine.pointIsWithinCircle(latCenter, lonCenter, latPoint, lonPoint, 5));
    }

    @Test
    public void testIfPointIsCorrectlyClassifiedAsWithinRangeWhenPointsAreTheSame() {

        //point in the middle of the circle
        double latCenter = 47.812848;
        double lonCenter = 9.651058;

        //same point in the circle
        double latPoint = 47.812848;
        double lonPoint = 9.651058;

        assertTrue(Haversine.pointIsWithinCircle(latCenter, lonCenter, latPoint, lonPoint, 0));
    }


}