package com.iplug.automation.models;

public class SchedualDetails {
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    String device_id;
    String time;
    String status;
    String duration;
    String device_type;
    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    String brightness;
    int sch_pos;

    public int getSch_pos() {
        return sch_pos;
    }

    public void setSch_pos(int sch_pos) {
        this.sch_pos = sch_pos;
    }
}
