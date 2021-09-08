package com.yuneec.image.utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class SaveSettings {

    public static String temperatureUnit_KEY = "TemperatureUnit_KEY";
    public static String language_KEY = "Language_KEY";

    private static Preferences preferences;

    private static SaveSettings instance;
    public static SaveSettings I() {
        if (instance == null) {
            instance = new SaveSettings();
        }
        preferences = Preferences.userRoot();
        return instance;
    }

    public void init(){

    }

    public void save(String key,int value){
        preferences.putInt(key,value);
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public int get(String key){
        int value = preferences.getInt(key,0);
        YLog.I(" SaveSettings ------->" + key + " : " + value);
        return value;
    }

    public static void main(String[] args) {

    }
}
