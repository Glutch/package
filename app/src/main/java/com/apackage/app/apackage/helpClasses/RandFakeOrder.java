package com.apackage.app.apackage.helpClasses;


import com.apackage.app.apackage.database.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Petteri Tuononen on 2017-11-15.
 */

public class RandFakeOrder {


    private static Random rand = new Random();

    public static ArrayList<Order> randomOrd(int amountOforders) {

        String[] streets = {"Adventvagen", "Aftongatan", "Ahrenbergsgatan", "AdolfEdelsvardsgatan", "AdlerSalviusgatan",
                "Alevägen", "Alelundsgatan", "Alelyckegatan", "AlbertEngstrumgatan", "Albotorget", "Aprilgatan", "Arbetsgatan"};
        String[] names = {"Kalle","Anna","Viktor","Max","Tobia","David","Daniel","Fredrik","Markus","Rickard","Sebastian",
                "Hanna"};
        String[] cities = {"Göteborg","Borås","Falkenberg","Malmö","Stockholm","Uppsala","Visby","Lund","Jönköping"
                ,"Gävle","Norrköping","Västerås"};
        String[] lastnames = {"Eriksson","Johansson"};


        ArrayList<Order> orderlist = new ArrayList<Order>();

        for (int i = 0; i < amountOforders; i++) {

        Order order = new Order();

        Date now = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date addedDate = addDays(now, rand.nextInt(30));

        order.deliveryTime = simpleDateFormat.format(addedDate);

        order.address = streets[rand.nextInt(streets.length)];
        order.postalTown = cities[rand.nextInt(cities.length)];
        order.postalCode = rand.nextInt(99999);
        order.clientName = names[rand.nextInt(names.length)];
        order.clientLastname = lastnames[rand.nextInt(lastnames.length)];
        order.clientId = rand.nextInt(99999);
        order.weight = (long) rand.nextInt(999999);
        order.orderId = rand.nextInt(40000);
        order.price = 100 * rand.nextInt(40);

        orderlist.add(order);
        }
        return orderlist;
    }

   private static Date addDays(Date d, int days)
    {
        d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
        return d;
    }
}
