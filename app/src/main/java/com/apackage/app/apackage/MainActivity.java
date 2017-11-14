package com.apackage.app.apackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apackage.app.apackage.database.DBHelper;

public class MainActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //testingDB();

  }

  //en metod för att testa databasfunktionalitet, ska ej vara kvar i slutprodukt
  public void testingDB(){
      DBHelper dbHelper = new DBHelper(this);
      dbHelper.addOrder(111,"Ett namn", "En adress", 222, "En postort", 333, 444, "en tid", false);
      dbHelper.getAllOrders();

  }
}
