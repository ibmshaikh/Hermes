package com.example.ibm.hermes;

import com.google.firebase.database.ServerValue;
import java.util.HashMap;
import java.util.Map;

public class LastMessage {
    private String Lastm;
    private String LastmessageSender;
    private Map<String, String> timestampCreated;

    public String getLastm() {
        return this.Lastm;
    }

    public void setLastm(String lastm) {
        this.Lastm = lastm;
    }

    public Map<String, String> getTimestampCreated() {
        return this.timestampCreated;
    }

    public String getLastmessageSender() {
        return this.LastmessageSender;
    }

    public void setLastmessageSender(String lastmessageSender) {
        this.LastmessageSender = lastmessageSender;
    }



    public void setTimestampCreated(Map<String, String> timestamp) {
        this.timestampCreated = timestamp;
    }
}
