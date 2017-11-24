package com.apackage.app.apackage;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class viewSpecificOrder extends AppCompatActivity implements OnMapReadyCallback{

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
  private Button deliveredBtn;
  private FusedLocationProviderClient mFusedLocationClient;
  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_specific_order);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    mFusedLocationClient =  LocationServices.getFusedLocationProviderClient(this);

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
    lastname.setText("?????" ); // todo fix last name?
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
    deliveredBtn = findViewById(R.id.button2);
    if (order.delivered)
      deliveredBtn.setVisibility(View.GONE);
  }

  public void onClickDelivered(View view) {
    dbHelper.markAsDelivered(order);
    Intent intent = getIntent();
    setResult(RESULT_OK , intent);
    finish();
  }


  @Override
  public void onMapReady(GoogleMap googleMap) {
    LatLng sydney = new LatLng(-33.852, 151.211);
    googleMap.addMarker(new MarkerOptions().position(sydney)
            .title("Marker in Sydney"));
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

  }

  public boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {


      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.ACCESS_FINE_LOCATION)) {


      } else {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
      }
      return false;
    } else {
      return true;
    }
    // TODO

  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


          if (ContextCompat.checkSelfPermission(this,
                  Manifest.permission.ACCESS_FINE_LOCATION)
                  == PackageManager.PERMISSION_GRANTED) {
          }

        } else {

        }
        return;
      }

    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (checkLocationPermission()) {
      if (ContextCompat.checkSelfPermission(this,
              Manifest.permission.ACCESS_FINE_LOCATION)
              == PackageManager.PERMISSION_GRANTED) {

      }
    }

  }




}
