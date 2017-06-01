package localhost.foof.network;

import java.util.ArrayList;

import localhost.foof.models.Account;
import localhost.foof.models.Product;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Retrofit интерфейс для взаимодействия с сервером
 */
public interface APIService {
    @FormUrlEncoded
    @POST("php-handlers/checkList.php")
    Call<ArrayList<Product>> getProducts(@Field("s") String s);

    @FormUrlEncoded
    @POST("php-handlers/addOrder.php")
    Call<String> addOrder(@Field("dataAboutUser") String dataAboutUser, @Field("products") String products);

    @FormUrlEncoded
    @POST("php-handlers/getAccount.php")
    Call<Account> getAccount(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("php-handlers/setAccount.php")
    Call<String> setAccount(@Field("login") String login, @Field("password") String password, @Field("dataAboutAccount") String dataAboutAccount);
}
