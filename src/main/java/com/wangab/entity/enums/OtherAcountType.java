package com.wangab.entity.enums;

/**
 * OtherAcountType
 *
 * @author Anbang Wang
 * @date 2016/11/23
 */
public enum OtherAcountType {
    WEIXIN("WEIXIN", 2),QQ("QQ", 3),SINAWEIBO("WEIBO", 4);

    private String name;
    private Integer index;

    private OtherAcountType(String name, Integer index){
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
