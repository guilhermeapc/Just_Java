package com.example.android.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    private final View.OnClickListener incrementListener = v -> increment();
    private final View.OnClickListener decrementListener = v -> decrement();

    private final View.OnClickListener submitListener = v -> submitOrder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button incrementButton = findViewById(R.id.btn_increment);
        Button decrementButton = findViewById(R.id.btn_decrement);

        Button submitButton = findViewById(R.id.btn_submit);

        incrementButton.setOnClickListener(incrementListener);
        decrementButton.setOnClickListener(decrementListener);
        submitButton.setOnClickListener(submitListener);

    }

    public void increment() {
        if (quantity == 100) {
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    public void decrement() {
        if (quantity == 0) {
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;


        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }


        if (addChocolate) {
            basePrice = basePrice + 2;
        }


        return quantity * basePrice;
    }

    private String createOrderSummary(String name, int price, boolean addWhippedCream,
                                      boolean addChocolate) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }


    public void submitOrder() {

        EditText nameField = findViewById(R.id.name_field);
        Editable nameEditable = nameField.getText();
        String name = nameEditable.toString();

        CheckBox whippedCreamCB = findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCB = findViewById(R.id.chocolate_checkbox);

        boolean hasWhippedCream = whippedCreamCB.isChecked();
        boolean hasChocolate = chocolateCB.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String summary = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        TextView priceView = findViewById(R.id.price_text_view);
        priceView.setText(summary);


        //email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, summary);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}