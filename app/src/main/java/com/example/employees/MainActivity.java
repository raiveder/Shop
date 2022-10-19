package com.example.employees;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static String id;
    Spinner spinner;
    Connection connection;
    Button btnAdd;
    EditText findByProduct;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddData.class);
            startActivity(intent);
        }));

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
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            try {
                id = String.valueOf(arg3);
                ConnectionHelper dbHelper = new ConnectionHelper();
                connection = dbHelper.connectionClass();
                if (connection != null) {
                    Intent intent = new Intent(MainActivity.this, Change.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Проверьте подключение!",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Возникла ошибка!",
                        Toast.LENGTH_LONG).show();
            }
        });

        getTextFromSQL("Select * FROM Shop");
    }

    private void sort(int position) {
        String query = "Select * FROM Shop WHERE Product LIKE '" +
                findByProduct.getText() + "%'";

        switch (position) {
            case 0:
                getTextFromSQL(query);
                break;

            case 1:
                getTextFromSQL(query + " ORDER BY Product ASC");
                break;

            case 2:
                getTextFromSQL(query + " ORDER BY Quantity ASC");
                break;

            case 3:
                getTextFromSQL(query + " ORDER BY Cost ASC");
                break;
        }
    }

    public static String encodeImage(Bitmap bitmap) {
        int prevW = 500;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        int a = bitmap.getHeight();
        int c = bitmap.getWidth();

        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    public void getTextFromSQL(String query) {
        List<Mask> data = new ArrayList<Mask>();
        AdapterMask pAdapter = new AdapterMask(MainActivity.this, data);
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    Mask tempMask = new Mask
                            (Integer.parseInt(resultSet.getString("Id")),
                                    resultSet.getString("Product"),
                                    Integer.parseInt(resultSet.getString("Quantity")),
                                    Double.parseDouble(resultSet.getString("Cost")),
                                    resultSet.getString("Image")
                            );

                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            } else {
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        enterMobile(pAdapter);
    }

    public void enterMobile(AdapterMask pAdapter) {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }
}