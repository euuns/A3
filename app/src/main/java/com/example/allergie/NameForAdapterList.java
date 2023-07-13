package com.example.allergie;

public class NameForAdapterList {
    private String foodName;
    private String test;

    public NameForAdapterList(String foodName, String test){
        this.foodName = foodName;
        this.test = test;
    }

    public String getFoodName(){
        return foodName;
    }
    public String getTest(){ return test; }
}