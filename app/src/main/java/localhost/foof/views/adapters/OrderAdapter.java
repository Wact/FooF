/*
 * Copyright (c) 2017.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package localhost.foof.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import localhost.foof.R;
import localhost.foof.activities.OrderActivity;
import localhost.foof.models.Order;

/**
 * Created by ARTEM on 02.05.2017.
 */

/**
 * Класс Адаптер Продуктов, определяющий отображение продукта
 */
public class OrderAdapter extends ArrayAdapter<Order> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Order> orders;

    public OrderAdapter(Context context, int resource, ArrayList<Order> orders) {
        super(context, resource, orders);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        
        final Order order = orders.get(position);
        String stroke = " #: "+order.getId()+" "+getContext().getString(R.string.secondName)+order.getSecondName()+"\n"+" "+getContext().getString(R.string.cost)+order.getPrice();
        switch(order.getStatus()) {
            case "0":
                stroke+=" \n "+getContext().getString(R.string.statusExecuted);
                viewHolder.orderView.setBackgroundResource(R.color.colorExecutedOrder);
                    break;
            case "1":
                stroke+=" \n "+getContext().getString(R.string.statusActive);
                viewHolder.orderView.setBackgroundResource(R.color.colorActiveOrder);
                order.comment = "";
                    break;
        }
        viewHolder.orderText.setText(stroke);

        viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("id", order.id);
                intent.putExtra("firstName", order.firstName);
                intent.putExtra("secondName", order.secondName);
                intent.putExtra("address", order.address);
                intent.putExtra("products", order.products);
                intent.putExtra("price", order.price);
                intent.putExtra("status", order.status);
                intent.putExtra("comment", order.comment);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * Класс формы для отображения заказа
     */
    private class ViewHolder {
        final LinearLayout orderView;
        final TextView orderText;

        ViewHolder(View view) {
            orderView = (LinearLayout) view.findViewById(R.id.orderView);
            orderText = (TextView) view.findViewById(R.id.orderText);
        }
    }
}
