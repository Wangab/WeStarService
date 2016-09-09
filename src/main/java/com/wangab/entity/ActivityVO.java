package com.wangab.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ActivityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(hidden = true)
    private long id;
    @ApiModelProperty(hidden = true)
    private String userid;
    private String title;
    private String contents;
    private Date startTime;
    private Date endTime;
    private Double commission;
    private int number;
    private String location;
    private String partyLocation;
    private String payType;
    private String contact;
    private String phone;

    @JsonCreator
    public ActivityVO(@JsonProperty("actid") long id, @JsonProperty("userID") String userid, @JsonProperty("title") String title, @JsonProperty("contents") String contents,@JsonProperty("startTime") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("endTime") Date endTime, @JsonProperty("commission") Double commission, @JsonProperty("number") int number, @JsonProperty("location") String location, @JsonProperty("partyLocation") String partyLocation, @JsonProperty("payType") String payType, @JsonProperty("contact") String contact, @JsonProperty("phone") String phone) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.contents = contents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.commission = commission;
        this.number = number;
        this.location = location;
        this.partyLocation = partyLocation;
        this.payType = payType;
        this.contact = contact;
        this.phone = phone;
    }

    public ActivityVO(long id, String title, String contents, Date startTime, Date endTime, Double commission, int number, String location, String partyLocation, String payType, String contact, String phone) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.commission = commission;
        this.number = number;
        this.location = location;
        this.partyLocation = partyLocation;
        this.payType = payType;
        this.contact = contact;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPartyLocation() {
        return partyLocation;
    }

    public void setPartyLocation(String partyLocation) {
        this.partyLocation = partyLocation;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[")
                .append(id).append("\t")
                .append(title).append("\t")
                .append(contents).append("\t")
                .append(startTime).append("\t")
                .append(endTime).append("\t")
                .append(commission).append("\t")
                .append(number).append("\t")
                .append(location).append("\t")
                .append(partyLocation).append("\t")
                .append(payType).append("\t")
                .append(contact).append("\t")
                .append(phone).append("]");
        return stringBuilder.toString();
    }
}
