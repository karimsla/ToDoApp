package com.esprit.todoapp.Acitvities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.esprit.todoapp.Database.AppDatabase;
import com.esprit.todoapp.Database.DAO;
import com.esprit.todoapp.Model.User;
import com.esprit.todoapp.databinding.ActivitySignupBinding;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.esprit.todoapp.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private DAO dao;

    private EditText etUserName, etPassword, etMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dao = AppDatabase.getDb(this).getDAO();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etMail = findViewById(R.id.etMail);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                boolean isError = false;

                if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {
                    etUserName.setError(getString(R.string.signUpUserNameError));
                    isError = true;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                    etPassword.setError(getString(R.string.signUpPasswordError));
                    isError = true;
                }
                if (TextUtils.isEmpty(etMail.getText().toString().trim())) {
                    etMail.setError(getString(R.string.signUpEmailError));
                    isError = true;
                }
                if (isError) return;

                if (dao.signUpControl(etUserName.getText().toString(), etMail.getText().toString()) == 0) {
                    User user = new User();
                    user.setUserMail(etMail.getText().toString());
                    user.setUserName(etUserName.getText().toString());
                    user.setUserPassword(etPassword.getText().toString());
                    dao.insertUser(user);
                    Toast.makeText(this, getString(R.string.signUpSuccessMessage), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.signUpErrorMessage), Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
}