package com.example.comp3074_assignment1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editHours, editRate;
    private TextView textPay, textOvertimePay, textTotalPay, textTax;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editHours = findViewById(R.id.edit_hours);
        editRate = findViewById(R.id.edit_rate);
        btnCalculate = findViewById(R.id.btn_calculate);
        textPay = findViewById(R.id.text_pay);
        textOvertimePay = findViewById(R.id.text_overtime_pay);
        textTotalPay = findViewById(R.id.text_total_pay);
        textTax = findViewById(R.id.text_tax);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePay();
            }
        });
    }
}