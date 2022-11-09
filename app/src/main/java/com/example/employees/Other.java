package com.example.employees;

import android.widget.TextView;

public class Other {

    public static void setDynamicHint(TextView product, TextView quantity, TextView cost) {

        product.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                product.setHint(null);
            else
                product.setHint(R.string.product);
        });

        quantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                quantity.setHint(null);
            else
                quantity.setHint(R.string.quantity);
        });

        cost.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                cost.setHint(null);
            else
                cost.setHint(R.string.cost);
        });
    }
}
