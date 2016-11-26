package com.wangab.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * OtherRegistVO
 *
 * @author Anbang Wang
 * @date 2016/11/23
 */
public class OtherRegistVO {
    @ApiModelProperty(name = "openid", value = "第三方的openid", dataType = "String", required = true)
    @NotEmpty(message = "openid is not null or empty")
    private String openid;

    @ApiModelProperty(name = "accountType", value = "第三方账户类型，包括weixin、qq、weibo", dataType = "String", required = true)
    @NotEmpty(message = "accountType is not null or empty")
    private String accountType;

    @ApiModelProperty(name = "nick", value = "第三方的昵称", dataType = "String", required = true)
    @NotEmpty(message = "nick is not null or empty")
    private String nick;

    @ApiModelProperty(name = "iconUrl", value = "第三方的的头像url", dataType = "String")
    private String iconUrl;

    @ApiModelProperty(name = "sex", value = "第三方性别", dataType = "String", required = true)
    @NotEmpty(message = "sex is not null or empty")
    private String sex;

    @ApiModelProperty(name = "signature", value = "第三方的个性签名", dataType = "String")
    private String signature;
    @ApiModelProperty(name = "addr", value = "第三方的个性地址", dataType = "String")
    private String addr;
    @ApiModelProperty(name = "birthday", value = "第三方生日", dataType = "Date")
    private Date birthday;
    @ApiModelProperty(hidden = true)
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
