package com.apackage.app.apackage.database;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tjoffex on 13/11/17.
 */


public class Order {
    public long ID;
    public long clientId;
    public String clientName;
    public String clientLastname;
    public String address;
    public long postalCode;
    public String postalTown;
    public long orderId;
    public long weight;
    public long price;
    public String deliveryTime;
    public boolean delivered;
    public LatLng latLng;


}
