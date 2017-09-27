package com.example.ibm.hermes;

public class Messages {
    private String Main_message;
    private String Sender;
    private long Time;

    public Messages(String Main_message, String Sender, long Time) {
        this.Main_message = Main_message;
        this.Sender = Sender;
        this.Time = Time;
    }

    public Messages (){ }

    public String getMain_message() {
        return this.Main_message;
    }

    public void setMain_message(String main_message) {
        this.Main_message = main_message;
    }

    public String getSender() {
        return this.Sender;
    }

    public void setSender(String sender) {
        this.Sender = sender;
    }

    public long getTime() {
        return this.Time;
    }

    public void setTime(long time) {
        this.Time = time;
    }
}
