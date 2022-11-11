package com.example.employees;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

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

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean checkData(Context context, String product, String quantity, String cost) {

        if (product.equals("")) {
            Toast.makeText(context, "Заполните поле \"Товар\"",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        if (quantity.equals("")) {
            Toast.makeText(context, "Заполните поле \"Количество\"",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        if (cost.equals("")) {
            Toast.makeText(context, "Заполните поле \"Стоимость\"",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }
}
