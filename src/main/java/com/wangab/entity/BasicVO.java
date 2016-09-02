package com.wangab.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by wanganbang on 9/1/16.
 */
public class BasicVO implements Serializable {
    private static final long serialVersionUID = 7847704025901187458L;

    @JsonProperty("accessToken")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
