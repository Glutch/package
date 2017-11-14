package com.apackage.app.apackage.database;

import java.time.LocalDateTime;

/**
 * Created by Tjoffex on 13/11/17.
 *
 */


public class Order {
    public long ID;
    public long clientId;
    public String clientName;
    public String address;
    public long postalCode;
    public String postalTown;
    public long orderId;
    public long weight;
    //public LocalDateTime deliveryTime;  Utkommenterad tills jag får datetime att fungera i sqlite, ersätts av String tillsvidare
    public String deliveryTime;
    public boolean delivered;

}
