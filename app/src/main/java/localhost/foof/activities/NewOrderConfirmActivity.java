package localhost.foof.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import localhost.foof.network.NetworkHandler;
import localhost.foof.views.fragments.NewOrderFragment;
import localhost.foof.models.Product;
import localhost.foof.R;

import static localhost.foof.activities.MainActivity.account;

/**
 * Created by ARTEM on 22.03.2017.
 */

/**
 * Активность Регистрации отображается при нажатии кнопки 'Корзина' в toolbar'е
 *      или по нажатию в левом боковом меню кнопки 'Заказать'
 */
public class NewOrderConfirmActivity extends Activity {
    LinearLayout firstName;
    LinearLayout secondName;
    LinearLayout phone;
    LinearLayout mail;
    LinearLayout address;

    EditText etFirstName;
    EditText etSecondName;
    EditText etPhone;
    EditText etMail;
    EditText etAddress;

    ImageView ivFirstName;
    ImageView ivSecondName;
    ImageView ivPhone;
    ImageView ivMail;
    ImageView ivAddress;

    Button btnConfirm;

    /**
     * Массив, содержащий информацию о корректности полей
     */
    ArrayList<Boolean> checkCorr = new ArrayList<>();
    ArrayList<Product> products;

    private boolean mailError;
    private boolean phoneError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworderconfirm);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        firstName = (LinearLayout) findViewById(R.id.firstName);
        secondName = (LinearLayout) findViewById(R.id.secondName);
        phone = (LinearLayout) findViewById(R.id.phone);
        mail = (LinearLayout) findViewById(R.id.mail);
        address = (LinearLayout) findViewById(R.id.address);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etSecondName = (EditText) findViewById(R.id.etSecondName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etMail = (EditText) findViewById(R.id.etMail);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone.setTag("PHONE");
        etMail.setTag("MAIL");

        ivFirstName = (ImageView) findViewById(R.id.ivFirstName);
        ivSecondName = (ImageView) findViewById(R.id.ivSecondName);
        ivPhone = (ImageView) findViewById(R.id.ivPhone);
        ivMail = (ImageView) findViewById(R.id.ivMail);
        ivAddress = (ImageView) findViewById(R.id.ivAddress);

        btnConfirm = (Button) findViewById(R.id.confirm);

        for(int i = 0; i < 5; i++) {
            checkCorr.add(i, false);
        }

        checkOfCorrectnessOfFields(etFirstName, ivFirstName, 0);
        checkOfCorrectnessOfFields(etSecondName, ivSecondName, 1);
        checkOfCorrectnessOfFields(etPhone, ivPhone, 2);
        checkOfCorrectnessOfFields(etMail, ivMail, 3);
        checkOfCorrectnessOfFields(etAddress, ivAddress, 4);

        products = NewOrderFragment.postProducts;

        if(products.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.makeOrder), Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        if(!account.isNull("mail")) {
            JSONObject dataAboutAccount;
            try {
                dataAboutAccount = new JSONObject(account.get("dataAboutAccount").toString());
                etFirstName.setText(dataAboutAccount.get("firstName").toString());
                etSecondName.setText(dataAboutAccount.get("secondName").toString());
                etPhone.setText(dataAboutAccount.get("phone").toString());
                etMail.setText(account.get("mail").toString());
                checkCorr.set(0, true);
                checkCorr.set(1, true);
                checkCorr.set(2, true);
                checkCorr.set(3, true);
                ivFirstName.setImageResource(R.drawable.fingerup);
                ivSecondName.setImageResource(R.drawable.fingerup);
                ivPhone.setImageResource(R.drawable.fingerup);
                ivMail.setImageResource(R.drawable.fingerup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFirstName.clearFocus();
                etSecondName.clearFocus();
                etPhone.clearFocus();
                etMail.clearFocus();
                etAddress.clearFocus();
                boolean corr = false;
                String error = "";
                try {
                    for (int i = 0; i < 5; i++) {
                        if((!checkCorr.get(i) && checkCorr.get(i)!=null) || (checkCorr.get(i)==null)) {
                            corr = false;
                            error = getString(R.string.fillFields);
                            break;
                        } else {
                            corr = true;
                        }
                    }
                    if(mailError) {
                        corr = false;
                        error = getString(R.string.wrongMail);
                        etMail.requestFocus();
                    }
                    if(phoneError) {
                        corr = false;
                        error = getString(R.string.wrongPhone);
                        etPhone.requestFocus();
                    }
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    corr = false;
                }
                if(corr) {
                    String firstName = etFirstName.getText().toString();
                    String secondName = etSecondName.getText().toString();
                    String phone = etPhone.getText().toString();
                    String mail = etMail.getText().toString();
                    String address = etAddress.getText().toString();
                    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    JSONArray jsonResponse = new JSONArray();
                    JSONObject jsonDataAboutUser = new JSONObject();
                    try {
                        jsonDataAboutUser.put("firstName", firstName);
                        jsonDataAboutUser.put("secondName", secondName);
                        jsonDataAboutUser.put("phone", phone);
                        jsonDataAboutUser.put("mail", mail);
                        jsonDataAboutUser.put("address", address);
                        jsonDataAboutUser.put("androidId", androidId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Double generalPrice = 0.0;
                    for (int i = 0; i < products.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            Product now = products.get(i);
                            jsonObject.put("product", now.name + " x" + now.count);
                            jsonObject.put("price", now.count * Double.valueOf(now.price));
                        /*generalPrice+=jsonObject.getDouble("price");
                        System.out.println(generalPrice);*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonResponse.put(jsonObject);
                    }
                    //System.out.println(jsonResponse.toString());
                    NetworkHandler networkHandler = new NetworkHandler(getApplicationContext());
                    networkHandler.execute("addOrder", jsonDataAboutUser.toString(), jsonResponse.toString());
                    NewOrderFragment.products = new ArrayList<>();
                    NewOrderFragment.postProducts = new ArrayList<>();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    /**
     * Процедура проверки полей формы регистрации на корректность
     * @param et - EditText, данные в котором проверяется на корректность
     * @param iv - ImageView, в который выводится картинка, соответствующая
     *           состоянию корректности (True или False)
     * @param n - позиция в массиве checkCorr, соответствующая номеру EditText
     */
    public void checkOfCorrectnessOfFields(final EditText et, final ImageView iv, final int n) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    if(!Objects.equals(et.getText().toString(), "")) {
                        iv.setImageResource(R.drawable.fingerup);
                        checkCorr.set(n, true);
                    } else {
                        iv.setImageResource(R.drawable.fingerdown);
                        checkCorr.set(n, false);
                    }
                    if(et.getTag()=="MAIL") {
                        if(Patterns.EMAIL_ADDRESS.matcher(et.getText().toString()).matches()) {
                            iv.setImageResource(R.drawable.fingerup);
                            checkCorr.set(n, true);
                            mailError = false;
                        } else {
                            iv.setImageResource(R.drawable.fingerdown);
                            checkCorr.set(n, false);
                            mailError = true;
                        }
                    }
                    if(et.getTag() == "PHONE") {
                        if (et.getText().toString().length() > 7) {
                            iv.setImageResource(R.drawable.fingerup);
                            checkCorr.set(n, true);
                            phoneError = false;
                        } else {
                            iv.setImageResource(R.drawable.fingerdown);
                            checkCorr.set(n, false);
                            phoneError = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        products.clear();
        finish();
    }
}
