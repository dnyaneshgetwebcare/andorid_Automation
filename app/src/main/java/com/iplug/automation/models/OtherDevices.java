package com.iplug.automation.models;

public class OtherDevices {
    public int getOnDevices() {
        return onDevices;
    }

    public void setOnDevices(int onDevices) {
        this.onDevices = onDevices;
    }

    public int getOffDevices() {
        return OffDevices;
    }

    public void setOffDevices(int offDevices) {
        OffDevices = offDevices;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    int onDevices;
    int OffDevices;
    String roomtype;

}
