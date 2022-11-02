package com.example.employees;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static int id;
    Spinner spinner;
    EditText findByProduct;
    ListView listView;
    List<Mask> lvProducts;
    AdapterMask pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> startActivity(new Intent(
                MainActivity.this, AddData.class))));

        findByProduct = findViewById(R.id.FindProduct);
        findByProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                sort(spinner.getSelectedItemPosition());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        listView = findViewById(R.id.lvData);
        lvProducts = new ArrayList<>();
        pAdapter = new AdapterMask(MainActivity.this, lvProducts);
        listView.setAdapter(pAdapter);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {

            Intent intent = new Intent(MainActivity.this, Change.class);
            intent.putExtra("Id", Integer.parseInt(String.valueOf(arg3)));
            // Посмотреть со сбитыми id
            intent.putExtra("Product", lvProducts.get(position).getProduct());
            intent.putExtra("Quantity", lvProducts.get(position).getQuantity());
            intent.putExtra("Cost", lvProducts.get(position).getCost());
            intent.putExtra("Image", lvProducts.get(position).getImage());
            startActivity(intent);
        });
        new GetProducts().execute();

    }

    private void sort(int position) {
        String query = "Select * FROM Shop WHERE Product LIKE '" +
                findByProduct.getText() + "%'";

        switch (position) {
            case 0:
                getData(query);
                break;

            case 1:
                getData(query + " ORDER BY Product ASC");
                break;

            case 2:
                getData(query + " ORDER BY Quantity ASC");
                break;

            case 3:
                getData(query + " ORDER BY Cost ASC");
                break;
        }
    }

    public void getData(String query) {

    }

    public static String encodeImage(Bitmap bitmap) {
        int prevW = 500;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();

        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    private class GetProducts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5101/NGKNN/СергеевДЕ/api/Shops");
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
            } catch (Exception ignored) {

            }
        }
    }
}