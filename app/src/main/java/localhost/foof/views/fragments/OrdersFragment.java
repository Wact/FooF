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
