package com.apackage.app.apackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apackage.app.apackage.database.Order;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by mrx on 2017-11-19.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Order> orderArrayList;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, ArrayList<Order> orderArrayList){
        this.orderArrayList = orderArrayList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderArrayList.get(position).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView==null)
            view = inflater.inflate(R.layout.order_list_layout, parent, false);
        else
            view = convertView;

        TextView textOrderId = view.findViewById(R.id.textOrderId);
        TextView textOrderDate = view.findViewById(R.id.textOrderDate);
        textOrderId.setText(""+orderArrayList.get(position).clientId);
        textOrderDate.setText(orderArrayList.get(position).deliveryTime);

        return view;
    }
}
