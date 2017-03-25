package com.alp.getirhackathon.Service;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class KeyValuePair {

    private String Key;
    private String Value;

    public KeyValuePair(String key, String value) {
        Key = key;
        Value = value;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
