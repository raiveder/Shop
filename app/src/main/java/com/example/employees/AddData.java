package com.example.employees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddData extends AppCompatActivity implements View.OnClickListener {
    TextView txtProduct;
    TextView txtQuantity;
    TextView txtCost;
    ImageView imageView;
    String Image;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(AddData.this, MainActivity.class);
            startActivity(intent);
        }));

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        txtProduct = findViewById(R.id.Product);
        txtProduct.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtProduct.setHint(null);
            else
                txtProduct.setHint(R.string.product);
        });

        txtQuantity = findViewById(R.id.Quantity);
        txtQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtQuantity.setHint(null);
            else
                txtQuantity.setHint(R.string.quantity);
        });

        txtCost = findViewById(R.id.Cost);
        txtCost.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtCost.setHint(null);
            else
                txtCost.setHint(R.string.cost);
        });

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //Здесь должен быть код для переворота изображения, но он не работает (в Change)

                    imageView.setImageBitmap(bitmap);
                    Image = MainActivity.encodeImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    public void onClick(View v) {
        String Product = txtProduct.getText().toString();
        String Quantity = txtQuantity.getText().toString();
        String Cost = txtCost.getText().toString();

        postData(Product, Quantity, Cost, Image);
        /*switch (v.getId()) {

            case R.id.btnAdd:
                try {
                    ConnectionHelper dbHelper = new ConnectionHelper();
                    connection = dbHelper.connectionClass();

                    if (connection != null) {
                        String query = "INSERT INTO Employees VALUES('" + Product + "', '" + Quantity +
                                "', " + Cost + ", '" + Image + "')";
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(query);

                        txtSurname.setText(null);
                        txtName.setText(null);
                        txtAge.setText(null);

                        txtSurname.clearFocus();
                        txtName.clearFocus();
                        txtAge.clearFocus();

                        Toast.makeText(this, "Сотрудник успешно добавлен", Toast.LENGTH_LONG).show();
                        break;
                    } else {
                        Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
                }
        }*/
    }

    private void postData(String product, String quantity, String cost, String image) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5101/NGKNN/СергеевДЕ/api/Shops") // Бросает в catch
                // Конвертер JSON
                .addConverterFactory(GsonConverterFactory.create())
                // Строим конструктор
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModal modal = new DataModal(product, Integer.parseInt(quantity),
                Integer.parseInt(cost), image);

        // Вызов сообщения и передача модального класса
        Call<DataModal> call = retrofitAPI.createPost(modal);

        //Выполнение метода
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                // Когда получен ответ от API
                Toast.makeText(AddData.this, "Data added to API", Toast.LENGTH_LONG).show();

                txtProduct.setText("");
                txtQuantity.setText("");
                txtCost.setText("");

                DataModal responseFromAPI = response.body();
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(AddData.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}