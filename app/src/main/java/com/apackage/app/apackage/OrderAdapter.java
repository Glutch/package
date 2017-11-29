package com.apackage.app.apackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apackage.app.apackage.database.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * An Adapter that handles Orders to show in ListView.
 */
public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;
    private LayoutInflater inflater;

    /**
     * Constructor that sets context, orderList and inflater.
     *
     * @param context
     * @param orderList
     */
    public OrderAdapter(Context context, List<Order> orderList) {
        if (orderList == null)
            this.orderList = new ArrayList<>();
        else
            this.orderList = orderList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderList.get(position).orderId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null)
            view = inflater.inflate(R.layout.order_list_layout, parent, false);
        else
            view = convertView;

        TextView textOrderId = view.findViewById(R.id.textOrderId);
        TextView textOrderDate = view.findViewById(R.id.textOrderDate);
        textOrderId.setText("#" + orderList.get(position).orderId);
        textOrderDate.setText(orderList.get(position).deliveryTime);

        return view;
    }

    /**
     * Inserts a new orderList and notifies the ListView.
     *
     * @param orderList - new orderList
     */
    public void setOrderList(List<Order> orderList) {
        if (orderList == null)
            this.orderList = new ArrayList<>();
        else
            this.orderList = orderList;
        notifyDataSetChanged();
    }

    /**
     * Removes a specific order from orderList and notifies the ListView.
     *
     * @param order - specific order to remove.
     */
    public void removeOrder(Order order) {
        orderList.remove(order);
        notifyDataSetChanged();
    }
}
