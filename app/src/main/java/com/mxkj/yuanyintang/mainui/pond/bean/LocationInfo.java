package com.mxkj.yuanyintang.mainui.pond.bean;

import java.io.Serializable;

public class LocationInfo implements Serializable{
    private String address;
    private String addressDesc;
    private Double Latitude;//未读
    private Double LonTitude;//精度

    public String getAddressDesc() {
        return addressDesc;
    }

    public void setAddressDesc(String addressDesc) {
        this.addressDesc = addressDesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLonTitude() {
        return LonTitude;
    }

    public void setLonTitude(Double lonTitude) {
        LonTitude = lonTitude;
    }
}
