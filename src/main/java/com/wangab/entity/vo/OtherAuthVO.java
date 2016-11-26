package com.wangab.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by wanganbang on 11/26/16.
 */
public class OtherAuthVO {
    @ApiModelProperty(name = "openid", value = "第三方认证后得到的为以标识", dataType = "String")
    @NotEmpty(message = "第三方认证openid不存在")
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
