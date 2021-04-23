package com.lyg.factorysmartsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lyg.factorysmartsystem.ConfigFile.ServerApi;
import com.lyg.factorysmartsystem.ConfigFile.authdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    String token;
    TextView daftar;
    EditText email, password;
    ImageView ImageSignIn;
    private String valid_email, valid_pwd;

    authdata authdataa;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
        InitilizeMail();
        InitilizePwd();

//        ----------- build token ---------------
        token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token Hp :", token);
//        ----------- build token ---------------

        ImageSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                functionLogin();
            }
        });

    }

    public void init(){
        authdataa = new authdata(this);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        daftar = findViewById(R.id.txt_daftar);
        email = findViewById(R.id.EmailText);
        password = findViewById(R.id.pwdText);
        ImageSignIn = findViewById(R.id.ImageSignInOnLogin);
        daftar.setPaintFlags(daftar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void functionLogin(){
//        if (email.getText().toString().isEmpty()){
//            Toast.makeText(Login.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
//        } else if (password.getText().toString().isEmpty()){
//            Toast.makeText(Login.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
//        } else if (password.getText().toString().length() < 1) {
//            Toast.makeText(Login.this, "Password cannot be less than 8 characters!", Toast.LENGTH_SHORT).show();
//        }

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("data").matches("Your account is not active")) {
                        Toast.makeText(Login.this, "Your account is not active !", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject data = jsonObject.getJSONObject("data");
                        authdataa.setdatauser(
                                data.getString("userID"),
                                data.getString("userName"),
                                data.getString("userNIK"),
                                data.getString("userMail"),
                                data.getString("token"),
                                data.getString("CreateBy"),
                                data.getString("CreateDate"),
                                data.getString("LastModifyBy"),
                                data.getString("LastModifyDate")
                        );
                        Intent intent = new Intent(Login.this, MainMenu.class);
                        Toast.makeText(Login.this, data.getString("pesan"), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Login.this, "Error !", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Invalid Email and Password !", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userMail", email.getText().toString().trim());
                params.put("userPassword", password.getText().toString().trim());
                params.put("token", token);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void InitilizeMail() {
        // TODO Auto-generated method stub

        email = findViewById(R.id.EmailText);

        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                Is_Valid_Email(email); // pass your EditText Obj here.
            }

            public void Is_Valid_Email(EditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else if (isEmailValid(edt.getText().toString()) == false) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else {
                    valid_email = edt.getText().toString();
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            } // end of TextWatcher (email)
        } );

    }

    private void InitilizePwd() {
        // TODO Auto-generated method stub

        password = findViewById(R.id.pwdText);

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                Is_Check_Count(password); // pass your EditText Obj here.
            }

            public void Is_Check_Count(EditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Password cannot be less than 8 characters!");
                    valid_pwd = null;
                } else if (edt.getText().toString().length() < 8) {
                    edt.setError("Password cannot be less than 8 characters!");
                    valid_pwd = null;
                } else {
                    valid_pwd = edt.getText().toString();
                }
            }


        } );

    }
}