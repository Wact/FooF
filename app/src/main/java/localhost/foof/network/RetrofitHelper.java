package localhost.foof.network;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ListView;

import java.util.ArrayList;

import localhost.foof.views.adapters.ProductAdapter;
import localhost.foof.R;
import localhost.foof.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ARTEM on 27.05.2017.
 */

/**
 * Класс создания асинхронных Retrofit-запросов к серверу
 */
public class RetrofitHelper extends Application {
    Context context;

    private ProgressDialog dialog;
    String baseUrl = "http://foofcontrol.me.pn/";

    public RetrofitHelper(Context ctx) {
        context = ctx;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    APIService service = retrofit.create(APIService.class);

    public void getProducts(ProgressDialog dialog, final ListView productList) {
        this.dialog = dialog;

        Call<ArrayList<Product>> call = service.getProducts("");

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                showDialog();
                ProductAdapter adapter = new ProductAdapter(context, R.layout.list_item, response.body());
                productList.setAdapter(adapter);
                hideDialog();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                t.printStackTrace();
                hideDialog();
            }
        });
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
