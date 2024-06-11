package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtResetPass;
    private EditText edtResetConfirm;
    private Button btnReset;
    private static final String RESET_PASSWORD_URL = "http://192.168.1.23/android_project/reset_password.php";
    private static final String TAG = "ResetPasswordActivity";
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        populate();

        userEmail = getIntent().getStringExtra("user_email");

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()) {
                    resetPassword();
                }
            }
        });
    }

    private void populate() {
        edtResetPass = findViewById(R.id.edtResetPass);
        edtResetConfirm = findViewById(R.id.edtResetConfirm);
        btnReset = findViewById(R.id.btnReset);
    }

    private boolean validateInputs(){

        String password = edtResetPass.getText().toString().trim();
        String confirmation = edtResetConfirm.getText().toString().trim();

        if (password.replaceAll(" ", "").isEmpty()) {
            edtResetPass.setError("Please enter your password");
            edtResetPass.requestFocus();
            return false;
        } else if (password.length() < 5) {
            edtResetPass.setError("Password must contain at least 5 characters");
            edtResetPass.requestFocus();
            return false;
        } else if (!password.matches(".*[a-zA-Z]+.*")) {
            edtResetPass.setError("Password must contain at least 1 character");
            edtResetPass.requestFocus();
            return false;
        } else if (!password.matches(".*[0-9]+.*")) {
            edtResetPass.setError("Password must contain at least 1 number");
            edtResetPass.requestFocus();
            return false;
        } else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            edtResetPass.setError("Password must contain at least 1 special character");
            edtResetPass.requestFocus();
            return false;
        }

        if (!edtResetPass.equals(edtResetConfirm)) {
            edtResetPass.setError("Passwords do not match");
            edtResetConfirm.requestFocus();
            return false;

        }

        return true;
    }

    private void resetPassword() {
        final String newPassword = edtResetPass.getText().toString().trim();
        final String confirmPassword = edtResetConfirm.getText().toString().trim();

        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User E-mail is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RESET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
                        Toast.makeText(ResetPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG,error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("new_password", newPassword);
                params.put("user_email", userEmail);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
