package com.getwebcare.automation.models;

public class DevicesModel {
    String id;
    String status;
    String name;
    String brightness;

    public DevicesModel() {
    }

    public DevicesModel(String id, String status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }
}
