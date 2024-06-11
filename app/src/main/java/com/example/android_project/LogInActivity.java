package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    private EditText edtLogEmail;
    private EditText edtPass;
    private Button btnLogin;
    private TextView txtSignup;
    private Button btnForgot;
    private String userType;

    private CheckBox chkRemember;
    private TextView textView;
    private static final String LOGIN_URL = "http://192.168.1.23/android_project/login.php";
    private final String TAG = "LogInActivity";
    private MySharedPref mySharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userType = getIntent().getStringExtra("userType");
        mySharedPref = new MySharedPref(LogInActivity.this);


        populate();
        if(userType.equalsIgnoreCase("manager")){
            textView.setVisibility(View.INVISIBLE);
            txtSignup.setVisibility(View.INVISIBLE);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    loginUser();
                }
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                intent.putExtra("userType", userType);
                startActivity(intent);
            }
        });
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();

        String email = mySharedPref.readString("email", "noValue");
        String pass = mySharedPref.readString("pass", "noValue");

        if (!email.equals("noValue")){

            edtLogEmail.setText(email);
            edtPass.setText(pass);
            chkRemember.setChecked(true);
        } else {

            edtLogEmail.setText("");
            edtPass.setText("");
            chkRemember.setChecked(false);
        }


    }

    private void populate() {
        edtLogEmail = findViewById(R.id.edtLogEmail);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);
        btnForgot = findViewById(R.id.btnForgot);
        chkRemember = findViewById(R.id.chkRemember);
        textView = findViewById(R.id.textView);
    }

    private boolean validateInputs() {
        String email = edtLogEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        if (email.isEmpty()) {
            edtLogEmail.setError("E-mail id required");
            return false;
        }
        if (password.isEmpty()) {
            edtPass.setError("Password is required");
            return false;
        }
        return true;
    }

private void loginUser() {
    final String email = edtLogEmail.getText().toString().trim();
    final String pass = edtPass.getText().toString().trim();
    String url = LOGIN_URL + "?email=" + email + "&pass=" + pass;
    mySharedPref = MySharedPref.getInstance(LogInActivity.this);


    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, "Response: " + response.toString());
                    if (response.length() > 0) {
                        try {
                            JSONObject userObject = response.getJSONObject(0);
                            Toast.makeText(LogInActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LogInActivity.this, Profile.class);
                            intent.putExtra("userType", userType);
                            intent.putExtra("user_email" , email);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LogInActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.toString());
                    Toast.makeText(LogInActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
    if (chkRemember.isChecked()){

        mySharedPref.writeString("email", email);
        mySharedPref.writeString("pass", pass);
    }
    else {

        mySharedPref.writeString("email", "noValue");
        mySharedPref.writeString("pass" , "noValue");
    }

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(jsonArrayRequest);
}
}
