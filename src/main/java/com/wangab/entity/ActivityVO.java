package com.wangab.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ActivityVO implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String contents;
    private Timestamp startTime;
    private Timestamp endTime;
    private Double commission;
    private int number;
    private String location;
    private String partyLocation;
    private String payType;
    private String contact;
    private String phone;

    @JsonCreator
    public ActivityVO(long id, String title, String contents, Timestamp startTime, Timestamp endTime, Double commission, int number, String location, String partyLocation, String payType, String contact, String phone) {
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
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
