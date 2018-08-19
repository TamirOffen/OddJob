package com.tamir.offen.OddJob;

import java.io.Serializable;

/**
 * Created by paen3 on 8/15/2018.
 */

public class User implements Serializable{
    String id;
    String name;
    String email;
    String parentId;

    public User(){

    }
    public User(String id,String name, String email){
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
