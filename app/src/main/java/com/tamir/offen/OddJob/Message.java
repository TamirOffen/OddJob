package com.tamir.offen.OddJob;

/**
 * Created by paen3 on 8/18/2018.
 */

public class Message {
    private String message, type;
    private long time;
    private boolean seen;

    public Message(){

    }

    public Message(String message, String type, long time, boolean seen) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
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
}
