package com.apackage.app.apackage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.FontsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tjoffex on 13/11/17.
 *
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "OrderDataBase", null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Orders (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "clientId INTEGER," +
                "clientName TEXT NOT NULL," +
                "address TEXT NOT NULL," +
                "postalCode INTEGER," +
                "postalTown TEXT," +
                "orderId INTEGER," +
                "weight INTEGER," +
                "deliveryTime TEXT," +
                "delivered INTEGER DEFAULT 0)";

        db.execSQL(sql);

    }

    @Override
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
    public void addOrder(long clientID, String clientName, String address, long postalCode, String postalTown, long orderId, long weight, String deliveryTime, boolean delivered){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("clientID", clientID);
        contentValues.put("clientName",clientName);
        contentValues.put("address",address);
        contentValues.put("postalCode", postalCode);
        contentValues.put("postalTown", postalTown);
        contentValues.put("orderID", orderId);
        contentValues.put("weight", weight);
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
        addOrder(order.clientId, order.clientName, order.address, order.postalCode, order.postalTown, order.orderId, order.weight, order.deliveryTime, order.delivered);
    }

    //Todo finish method addMultiple orders
    public void addMultipleOrders(List<Order> orderList){

    }

    /**
     * Returns all orders in the database
     * @return a List of all Orders in the database
     */
    public List<Order> getAllOrders(){
        return getSpecificOrders(null,null,null,null,null,null);
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
    public List<Order> getSpecificOrders(String columns[], String selection, String[] selectionArgs, String groupBy, String having, String orderby ){
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("Orders", columns, selection, selectionArgs,groupBy,having, orderby);

        boolean success = c.moveToFirst();

        if( !success)
            return orderList;
        do{
            Order order = new Order();
            order.ID = c.getLong(0);
            order.clientId = c.getLong(1);
            order.clientName = c.getString(2);
            order.address = c.getString(3);
            order.postalCode = c.getLong(4);
            order.postalTown = c.getString(5);
            order.orderId = c.getLong(6);
            order.weight = c.getLong(7);
            order.deliveryTime = c.getString(8);
            int delivered = c.getInt(9);
            order.delivered = (delivered == 1);

            orderList.add(order);
            Log.d("DB-test", "getAllOrders: " + order.ID + ", " + order.clientId + ", " + order.address + ", " + order.postalCode + ", " + order.postalTown + ", " + order.orderId + ", " + order.weight + ", " + order.deliveryTime + ", " + order.delivered);

        }while(c.moveToNext());

        db.close();
        c.close();
        return orderList;

    }




}
