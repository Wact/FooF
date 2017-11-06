package localhost.foof.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import localhost.foof.network.NetworkHandler;
import localhost.foof.R;

import static localhost.foof.activities.MainActivity.account;

/**
 * Активность Регистрации отображается при нажатии кнопки 'Зарегистрироваться' в Фрагменте Аккаунт
 */
public class RegistrationActivity extends AppCompatActivity {
    LinearLayout firstName;
    LinearLayout secondName;
    LinearLayout phone;
    LinearLayout mail;
    LinearLayout password;

    EditText etFirstName;
    EditText etSecondName;
    EditText etPhone;
    EditText etMail;
    EditText etPassword;

    ImageView ivFirstName;
    ImageView ivSecondName;
    ImageView ivPhone;
    ImageView ivMail;
    ImageView ivPassword;

    Button btnConfirm;

    /**
     * Массив, содержащий информацию о корректности полей
     */
    ArrayList<Boolean> checkCorr = new ArrayList<>();
    private boolean mailError;
    private boolean passwordError;
    private  boolean phoneError;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        firstName = (LinearLayout) findViewById(R.id.firstName);
        secondName = (LinearLayout) findViewById(R.id.secondName);
        phone = (LinearLayout) findViewById(R.id.phone);
        mail = (LinearLayout) findViewById(R.id.mail);
        password = (LinearLayout) findViewById(R.id.password);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etSecondName = (EditText) findViewById(R.id.etSecondName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etMail = (EditText) findViewById(R.id.etMail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone.setTag("PHONE");
        etMail.setTag("MAIL");
        etPassword.setTag("PASSWORD");

        ivFirstName = (ImageView) findViewById(R.id.ivFirstName);
        ivSecondName = (ImageView) findViewById(R.id.ivSecondName);
        ivPhone = (ImageView) findViewById(R.id.ivPhone);
        ivMail = (ImageView) findViewById(R.id.ivMail);
        ivPassword = (ImageView) findViewById(R.id.ivPassword);

        btnConfirm = (Button) findViewById(R.id.confirm);

        for (int i = 0; i < 5; i++) {
            checkCorr.add(i, false);
        }
        checkOfCorrectnessOfFields(etFirstName, ivFirstName, 0);
        checkOfCorrectnessOfFields(etSecondName, ivSecondName, 1);
        checkOfCorrectnessOfFields(etPhone, ivPhone, 2);
        checkOfCorrectnessOfFields(etMail, ivMail, 3);
        checkOfCorrectnessOfFields(etPassword, ivPassword, 4);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFirstName.clearFocus();
                etSecondName.clearFocus();
                etPhone.clearFocus();
                etMail.clearFocus();
                etPassword.clearFocus();
                boolean corr = false;
                String error = "";
                try {
                    for (int i = 0; i < 5; i++) {
                        if ((!checkCorr.get(i) && checkCorr.get(i) != null) || (checkCorr.get(i) == null)) {
                            corr = false;
                            error = getString(R.string.fillFields);
                            break;
                        } else {
                            corr = true;
                        }
                    }
                    if(phoneError) {
                        corr = false;
                        error = getString(R.string.wrongPhone);
                        etPhone.requestFocus();
                    }
                    if(mailError){
                        corr = false;
                        error = getString(R.string.wrongMail);
                        etMail.requestFocus();
                    }
                    if(passwordError) {
                        corr = false;
                        error = getString(R.string.wrongPassword);
                        etPassword.requestFocus();
                    }
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    corr = false;
                }
                if (corr) {
                    String firstName = etFirstName.getText().toString();
                    String secondName = etSecondName.getText().toString();
                    String phone = etPhone.getText().toString();
                    String mail = etMail.getText().toString();
                    String password = etPassword.getText().toString();
                    JSONObject jsonDataAboutAccount = new JSONObject();
                    try {
                        jsonDataAboutAccount.put("firstName", firstName);
                        jsonDataAboutAccount.put("secondName", secondName);
                        jsonDataAboutAccount.put("phone", phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(password.getBytes());
                        password = bytesToHexString(md.digest());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    NetworkHandler networkHandler = new NetworkHandler(getApplicationContext());
                    networkHandler.execute("setAccount", mail, password, jsonDataAboutAccount.toString()).toString();
                    String result = null;
                    try {
                        result = networkHandler.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(Objects.equals(result, "Login is occupied")) {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_occupied), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        try {
                            account.put("mail",mail);
                            account.put("password",password);
                            account.put("dataAboutAccount",jsonDataAboutAccount);
                            account.put("orders","[]");
                            account.put("bonus","0");
                            writeFile();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.meeting, Toast.LENGTH_LONG);
                        toast.show();
                        startActivity(intent);
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                if (!b) {
                    if (!Objects.equals(et.getText().toString(), "")) {
                        iv.setImageResource(R.drawable.fingerup);
                        checkCorr.set(n, true);
                    } else {
                        iv.setImageResource(R.drawable.fingerdown);
                        checkCorr.set(n, false);
                    }
                    if (et.getTag() == "MAIL") {
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
                    if (et.getTag() == "PASSWORD") {
                        if (et.getText().toString().length() > 8) {
                            iv.setImageResource(R.drawable.fingerup);
                            checkCorr.set(n, true);
                            passwordError = false;
                        } else {
                            iv.setImageResource(R.drawable.fingerdown);
                            checkCorr.set(n, false);
                            passwordError = true;
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

    /**
     * Функция перевода хеша пароля из массива байтов в строку
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Registration Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void writeFile() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("content.txt", MODE_APPEND);
            byte[] bytes = account.toString().getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
