package com.example.employees;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText findByProduct;
    ListView listView;
    List<Mask> lvProducts;
    AdapterMask pAdapter;
    ProgressBar pbWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> startActivity(new Intent(
                MainActivity.this, AddData.class))));

        pbWait = findViewById(R.id.pbWait);

        findByProduct = findViewById(R.id.FindProduct);
        findByProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sort(spinner.getSelectedItemPosition());
            }
        });

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                List<Mask> list = new ArrayList<>();
                pAdapter = new AdapterMask(MainActivity.this, list);
                listView.setAdapter(pAdapter);

                pbWait.setVisibility(View.VISIBLE);

                new Handler().postDelayed(() ->
                        sort(position), 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        lvProducts = new ArrayList<>();
        pAdapter = new AdapterMask(MainActivity.this, lvProducts);

        listView = findViewById(R.id.lvData);
        listView.setAdapter(pAdapter);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Intent intent = new Intent(MainActivity.this, Change.class);
            intent.putExtra("Id", Integer.parseInt(String.valueOf(arg3)));
            intent.putExtra("Product", lvProducts.get(position).getProduct());
            intent.putExtra("Quantity", lvProducts.get(position).getQuantity());
            intent.putExtra("Cost", lvProducts.get(position).getCost());
            intent.putExtra("Image", lvProducts.get(position).getImage());
            startActivity(intent);
        });

        new GetProducts().execute();
    }

    private void sort(int position) {

        List<Mask> list = new ArrayList<>();

        if (findByProduct.getText().equals(null)) {
            list.addAll(lvProducts);
        } else {
            String text = findByProduct.getText().toString();

            for (Mask item : lvProducts) {
                if(item.getProduct().substring(0, text.length()).equalsIgnoreCase(text))
                {
                    list.add(item);
                }
            }
        }

        switch (position) {

            case 1:
                Collections.sort(list, new SortByProduct());
                break;

            case 2:
                Collections.sort(list, new SortByQuantity());
                break;

            case 3:
                Collections.sort(list, new SortByCost());
                break;
        }

        pbWait.setVisibility(View.GONE);
        pAdapter = new AdapterMask(MainActivity.this, list);
        listView.setAdapter(pAdapter);
    }

    private class GetProducts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/СергеевДЕ/api/Shops");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            try {
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0; i < tempArray.length(); i++) {

                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempProduct = new Mask(
                            productJson.getInt("Id"),
                            productJson.getString("Product"),
                            productJson.getInt("Quantity"),
                            productJson.getInt("Cost"),
                            productJson.getString("Image")
                    );
                    lvProducts.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}