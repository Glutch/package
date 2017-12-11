package com.apackage.app.apackage;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity that presents a specific order.
 */
public class ViewSpecificOrder extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;

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
        if (order.delivered) {
            deliveredBtn.setVisibility(View.GONE);
        }

    }

    /**
     * When button delivered clicked try to send sms and change order in database to delivered.
     * @param view
     */
    public void onClickDelivered(View view) {
        if (latLng != null) {
            order.latLng = latLng;
            dbHelper.setLatLang(order);
        }

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
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        MarkerOptions markerOptions = new MarkerOptions();
        if (order.latLng != null) {
            markerOptions.position(order.latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(order.latLng, 11));
        }


    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position

            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mGoogleMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000); //15 seconds
        mLocationRequest.setFastestInterval(13000); //13 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());



        order.latLng = latLng;
        dbHelper.setLatLang(order);
    }
}