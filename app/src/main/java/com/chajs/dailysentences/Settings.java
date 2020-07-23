package com.chajs.dailysentences;

import java.io.Serializable;

public class Settings  implements Serializable {
    private static final long serialVersionUID = 2L;

    public String id;
    public String autoArlamYn;
    public String fromTime;
    public String toTIme;
    public String dailyArlamCount;
    public String usingServerYn;
    public String email;

    public Settings(String id, String autoAaramYn, String fromTime, String toTIme, String dailyArlamCount, String usingServerYn, String email) {
        this.id = id;
        this.autoArlamYn = autoAaramYn;
        this.fromTime = fromTime;
        this.toTIme = toTIme;
        this.dailyArlamCount = dailyArlamCount;
        this.usingServerYn = usingServerYn;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutoArlamYn() {
        return autoArlamYn;
    }

    public void setAutoArlamYn(String autoArlamYn) {
        this.autoArlamYn = autoArlamYn;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTIme() {
        return toTIme;
    }

    public void setToTIme(String toTIme) {
        this.toTIme = toTIme;
    }

    public String getDailyArlamCount() {
        return dailyArlamCount;
    }

    public void setDailyArlamCount(String dailyArlamCount) {
        this.dailyArlamCount = dailyArlamCount;
    }

    public String getUsingServerYn() {
        return usingServerYn;
    }

    public void setUsingServerYn(String usingServerYn) {
        this.usingServerYn = usingServerYn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
