package com.wangab.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.Nullable;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by wanganbang on 8/31/16.
 */
public class ChgPWDVO implements Serializable {
    private static final long serialVersionUID = 7847704025901187458L;
    @Nullable
    @ApiModelProperty(hidden = true)
    private String userid;
    @ApiModelProperty(hidden = true)
    private String oldpwd;
    @ApiModelProperty(hidden = true)
    private String newpwd;

    @JsonCreator
    public ChgPWDVO(@JsonProperty("userID") String userid, @JsonProperty("oldPWD") String oldpwd, @JsonProperty("passWD") String newpwd) {
        this.userid = userid;
        this.oldpwd = oldpwd;
        this.newpwd = newpwd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOldpwd() {
        return oldpwd;
    }

    public void setOldpwd(String oldpwd) {
        this.oldpwd = oldpwd;
    }

    public String getNewpwd() {
        return newpwd;
    }

    public void setNewpwd(String newpwd) {
        this.newpwd = newpwd;
    }
}
