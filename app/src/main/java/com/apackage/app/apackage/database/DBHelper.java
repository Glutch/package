package com.apackage.app.apackage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tjoffex on 13/11/17.
 *
 *
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, "OrderDataBase", null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Orders (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "clientId INTEGER," +
                "clientName TEXT NOT NULL," +
                "clientLastName TEXT NOT NULL," +
                "address TEXT NOT NULL," +
                "postalCode INTEGER," +
                "postalTown TEXT," +
                "orderId INTEGER," +
                "weight INTEGER," +
                "price INTEGER," +
                "deliveryTime TEXT," +
                "delivered INTEGER DEFAULT 0," +
                "lang TEXT," +
                "lat TEXT)";

        db.execSQL(sql);

    }

    @Override
    //Todo make a functional override of onUpgrade to anticipate db-changes when app in use
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Adds an order, takes the Order objects fields as parameters
     * @param clientID -
     * @param clientName -
     * @param address -
     * @param postalCode -
     * @param postalTown -
     * @param orderId -
     * @param weight -
     * @param deliveryTime -
     * @param delivered -
     */
    public void addOrder(long clientID, String clientName,String clientLastName, String address, long postalCode, String postalTown, long orderId, long weight, long price, String deliveryTime, boolean delivered){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("clientID", clientID);
        contentValues.put("clientName",clientName);
        contentValues.put("clientLastName",clientLastName);
        contentValues.put("address",address);
        contentValues.put("postalCode", postalCode);
        contentValues.put("postalTown", postalTown);
        contentValues.put("orderID", orderId);
        contentValues.put("weight", weight);
        contentValues.put("price", price);
        contentValues.put("deliveryTime", deliveryTime);
        if(delivered){
            contentValues.put("delivered", 1);
        } else {
            contentValues.put("delivered", 0);
        }

        long id = db.insert("Orders",null, contentValues);
        db.close();

    }

    /**
     * Adds an order, takes an Order object as parameter
     * @param order -
     */
    public void addOrder(Order order){
        addOrder(order.clientId, order.clientName, order.clientLastname, order.address, order.postalCode, order.postalTown, order.orderId, order.weight, order.price, order.deliveryTime, order.delivered);
    }

    /**
     * Adds a list of orders
     * @param orderList
     */
    public void addMultipleOrders(List<Order> orderList){
        for(Order order : orderList){
            addOrder(order);
        }

    }

    /**
     * Returns all orders in the database
     * @return a List of all Orders in the database
     */
    public List<Order> getAllOrders(){
        return getSpecificOrders(null,null,
                            null,null,null,null);
    }

    /**
     * Returns specific orders from the database
     * @param columns -
     * @param selection -
     * @param selectionArgs -
     * @param groupBy -
     * @param having -
     * @param orderby -
     * @return a List of orders matching the parameters
     */
    public List<Order> getSpecificOrders(String columns[], String selection, String[] selectionArgs,
                                         String groupBy, String having, String orderby ){
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("Orders", columns, selection, selectionArgs,groupBy,having, orderby);

        boolean success = c.moveToFirst();

        if( !success) {

            return orderList = null;

        }
        do{
            Order order = new Order();
            order.ID = c.getLong(0);
            order.clientId = c.getLong(1);
            order.clientName = c.getString(2);
            order.clientLastname = c.getString(3);
            order.address = c.getString(4);
            order.postalCode = c.getLong(5);
            order.postalTown = c.getString(6);
            order.orderId = c.getLong(7);
            order.weight = c.getLong(8);
            order.price =c.getLong(9);
            order.deliveryTime = c.getString(10);
            int delivered = c.getInt(11);
            order.delivered = (delivered == 1);
            if (c.getString(12)!=null)
                order.latLng = new LatLng( Double.parseDouble(c.getString(12)), Double.parseDouble(c.getString(13)));

            orderList.add(order);
            Log.d("DB-test", "getAllOrders: " + order.ID + ", " + order.clientId + ", " + order.address + ", " + order.postalCode + ", " + order.postalTown + ", " + order.orderId + ", " + order.weight + ", " + order.price+ ", " + order.deliveryTime + ", " + order.delivered);

        }while(c.moveToNext());

        db.close();
        c.close();
        return orderList;

    }


    /**
     * Marks an order as delivered
     * @param order -
     */
    public void markAsDelivered(Order order){
        String deliveryTime = DateFormat.getDateInstance().toString();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("delivered", 1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        contentValues.put("deliveryTime", simpleDateFormat.format(date.getTime()));

        db.update("Orders", contentValues, "id=?", new String[] {""+ order.ID});
        db.close();
    }

    /**
     * marks all orders on list as delivered
     * @param orderList -
     */
    public void markListAsDelivered(List<Order> orderList){
        for(Order order : orderList){
            markAsDelivered(order);
        }
    }

    /**
     * Get orders Delivered or not
     * @param isDelivered 1=true, 0=false
     * @return a List of orders from database.
     */
    public List<Order> getOrdersDelivered(int isDelivered){
        String selection = "delivered="+isDelivered;
        return getSpecificOrders(null,selection,null,null,
                                null,"deliveryTime ASC");
    }

    /**
     * Get order by orderID
     * @param orderID specifik order id.
     * @return one order. null if order doesnt exist
     */
    public Order getByOrderId(long orderID){
        String selection = "orderID="+orderID;

        // null if order doesnt exist
        if(getSpecificOrders(null,selection,null,null,null,null) == null)
            return null;
        return getSpecificOrders(null,selection,null,null,null,null).get(0);
    }

    public void setLatLang(Order order){
        String lat = "" + order.latLng.latitude;
        String lang = "" + order.latLng.longitude;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lang", lang);
        contentValues.put("lat", lat);

        db.update("Orders", contentValues, "id=?", new String[] {""+ order.ID});
        db.close();
    }

    //used to reset the database during dev to avoid version handling the database
    public void resetTheMF(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE Orders");
        onCreate(db);

    }




}
