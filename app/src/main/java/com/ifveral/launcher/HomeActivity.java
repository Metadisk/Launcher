package com.ifveral.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {
    Button btn_apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_apps = findViewById(R.id.btn_launcher);
        btn_apps.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

    }
}
