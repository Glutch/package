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

    //testingDB();
      // fakeOrder(1); // inparameter är antal order
  }

  //en metod för att testa databasfunktionalitet, ska ej vara kvar i slutprodukt
  public void testingDB(){
      DBHelper dbHelper = new DBHelper(this);
      dbHelper.addOrder(111,"Ett namn", "En adress", 222, "En postort", 333, 444, "en tid", false);
      dbHelper.getAllOrders();

  }


  public ArrayList<Order> fakeOrder(int amountOforders){

      String[] adresser = getResources().getStringArray(R.array.fadresser);
      String[] names    = getResources().getStringArray(R.array.fnames);
      String[] cities    = getResources().getStringArray(R.array.fcities);
      return RandFakeOrder.randomOrd(amountOforders,adresser,names,cities);

  }
}
