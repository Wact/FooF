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

package localhost.foof.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import localhost.foof.R;
import localhost.foof.activities.MainActivity;
import localhost.foof.models.Order;
import localhost.foof.network.NetworkHandler;
import localhost.foof.views.adapters.OrderAdapter;

import static localhost.foof.activities.MainActivity.account;

/**
 * Created by ARTEM on 01.05.2017.
 */

/**
 * Класс Фрагмент Заказов показывает ListView-список заказов авторизованного пользователя.
 * Если пользователь не авторизован, ему будет показан Фрагмент Аккаунт
 */
public class OrdersFragment extends android.support.v4.app.Fragment {

    public static ArrayList<Order> orders = new ArrayList<>();
    ListView ordersList;
    JSONArray jsonResult;
    private boolean auth = true;

    public OrdersFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        if(account.isNull("mail")) {
            Toast toast = Toast.makeText(getContext(), R.string.enter_account, Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("Fragment", 0);
            startActivity(intent);
            auth = false;
            return view;
        } else {
            NetworkHandler networkHandler = new NetworkHandler(getContext());
            try {
                networkHandler.execute("getAccount", account.get("mail").toString(), account.get("password").toString());
                account = new JSONObject(networkHandler.get());
            } catch (JSONException e) {
                Toast toast = Toast.makeText(getContext(), R.string.connection, Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                auth = false;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonResult = new JSONArray(account.get("orders").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setOrders();

        final OrderAdapter adapter = new OrderAdapter(getContext(), R.layout.list_orders, orders);
        try {
            ordersList = (ListView) view.findViewById(R.id.ordersList);
            ordersList.setAdapter(adapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return view;
    }

    /**
     * Процедура инициализации списка заказов
     */
    public void setOrders() {
        try {
            orders = new ArrayList<>();
                for (int i = 0; i < jsonResult.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonResult.getJSONObject(i);
                        orders.add(0, new Order(jsonObject.getString("id"), jsonObject.getString("firstName"), jsonObject.getString("secondName"), jsonObject.getString("address")
                                , jsonObject.getString("products"), jsonObject.getString("price"), jsonObject.getString("status"), jsonObject.getString("comment")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        } catch (NullPointerException e) {
            if(auth) {
                Toast toast = Toast.makeText(getContext(), getString(R.string.connection), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
