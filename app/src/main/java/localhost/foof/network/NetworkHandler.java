package localhost.foof.network;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import localhost.foof.models.Account;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс создания синхронных Retrofit-запросов к серверу
 */
public class NetworkHandler extends AsyncTask<String, Void, String> {
    private Context context;
    String type = "";

    public NetworkHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        String baseUrl = "http://foofcontrol.me.pn/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);

        type = params[0];
        if(type.equals("addOrder")) {
            String dataAboutUser = params[1];
            String products = params[2];
            Call<String> call = service.addOrder(dataAboutUser, products);
            try {
                return call.execute().body();
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        if(type.equals("getAccount")) {
            String login = params[1];
            String password = params[2];
            Call<Account> call = service.getAccount(login, password);
            try {
                Gson gson = new Gson();
                Account account = call.execute().body();
                return gson.toJson(account);
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        if(type.equals("setAccount")) {
            String login = params[1];
            String password = params[2];
            String dataAboutAccount = params[3];
            Call<String> call = service.setAccount(login, password, dataAboutAccount);
            try {
                return call.execute().body();
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
