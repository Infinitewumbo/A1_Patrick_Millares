package com.example.comp3074_assignment1;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        listView = findViewById(R.id.list_payments);

        // 1. Load the list of payments using the helper method from MainActivity
        ArrayList<String> paymentList = MainActivity.loadPaymentEntries(this);

        // 2. Check if the list is empty and handle gracefully
        if (paymentList.isEmpty()) {
            paymentList.add("No payments logged yet. Go back to the main screen to calculate a payment.");
        }

        // 3. Create an ArrayAdapter to bind the data to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, // Default list item layout
                paymentList
        );

        // 4. Set the adapter on the ListView
        listView.setAdapter(adapter);

        // The back button to MainActivity is automatically handled by the
        // parentActivityName configuration in AndroidManifest.xml
    }
}
