package com.la4zen.lolzapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.la4zen.lolzapp.R;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity {

    Button btnSignIn;
    EditText loginEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        btnSignIn = findViewById(R.id.button);
        loginEditText = findViewById(R.id.editLogin);
        passwordEditText = findViewById(R.id.editPassword);
        btnSignIn.setOnClickListener(new View.OnClickListener()
            { //
                @Override
                public void onClick (View v){
                    new logIn().execute();
                    boolean success = true;/*s
                        signIn(loginEditText.getText().toString(), passwordEditText.getText().toString()); */
                    if (success) {
                        //LoginActivity.super.finish();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не удалось войти.",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
    }
    public class logIn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                Connection.Response res = Jsoup.connect("https://lolz.guru/login").method(Connection.Method.GET).followRedirects(true).timeout(10000).execute();
                Document doc = Jsoup.connect("https://lolz.guru/login/login").data(
                        "login", loginEditText.getText().toString(),
                        "password", passwordEditText.getText().toString(),
                        "stopfuckingbrute1337", "1",
                        "cookie_check","1"
                ).header("cookie","df_id=960319fc0b468f741693392b329868f7; xf_session=3fdf90ba8877804d5cc05423dfca9629; G_ENABLED_IDPS=google")
                        .followRedirects(true).post();
                System.out.println(doc);
            } catch (IOException e) { System.out.println(e);}
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}