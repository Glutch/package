package com.apackage.app.apackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class viewOrders extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_orders);

    Toolbar toolbar = findViewById(R.id.toolbar2);
    setSupportActionBar(toolbar);
  }
}
