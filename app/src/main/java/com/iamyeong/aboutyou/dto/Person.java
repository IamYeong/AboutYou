package com.iamyeong.aboutyou.dto;

import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String group;
    private String tinyInfo;

    //즐겨찾기
    private boolean started;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTinyInfo() {
        return tinyInfo;
    }

    public void setTinyInfo(String tinyInfo) {
        this.tinyInfo = tinyInfo;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
