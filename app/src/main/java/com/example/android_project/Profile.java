package com.example.android_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    private TextView txtEmail;
    private EditText edtProfileFirstName;
    private EditText edtProfileLastName;
    private EditText edtProfilePass;
    private EditText edtProfileConfirm;
    private EditText edtProfileCountry;
    private EditText edtProfileCity;
    private EditText edtProfilePhone;
    private TextView txtProfileCode;
    private ImageView imgProfileFlag;
    private Button btnSaveProfile;
    private boolean changePass = false;
    private ImageView imgProfile;
    private ImageView imgChangeProfile;
    private boolean isImageChanged = false;
    private String userEmail;
    private Uri profileImageUri;
    private TextView txtLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        populate();


        userEmail = getIntent().getStringExtra("user_email");
        txtEmail.setText(userEmail);

        fetchUserData(userEmail);

        imgChangeProfile.setOnClickListener(v -> pickImageFromGallery());

        btnSaveProfile.setOnClickListener(v -> saveProfile());
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populate() {
        txtEmail = findViewById(R.id.txtEmail);
        edtProfileFirstName = findViewById(R.id.edtProfileFirstName);
        edtProfileLastName = findViewById(R.id.edtProfileLastName);
        edtProfilePass = findViewById(R.id.edtProfilePass);
        edtProfileConfirm = findViewById(R.id.edtProfileConfirm);
        edtProfileCountry = findViewById(R.id.edtProfileCountry);
        edtProfileCity = findViewById(R.id.edtProfileCity);
        edtProfilePhone = findViewById(R.id.edtProfilePhone);
        txtProfileCode = findViewById(R.id.txtProfileCode);
        imgProfileFlag = findViewById(R.id.imgProfileFlag);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        imgProfile = findViewById(R.id.imgProfile);
        imgChangeProfile = findViewById(R.id.imgChangeProfile);
        txtLogout = findViewById(R.id.txtLogout);
    }

    private void fetchUserData(String email) {
        String url = "http://192.168.1.23/android_project/fetch_user_data.php?email=" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONObject user = response.getJSONObject("user");
                            edtProfileFirstName.setText(user.getString("first_name"));
                            edtProfileLastName.setText(user.getString("last_name"));
                            edtProfileCountry.setText(user.getString("country"));
                            edtProfileCity.setText(user.getString("city"));
                            edtProfilePhone.setText(user.getString("phone"));
                            String gender = user.getString("gender");
                            if (gender != null && gender.equalsIgnoreCase("Female")) {
                                edtProfileFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_female_35, 0);
                            } else {
                                edtProfileFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_male_35, 0);
                            }

                        } else {
                            Toast.makeText(Profile.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Profile.this, "Failed to parse user data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(Profile.this, "Failed to fetch user data", Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    imgProfile.setImageURI(result);
                    profileImageUri = result;
                    isImageChanged = true;
                }
            });

    private void pickImageFromGallery() {
        pickImageLauncher.launch("image/*");
    }

    private void saveProfile() {
        String firstName = edtProfileFirstName.getText().toString().trim();
        String lastName = edtProfileLastName.getText().toString().trim();
        String country = edtProfileCountry.getText().toString().trim();
        String city = edtProfileCity.getText().toString().trim();
        String phone = edtProfilePhone.getText().toString().trim();
        String password = edtProfilePass.getText().toString().trim();
        String confirmPassword = edtProfileConfirm.getText().toString().trim();

        if (!password.isEmpty() || !confirmPassword.isEmpty()) {
            if (!validatePassword(password, confirmPassword)) {
                return;
            }
        }

        String url = "http://192.168.1.23/android_project/update_user_data.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                            finish(); // Close the activity
                        } else {
                            Toast.makeText(Profile.this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Profile.this, "Failed to parse response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(Profile.this, "Failed to update profile", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("country", country);
                params.put("city", city);
                params.put("phone", phone);
                if (!password.isEmpty()) {
                    params.put("password", password);
                }
                if (isImageChanged && profileImageUri != null) {
                    params.put("profile_image", profileImageUri.toString()); // You can convert the image to a base64 string
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean validatePassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            edtProfilePass.setError("Password must contain at least 6 characters");
            edtProfilePass.requestFocus();
            return false;
        } else if (!password.matches(".*[a-zA-Z]+.*")) {
            edtProfilePass.setError("Password must contain at least 1 character");
            edtProfilePass.requestFocus();
            return false;
        } else if (!password.matches(".*[0-9]+.*")) {
            edtProfilePass.setError("Password must contain at least 1 number");
            edtProfilePass.requestFocus();
            return false;
        } else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            edtProfilePass.setError("Password must contain at least 1 special character");
            edtProfilePass.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            edtProfilePass.setError("Passwords do not match");
            edtProfileConfirm.setError("Passwords do not match");
            edtProfilePass.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Code to execute when the activity is resumed
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Code to execute when the activity is paused
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Code to execute when the activity is started
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Code to execute when the activity is stopped
    }
}
