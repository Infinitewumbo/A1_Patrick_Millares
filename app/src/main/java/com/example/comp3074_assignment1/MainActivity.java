package com.example.comp3074_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editHours, editRate;
    private TextView textPay, textOvertimePay, textTotalPay, textTax;
    private Button btnCalculate;

    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String KEY_PAYMENT_LIST = "PaymentList";

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_details) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculatePay() {
        String hoursStr = editHours.getText().toString();
        String rateStr = editRate.getText().toString();

        // Input validation
        if (hoursStr.isEmpty() || rateStr.isEmpty()) {
            Toast.makeText(this, "Error: Please enter both hours worked and hourly rate.", Toast.LENGTH_LONG).show();
            return;
        }

        double hours, rate;
        try {
            hours = Double.parseDouble(hoursStr);
            rate = Double.parseDouble(rateStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid number format. Please enter valid numerical values.", Toast.LENGTH_LONG).show();
            return;
        }

        if (hours <= 0 || rate <= 0) {
            Toast.makeText(this, "Error: Hours and Rate must be positive values.", Toast.LENGTH_LONG).show();
            return;
        }

        // Calculation variables
        double regularHours = Math.min(hours, 40);
        double overtimeHours = Math.max(0, hours - 40);

        // Pay calculation
        double regularPay = regularHours * rate;
        double overtimePay = overtimeHours * rate * 1.5;
        double totalPay = regularPay + overtimePay;

        // Tax calculation (18% of total pay)
        double taxRate = 0.18;
        double tax = totalPay * taxRate;

        // Formatting for display
        DecimalFormat df = new DecimalFormat("#,##0.00");

        // 3. Display results to the user
        textPay.setText(String.format("Regular Pay: $%s", df.format(regularPay)));
        textOvertimePay.setText(String.format("Overtime Pay: $%s", df.format(overtimePay)));
        textTotalPay.setText(String.format("TOTAL PAY: $%s", df.format(totalPay)));
        textTax.setText(String.format("Tax (18%%): $%s", df.format(tax)));

        // 4. Log the payment data
        logPaymentEntry(hours, rate, totalPay, tax);

        Toast.makeText(this, "Success: Payment calculated and logged!", Toast.LENGTH_SHORT).show();
    }

    private void logPaymentEntry(double hours, double rate, double totalPay, double tax) {
        try {
            // Load existing list
            ArrayList<String> paymentList = loadPaymentEntries();
            DecimalFormat df = new DecimalFormat("0.00");

            // Create a formatted string entry
            String newEntry = String.format("Hours: %.2f, Rate: $%.2f, Total Pay: $%s, Tax: $%s",
                    hours, rate, df.format(totalPay), df.format(tax));

            // Add the new entry to the list
            paymentList.add(newEntry);

            // Convert the list back to a JSON Array string for storage
            JSONArray jsonArray = new JSONArray(paymentList);

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_PAYMENT_LIST, jsonArray.toString());
            editor.apply();

        } catch (Exception e) {
            // Log error if JSON processing fails
            e.printStackTrace();
            Toast.makeText(this, "Error logging payment data.", Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<String> loadPaymentEntries(Context context) {
        ArrayList<String> paymentList = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonList = prefs.getString(KEY_PAYMENT_LIST, null);

        if (jsonList != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    paymentList.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return paymentList;
    }

    private ArrayList<String> loadPaymentEntries() {
        return loadPaymentEntries(this);
    }
}