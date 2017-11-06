package localhost.foof.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import localhost.foof.R;
import localhost.foof.activities.MainActivity;
import localhost.foof.activities.RegistrationActivity;
import localhost.foof.network.NetworkHandler;

import static android.os.ParcelFileDescriptor.MODE_APPEND;
import static localhost.foof.activities.MainActivity.account;

/**
 * Класс Фрагмент Аккаунт показывает форму для авторизации,
 * либо личный кабинет авторизованного пользователя
 */
public class AccountFragment extends Fragment{
    TextView bonus;
    EditText etLogin, etPassword;
    Button log, register, remindPassword, orders, exit;

    public AccountFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if(account.isNull("mail")) {
            View view = inflater.inflate(R.layout.fragment_accountunknown, container, false);
            etLogin = (EditText) view.findViewById(R.id.etLogin);
            etPassword = (EditText) view.findViewById(R.id.etPassword);
            log = (Button) view.findViewById(R.id.btnLogin);
            register = (Button) view.findViewById(R.id.btnRegister);
            //remindPassword = (Button) view.findViewById(R.id.btnRemindPassword);

            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String password = etPassword.getText().toString();
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(password.getBytes());
                        password = RegistrationActivity.bytesToHexString(md.digest());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    NetworkHandler networkHandler = new NetworkHandler(getContext());
                    try {
                        networkHandler.execute("getAccount", etLogin.getText().toString(), password);
                        String result = networkHandler.get();
                        if(result != null)
                            account = new JSONObject(result);
                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    Toast toast;
                    if(account.isNull("mail")) {
                        intent.putExtra("Fragment",0);
                        toast = Toast.makeText(getContext(), R.string.wrong_mail_pass, Toast.LENGTH_LONG);
                    } else {
                        toast = Toast.makeText(getContext(),R.string.meeting, Toast.LENGTH_LONG);
                        writeFile();
                        /*try {
                            FileOutputStream fos = getContext().openFileOutput("content.txt", MODE_APPEND);
                            byte[] bytes = account.toString().getBytes();
                            fos.write(bytes);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                    toast.show();
                    startActivity(intent);
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), RegistrationActivity.class);
                    startActivity(intent);
                }
            });

            /*remindPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_accountknown, container, false);
            bonus = (TextView) view.findViewById(R.id.tvBonus);
            exit = (Button) view.findViewById(R.id.btnExit);

            NetworkHandler networkHandler = new NetworkHandler(getContext());
            try {
                networkHandler.execute("getAccount", account.get("mail").toString(), account.get("password").toString());
                account = new JSONObject(networkHandler.get());
                bonus.setText(getString(R.string.bonus)+account.get("bonus"));
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

              exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    account = new JSONObject();
                    writeFile();
                    /*try {
                        FileOutputStream fos = getContext().openFileOutput("content.txt", MODE_APPEND);
                        byte[] bytes = account.toString().getBytes();
                        fos.write(bytes);
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    public void writeFile() {
        try {
            FileOutputStream fos = getContext().openFileOutput("content.txt", MODE_APPEND);
            byte[] bytes = account.toString().getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
