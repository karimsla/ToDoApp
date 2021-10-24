package com.example.todoapp.Acitvities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.todoapp.Database.AppDatabase;
import com.example.todoapp.Database.DAO;
import com.example.todoapp.Model.User;
import com.example.todoapp.R;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.todoapp.Tools.Utils;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {


    //Room Database
    private DAO dao;

    //Login Authentication
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //Components
    private TextInputEditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dao = AppDatabase.getDb(this).getDAO();
        preferences = getSharedPreferences(Utils.APP_NAME, MODE_PRIVATE);

        if (preferences.getBoolean(Utils.loginControlKey, false)) {
            User user = dao.loginControl(preferences.getString(Utils.loginUserNameKey, ""));
            openMainActivity(user);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        edtUsername = findViewById(R.id.etUserName);
        edtPassword = findViewById(R.id.etPassword);


        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                if (TextUtils.isEmpty(edtUsername.getText().toString().trim())) {
                    edtUsername.setError(getString(R.string.loginUserNameError));
                    isError = true;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    edtPassword.setError(getString(R.string.loginPasswordError));
                    isError = true;
                }
                if (isError) return;
                User user = dao.login(edtUsername.getText().toString(), edtPassword.getText().toString());
                if (user != null) {
                    createLoginSession(edtUsername.getText().toString(), edtPassword.getText().toString());
                    openMainActivity(user);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.loginErrorMessage), Toast.LENGTH_SHORT).show();

                }

            }
        });

        findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signUpIntent);
            }
        });

    }

    private void openMainActivity(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("user", new Gson().toJson(user));
        Intent loginSuccessIntent = new Intent(LoginActivity.this, MainActivity.class);
        loginSuccessIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        loginSuccessIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginSuccessIntent.putExtras(bundle);
        startActivity(loginSuccessIntent);
        finish();
    }


    public void createLoginSession(String userName, String password) {
        editor = preferences.edit();

        editor.putBoolean(Utils.loginControlKey, true);


        editor.putString(Utils.loginUserNameKey, userName);


        editor.apply();
    }
}