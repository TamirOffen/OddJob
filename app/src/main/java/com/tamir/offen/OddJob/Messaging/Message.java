package com.tamir.offen.OddJob.Messaging;

/**
 * Created by paen3 on 8/18/2018.
 */

public class Message {
    private String message, type;
    private long time;
    private boolean seen;
    private String from;

    public Message(){

    }

    public Message(String message, String type, long time, boolean seen, String from) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
