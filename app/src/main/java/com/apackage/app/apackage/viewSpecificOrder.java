package com.apackage.app.apackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;

import java.util.List;

public class viewSpecificOrder extends AppCompatActivity {

  private TextView ordernumber;
  private TextView firstname;
  private TextView lastname;
  private TextView address;
  private TextView postcode;
  private TextView city;
  private TextView weight;
  private TextView deliverydate;
  private TextView deliverystatus;
  private Order order;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_specific_order);

    Intent intent = getIntent();
    long id = intent.getLongExtra("ID", -1);

    DBHelper dbHelper = new DBHelper(this);
    Order order = dbHelper.getByOrderId((int)id);

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
    deliverydate = findViewById(R.id.deliverydate);
    deliverydate.setText(order.deliveryTime);
    deliverystatus = findViewById(R.id.deliverystatus);
    deliverystatus.setText(""+order.delivered);
  }
}
