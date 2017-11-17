package com.apackage.app.apackage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    testingDB();

  }

  //en metod för att testa databasfunktionalitet, ska ej vara kvar i slutprodukt
  public void testingDB(){
      DBHelper dbHelper = new DBHelper(this);
    //  dbHelper.resetTheMF();   //<- use this to reset database if needed

    //     dbHelper.addMultipleOrders(fakeOrder(5));
      dbHelper.getAllOrders();

  }


  public ArrayList<Order> fakeOrder(int amountOforders){
      return RandFakeOrder.randomOrd(amountOforders);

  }
}
