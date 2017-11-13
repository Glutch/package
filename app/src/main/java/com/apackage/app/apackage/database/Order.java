package com.apackage.app.apackage.database;

import java.time.LocalDateTime;

/**
 * Created by Tjoffex on 13/11/17.
 *
 */


public class Order {
    public long clientId;
    public String name;
    public String adress;
    public long orderId;
    public int weight;
    public boolean delivered;
    public LocalDateTime deliveryTime;
}
