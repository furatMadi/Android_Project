package com.example.android_project;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edtFirstName;
    private EditText edtLastName;
    private Spinner spnGender;
    private Spinner spnCountry;
    private Spinner spnCity;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirm;
    private EditText edtPhoneNumber;
    private TextView txtCountryCode;
    private ImageView imgFlag;
    private Button btnSignup;
    private String userType;
    private String[] genders = {"Gender", "Male", "Female"};
    private String[] countries = {"Country", "Palestine", "Jordan", "Yemen", "Lebanon", "Syria"};
    private boolean firstCountry = true;
    private boolean firstGender = true;
    private boolean firstCity = true;
    private int previousCountry = 1;
    private int previousCity = 1;
    private String[] cities = null;
    private static final String SIGNUP_URL = "http://192.168.1.23/android_project/signup.php";
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userType = getIntent().getStringExtra("userType");

    }

    protected void onResume() {
        super.onResume();
        initializeLayout();
        if(userType.equalsIgnoreCase("customer")){
            btnSignup.setText("Sign Up");
        }
        else if(userType.equalsIgnoreCase("manager")){
            btnSignup.setText("Add Admin");
        }
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()) {
                    signupUser();
                }
            }
        });
    }


    private boolean validateInputs() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirm.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();

        if (firstName.replaceAll(" ", "").isEmpty()) {
            edtFirstName.setError("First name is required");
            edtFirstName.requestFocus();
            return false;
        }

        if (lastName.replaceAll(" ", "").isEmpty()) {
            edtLastName.setError("Last name is required");
            edtLastName.requestFocus();
            return false;
        }

        if (email.replaceAll(" ", "").isEmpty()) {
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return false;
        }
        else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            edtEmail.setError("Please enter a valid email");
            edtEmail.requestFocus();
            return false;
        }

        if (password.replaceAll(" ", "").isEmpty()) {
            edtPassword.setError("Please enter your password");
            edtPassword.requestFocus();
            return false;
        } else if (password.length() < 5) {
            edtPassword.setError("Password must contain at least 5 characters");
            edtPassword.requestFocus();
            return false;
        } else if (!password.matches(".*[a-zA-Z]+.*")) {
            edtPassword.setError("Password must contain at least 1 character");
            edtPassword.requestFocus();
            return false;
        } else if (!password.matches(".*[0-9]+.*")) {
            edtPassword.setError("Password must contain at least 1 number");
            edtPassword.requestFocus();
            return false;
        } else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            edtPassword.setError("Password must contain at least 1 special character");
            edtPassword.requestFocus();
            return false;
        }

        if (confirmPass.replaceAll(" ","").isEmpty()) {
            edtConfirm.setError("Please confirm your password");
            edtConfirm.requestFocus();
            return false;
        }

        if (phone.replaceAll(" ", "").isEmpty()) {
            edtPhoneNumber.setError("Phone number is required");
            edtPhoneNumber.requestFocus();
            return false;
        }
        else if (!phone.matches("[0-9]+")) {
            edtPhoneNumber.setError("Please enter a valid phone number");
            edtPhoneNumber.requestFocus();
            return false;
        }

        if(spnGender.getSelectedItemPosition() == 0){
            Toast.makeText(SignUpActivity.this, "Please select the Gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spnCountry.getSelectedItemPosition() == 0){
            Toast.makeText(SignUpActivity.this, "Please select country", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spnCity.getSelectedItemPosition() == 0){
            Toast.makeText(SignUpActivity.this, "Please select city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPass)) {
            edtConfirm.setError("Passwords do not match");
            edtConfirm.requestFocus();
            return false;
        }

        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId()==R.id.spnCountry){

            if (firstCountry) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey));
                for (int i = 1; i < parent.getChildCount(); i++) {
                    ((TextView) parent.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
                }
                firstCountry = false;
                return;
            } else{
                if (pos == 0) {
                    spnCountry.setSelection(previousCountry);
                    Toast.makeText(SignUpActivity.this, "Please select a country", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pos == 1) { // Palestine
                    cities = new String[]{"City", "Gaza", "Tulkarm", "Ramallah", "Hebron", "Jerusalem", "Jenin", "Nablus", "Qalqilya", "Tubas", "Salfit", "Bethlehem", "Jericho"};
                    imgFlag.setImageResource(R.drawable.palestine);
                    txtCountryCode.setText("+970");
                }
                else if (pos == 2) { // Jordan
                    cities = new String[]{"City", "Amman", "Irbid", "Zarqa", "Aqaba", "Salt", "Madaba", "Karak", "Ma'an", "Tafilah", "Mafraq", "Jerash", "Ajloun"};
                    imgFlag.setImageResource(R.drawable.jordan);
                    txtCountryCode.setText("+962");
                }
                else if (pos == 3) { // Yemen
                    cities = new String[]{"City", "Sana'a", "Aden", "Taiz", "Al Hudaydah", "Ibb", "Dhamar", "Al Mukalla", "Zinjibar", "Sayyan", "Ash Shihr", "Sahar", "Hajjah"};
                    imgFlag.setImageResource(R.drawable.yemen);
                    txtCountryCode.setText("+967");
                }
                else if (pos == 4) { // Lebanon
                    cities = new String[]{"City", "Beirut", "Tripoli", "Sidon", "Tyre", "Jounieh", "Zahle", "Byblos", "Baalbek", "Batroun", "Aley", "Nabatieh", "Jbeil"};
                    imgFlag.setImageResource(R.drawable.lebanon);
                    txtCountryCode.setText("+961");
                }
                else if (pos == 5) { // Syria
                    cities = new String[]{"City", "Damascus", "Aleppo", "Homs", "Hama", "Latakia", "Deir ez-Zor", "Ar-Raqqah", "Al-Hasakah", "Qamishli", "Idlib", "Daraa", "As-Suwayda"};
                    imgFlag.setImageResource(R.drawable.syria);
                    txtCountryCode.setText("+963");

                }
                ArrayAdapter<String> objCityArr = new
                        ArrayAdapter<>(SignUpActivity.this, R.layout.spinner_layout, cities);
                spnCity.setAdapter(objCityArr);
                previousCountry = pos;
                firstCity = true;


            }

        }
        else if (parent.getId()==R.id.spnCity) {
            if (firstCity) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey));
                for (int i = 1; i < parent.getChildCount(); i++) {
                    ((TextView) parent.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
                }
                firstCity = false;
                return;
            } else {
                if (pos == 0) {
                    Toast.makeText(SignUpActivity.this, "Please select a city", Toast.LENGTH_SHORT).show();
                    spnCity.setSelection(previousCity);
                    return;
                }
            }
            previousCity = pos;

        }
        else if (parent.getId() == R.id.spnGender){
            if (firstGender){
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey));
                for (int i = 1; i < parent.getChildCount(); i++) {
                    ((TextView) parent.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
                }
                firstGender = false;

            } else {
                if (pos == 0){
                    Toast.makeText(SignUpActivity.this, "Please select a Gender", Toast.LENGTH_SHORT).show();
                    spnGender.setSelection(1);
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void signupUser() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirm.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();
        String gender = spnGender.getSelectedItem().toString();
        String city = spnCity.getSelectedItem().toString();
        String country = spnCountry.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                        if (response.contains("Sign-up successful")) {
                            Intent intent = new Intent(SignUpActivity.this , LogInActivity.class);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
                        Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG,error.toString());
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first name", firstName);
                params.put("last name", lastName);
                params.put("password", pass);
                params.put("email", email);
                params.put("gender", gender);
                params.put("city", city);
                params.put("country", country);
                params.put("phone", phone);
                params.put("user_type", userType);
                return params;
            }
        };

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initializeLayout(){
        btnSignup = (Button) SignUpActivity.this.findViewById(R.id.btnSignup);
        edtFirstName = (EditText) SignUpActivity.this.findViewById(R.id.edtFirstName);
        edtLastName = (EditText) SignUpActivity.this.findViewById(R.id.edtLastName);
        edtEmail = (EditText) SignUpActivity.this.findViewById(R.id.edtEmail);
        edtPassword = (EditText) SignUpActivity.this.findViewById(R.id.edtPassword);
        edtConfirm = (EditText) SignUpActivity.this.findViewById(R.id.edtConfirm);
        edtPhoneNumber = (EditText) SignUpActivity.this.findViewById(R.id.edtPhoneNumber);
        spnGender = (Spinner) SignUpActivity.this.findViewById(R.id.spnGender);
        imgFlag = (ImageView) SignUpActivity.this.findViewById(R.id.imgFlag);
        txtCountryCode = (TextView) SignUpActivity.this.findViewById(R.id.txtCountryCode);
        spnCity = (Spinner) SignUpActivity.this.findViewById(R.id.spnCity);
        spnCountry = (Spinner) SignUpActivity.this.findViewById(R.id.spnCountry);

        ArrayAdapter<String> objGenderArr = new
                ArrayAdapter<>(SignUpActivity.this, R.layout.spinner_layout, genders);
        spnGender.setAdapter(objGenderArr);
        spnGender.setOnItemSelectedListener(this);

        spnCountry = (Spinner) SignUpActivity.this.findViewById(R.id.spnCountry);
        ArrayAdapter<String> objCountryArr = new
                ArrayAdapter<>(SignUpActivity.this, R.layout.spinner_layout, countries);
        spnCountry.setAdapter(objCountryArr);

        cities = new String[]{"City"};
        ArrayAdapter<String> objCityArr = new
                ArrayAdapter<>(SignUpActivity.this, R.layout.spinner_layout, cities);
        spnCity.setAdapter(objCityArr);

        spnCountry.setOnItemSelectedListener(this);

        spnCity.setOnItemSelectedListener(this);



    }

}
