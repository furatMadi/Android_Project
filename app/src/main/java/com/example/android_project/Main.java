package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {
    private ImageView imgCustomer;
    private ImageView imgManager;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imgCustomer = findViewById(R.id.imgCustomer);
        imgManager = findViewById(R.id.imgManager);

        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Main.this, LogInActivity.class);
                intent.putExtra("userType", "customer");
                startActivity(intent);
            }
        });
        imgManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Main.this, LogInActivity.class);
                intent.putExtra("userType", "manager");
                startActivity(intent);
            }
        });
    }
}
