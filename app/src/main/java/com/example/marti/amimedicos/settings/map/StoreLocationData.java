package com.example.marti.amimedicos.settings.map;

/**
 * Created by Marti on 7/10/18.
 */

public class StoreLocationData {

    String lat;

    String lng;

    String distanceInMtrs;

    String distanceInKm;

    public StoreLocationData() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistanceInMtrs() {
        return distanceInMtrs;
    }

    public void setDistanceInMtrs(String distanceInMtrs) {
        this.distanceInMtrs = distanceInMtrs;
    }

    public String getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(String distanceInKm) {
        this.distanceInKm = distanceInKm;
    }
}
