package com.iplug.automation.models;

public class DevicesModel {
    String id;
    String status;
    String name;
    String type;
    String brightness;
    String schedual_string;
    String document_id;

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getSchedual_string() {
        return schedual_string;
    }

    public void setSchedual_string(String schedual_string) {
        this.schedual_string = schedual_string;
    }
    public DevicesModel() {
    }

    public DevicesModel(String id, String status, String name,String schedual_string) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.schedual_string = schedual_string;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
