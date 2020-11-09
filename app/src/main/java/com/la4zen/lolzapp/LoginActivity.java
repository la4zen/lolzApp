package com.la4zen.lolzapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.la4zen.lolzapp.R;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;


public class LoginActivity extends AppCompatActivity {

    Button btnSignIn;
    EditText loginEditText, passwordEditText;
    TextView errorTextView;
    String cookie;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        btnSignIn = findViewById(R.id.button);
        loginEditText = findViewById(R.id.editLogin);
        passwordEditText = findViewById(R.id.editPassword);
        errorTextView = findViewById(R.id.errorTextView);
        btnSignIn.setOnClickListener(new View.OnClickListener()
            { //
                @Override
                public void onClick (View v){
                    btnSignIn.setEnabled(false);
                    errorTextView.setText("Вход...");
                    new logIn().execute();
                }
            });
    }
    public class logIn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                errorTextView.setText("Отправка get-запроса...");
                Connection.Response response1 = Jsoup.connect("https://lolz.guru/login").method(Connection.Method.GET).followRedirects(true).timeout(10000).execute();
                errorTextView.setText("Отправка post-запроса с параметрами...");
                Connection.Response response2 = Jsoup.connect("https://lolz.guru/login/login").data(
                        "login", loginEditText.getText().toString(),
                        "password", passwordEditText.getText().toString(),
                        "stopfuckingbrute1337", "1",
                        "cookie_check","1"
                ).header("cookie","df_id=960319fc0b468f741693392b329868f7; G_ENABLED_IDPS=google")
                 .followRedirects(true).method(Connection.Method.POST).execute();
                errorTextView.setText("Обработка...");
                Elements result = response2.parse().select("div.loginForm--errors");
                if (result.isEmpty()) {
                    mSettings = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString("cookie", response2.headers().get("cookie"));
                    editor.commit();
                    System.out.println(mSettings.getString("cookie", null));
                    return null;
                } else {
                    return result.get(0).text();
                }
            } catch (IOException e) { System.out.println(e); return "Произошла неизвестная ошибка."; } // Нужен фикс. Пока только так.
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                setResult(RESULT_OK);
                LoginActivity.super.finish();
            } else {
                errorTextView.setText(result);
                btnSignIn.setEnabled(true);
            }
        }
    }
}