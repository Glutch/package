package com.apackage.app.apackage;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;
import com.apackage.app.apackage.settings.SettingsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity that presents a specific order.
 */
public class ViewSpecificOrder extends AppCompatActivity implements OnMapReadyCallback {

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        long orderId = intent.getLongExtra("ORDERID", -1);

        dbHelper = new DBHelper(this);
        order = dbHelper.getByOrderId(orderId);

        clientID = findViewById(R.id.clientID);
        clientID.setText("" + order.price);
        ordernumber = findViewById(R.id.ordernumber);
        ordernumber.setText("" + order.orderId);
        firstname = findViewById(R.id.firstname);
        firstname.setText(order.clientName);
        lastname = findViewById(R.id.lastname);
        lastname.setText(order.clientLastname);
        address = findViewById(R.id.address);
        address.setText(order.address);
        postcode = findViewById(R.id.postcode);
        postcode.setText("" + order.postalCode);
        city = findViewById(R.id.city);
        city.setText(order.postalTown);
        weight = findViewById(R.id.weight);
        weight.setText("" + order.weight);
        price = findViewById(R.id.price);
        price.setText("" + order.price);
        deliverydate = findViewById(R.id.deliverydate);
        deliverydate.setText(order.deliveryTime);
        deliverystatus = findViewById(R.id.deliverystatus);
        if (order.delivered)
            deliverystatus.setText(R.string.delivered);
        else
            deliverystatus.setText(R.string.not_delivered);
        deliveredBtn = findViewById(R.id.button2);
        if (order.delivered)
            deliveredBtn.setVisibility(View.GONE);
    }

    /**
     * When button delivered clicked try to send sms and change order in database to delivered.
     * @param view
     */
    public void onClickDelivered(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            sendSMS();
        } else {
            Toast toast = new Toast(this);
            toast.makeText(this, R.string.needs_permission_toast, Toast.LENGTH_SHORT).show();
        }


        dbHelper.markAsDelivered(order);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void sendSMS() {

        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String nr = sharedPreferences.getString(SettingsActivity.KEY_PHONENUMBER, "");
            String message = getString(R.string.order) + " " + order.orderId + " " + getString(R.string.delivered) + ".";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(nr, null, message, null, null);

        } catch (IllegalArgumentException e) {
            Toast toast = new Toast(this);
            toast.makeText(this, R.string.wrong_phonenumber_toast, Toast.LENGTH_SHORT).show();
        }
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