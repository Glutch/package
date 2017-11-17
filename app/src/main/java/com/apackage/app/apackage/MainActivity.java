package com.apackage.app.apackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;
import com.apackage.app.apackage.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    testingDB();

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    int number = sharedPreferences.getInt(SettingsActivity.KEY_GENERATEORDERS, -1);
    TextView textView = (TextView) findViewById(R.id.mainTextView);
    textView.setText("" + number);
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
  //Testar settings, ska ej vara kvar
  public void SettingsButton(View view) {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);

  }
}




