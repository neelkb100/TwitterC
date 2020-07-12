package com.example.twitterc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        edtUsername = findViewById(R.id.txtViewUserNameLoginT);
        edtPassword = findViewById(R.id.txtViewPasswordLoginT);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnLogin);
                }
                return false;
            }
        });

        btnLogin = findViewById(R.id.btnLoginT);
        btnLogin.setOnClickListener(this);

        btnSignUp = findViewById(R.id.btnSignUpT);
        btnSignUp.setOnClickListener(this);


        if(ParseUser.getCurrentUser() != null){

            //ParseUser.logOut();

            transactionToTwitterUsersActivity();
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnLoginT:

                ProgressDialog progressDialog = new ProgressDialog(this);

                progressDialog.setMessage("Login  " + edtUsername.getText().toString());
                progressDialog.show();

                loginMethod();

                progressDialog.dismiss();

                break;

            case R.id.btnSignUpT:

                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                break;

        }
    }

    private void loginMethod() {


        if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
            FancyToast.makeText(LoginActivity.this, "Username, Password is required",
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        } else {

            ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if(user != null && e == null){

                        FancyToast.makeText(LoginActivity.this,user.get("username")+" Login Successfully",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        transactionToTwitterUsersActivity();

                    } else{
                        FancyToast.makeText(LoginActivity.this,"Error "+ e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }

                }
            });

        }

    }

    private void transactionToTwitterUsersActivity() {

        Intent intent = new Intent(LoginActivity.this,TwittersUsers.class);
        startActivity(intent);
        finish();

    }

    public void loginRootLayoutTapped(View V){

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

        catch (Exception e){

            e.printStackTrace();
        }

    }

}