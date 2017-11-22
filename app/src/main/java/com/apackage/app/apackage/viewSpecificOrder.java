package com.apackage.app.apackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;

import java.util.List;

public class viewSpecificOrder extends AppCompatActivity {

  private TextView clientID;
  private TextView ordernumber;
  private TextView firstname;
  private TextView lastname;
  private TextView address;
  private TextView postcode;
  private TextView city;
  private TextView weight;
  private TextView price;
  private TextView deliverydate;
  private TextView deliverystatus;
  private Order order;
  private DBHelper dbHelper;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_specific_order);

    Intent intent = getIntent();
    long id = intent.getLongExtra("ID", -1);

    dbHelper = new DBHelper(this);
    order = dbHelper.getByOrderId((int)id);

    clientID = findViewById(R.id.clientID);
    clientID.setText(""+order.prize);
    ordernumber = findViewById(R.id.ordernumber);
    ordernumber.setText(""+order.orderId);
    firstname = findViewById(R.id.firstname);
    firstname.setText(order.clientName);
    lastname = findViewById(R.id.lastname);
    lastname.setText("?????" );
    address = findViewById(R.id.address);
    address.setText(order.address);
    postcode = findViewById(R.id.postcode);
    postcode.setText(""+order.postalCode);
    city = findViewById(R.id.city);
    city.setText(order.postalTown);
    weight = findViewById(R.id.weight);
    weight.setText(""+order.weight);
    price = findViewById(R.id.price);
    price.setText(""+order.prize);
    deliverydate = findViewById(R.id.deliverydate);
    deliverydate.setText(order.deliveryTime);
    deliverystatus = findViewById(R.id.deliverystatus);
    deliverystatus.setText(""+order.delivered);
  }

  public void onClickDelivered(View view) {
    dbHelper.markAsDelivered(order);
    Intent intent = getIntent();
    setResult(RESULT_OK , intent);
    finish();
  }
}
