package com.iplug.automation.models;

public class RoomsModel {
    String RoomName;
    int on_count;
    int off_count;


    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public int getOn_count() {
        return on_count;
    }

    public void setOn_count(int on_count) {
        this.on_count = on_count;
    }

    public int getOff_count() {
        return off_count;
    }

    public void setOff_count(int off_count) {
        this.off_count = off_count;
    }
}
