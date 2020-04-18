package com.chajs.dailysentences;

import java.io.Serializable;

public class Sentence  implements Serializable {

    private static final long serialVersionUID = 1L;

    public String id;
    public String korSentence;
    public String engSentence;
    public String startDate;
    public String endDate;
    public String popupTime;
    public String sucessCount;
    public String failCount;
    public String skipCount;

    public Sentence(String id, String korSentence, String engSentence, String startDate, String endDate, String popupTime, String sucessCount, String failCount, String skipCount) {
        this.id = id;
        this.korSentence = korSentence;
        this.engSentence = engSentence;
        this.startDate = startDate;
        this.endDate = endDate;
        this.popupTime = popupTime;
        this.sucessCount = sucessCount;
        this.failCount = failCount;
        this.skipCount = skipCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKorSentence() {
        return korSentence;
    }

    public void setKorSentence(String korSentence) {
        this.korSentence = korSentence;
    }

    public String getEngSentence() {
        return engSentence;
    }

    public void setEngSentence(String engSentence) {
        this.engSentence = engSentence;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPopupTime() {
        return popupTime;
    }

    public void setPopupTime(String popupTime) {
        this.popupTime = popupTime;
    }

    public String getSucessCount() {
        return sucessCount;
    }

    public void setSucessCount(String sucessCount) {
        this.sucessCount = sucessCount;
    }

    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }

    public String getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(String skipCount) {
        this.skipCount = skipCount;
    }


}
