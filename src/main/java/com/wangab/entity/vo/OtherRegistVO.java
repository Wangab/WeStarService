package com.wangab.entity.vo;

import com.wangab.entity.enums.OtherAcountType;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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
    @ApiModelProperty(name = "acountType", value = "第三方账户类型，包括weixin、qq、weibo", dataType = "OtherAcountType", required = true)
    @NotEmpty(message = "acountType is not null or empty")
    private OtherAcountType acountType;
    @ApiModelProperty(name = "nick", value = "第三方的昵称", dataType = "String", required = true)
    @NotEmpty(message = "nick is not null or empty")
    private String nick;
    @ApiModelProperty(name = "iconUrl", value = "第三方的的头像url", dataType = "String", required = true)
    @NotEmpty(message = "iconUrl is not null or empty")
    private String iconUrl;
    @ApiModelProperty(name = "signature", value = "第三方的个性签名", dataType = "String", required = true)
    private String signature;
    @ApiModelProperty(name = "addr", value = "第三方的个性地址", dataType = "String")
    private String addr;
    @ApiModelProperty(name = "birthday", value = "第三方生日", dataType = "Date")
    private Date birthday;
    @ApiModelProperty(name = "sex", value = "第三方性别", dataType = "String", required = true)
    private String sex;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public OtherAcountType getAcountType() {
        return acountType;
    }

    public void setAcountType(OtherAcountType acountType) {
        this.acountType = acountType;
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
