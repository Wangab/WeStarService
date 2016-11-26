package com.wangab.entity.po;

import org.json.JSONObject;

/**
 * Created by wanganbang on 11/26/16.
 */
public class EasemobPO {
    private String username;
    private String password;
    private String nickname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public JSONObject toJsonObject(){
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("nickname", nickname);
        json.put("password", password);
        return json;
    }
}
