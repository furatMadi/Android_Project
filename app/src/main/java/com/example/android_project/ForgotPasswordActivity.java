package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {


    private EditText edtResetEmail;
    private Button btnSearch;
    private static final String VERIFY_USER_URL = "http://192.168.1.23/android_project/verify_user.php";
    private static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        populate();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void populate() {

        edtResetEmail = findViewById(R.id.edtResetEmail);
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void verifyUser() {
        final String email = edtResetEmail.getText().toString().trim();

        Map<String, String> params = new HashMap<>();
        params.put("email", email);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VERIFY_USER_URL, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if ("User verified".equals(status)) {
                                Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                                intent.putExtra("user_email",email);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Verification failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPasswordActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
                        Toast.makeText(ForgotPasswordActivity.this, "An error occurred: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}