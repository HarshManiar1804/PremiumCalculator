package com.harsh.premiumcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class FirstDialog extends AppCompatActivity {
        EditText txtName;
        Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_dialog);

        txtName = findViewById(R.id.txtName);
        btnSave = findViewById(R.id.btnSave);
        
    }
}