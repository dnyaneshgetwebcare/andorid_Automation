package com.iplug.automation.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_profile")
public class UserProfileEntity implements Serializable {
    @NonNull
    @PrimaryKey
    String email_id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "address")
    String address;
    @ColumnInfo(name = "contact_nos")
    String contact_nos;
    @ColumnInfo(name = "property_type")
    String property_type;
    @ColumnInfo(name = "property_vairent")
    String property_vairent;

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getProperty_vairent() {
        return property_vairent;
    }

    public void setProperty_vairent(String property_vairent) {
        this.property_vairent = property_vairent;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_nos() {
        return contact_nos;
    }

    public void setContact_nos(String contact_nos) {
        this.contact_nos = contact_nos;
    }
}
