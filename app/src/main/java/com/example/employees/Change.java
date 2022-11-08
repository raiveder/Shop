package com.example.employees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Change extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    Button btnSafe;
    Button btnDel;
    Button btnDelImage;
    TextView txtProduct;
    TextView txtQuantity;
    TextView txtCost;
    ImageView imageView;
    String Image;
    int Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(Change.this, MainActivity.class);
            startActivity(intent);
        }));

        btnSafe = findViewById(R.id.btnSafe);
        btnSafe.setOnClickListener(this);

        btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        btnDelImage = findViewById(R.id.btnDelImage);
        btnDelImage.setOnClickListener(this);

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

        setData();
    }

    private void setData()
    {
        Bundle arg = getIntent().getExtras();
        Id = arg.getInt("Id");
        txtProduct.setText(arg.getString("Product"));
        txtQuantity.setText(String.valueOf(arg.getInt("Quantity")));
        txtCost.setText(String.valueOf(arg.getInt("Cost")));
        Image = arg.getString("Image");
        imageView.setImageBitmap(getImgBitmap(Image));
    }

    private void putData(int id, String product, String quantity, String cost, String image) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/СергеевДЕ/api/Shops/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask(id, product, Integer.parseInt(quantity),
                Integer.parseInt(cost), image);

        Call<Mask> call = retrofitAPI.updateData(mask);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(Change.this, "Товар успешно изменён", Toast.LENGTH_SHORT).show();

                txtProduct.setText("");
                txtQuantity.setText("");
                txtCost.setText("");

                txtProduct.clearFocus();
                txtQuantity.clearFocus();
                txtCost.clearFocus();
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(Change.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);
                    Image = MainActivity.encodeImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private Bitmap getImgBitmap(String encodedImg) {
        if (!encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return BitmapFactory.decodeResource(Change.this.getResources(),
                R.drawable.stub);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSafe:
                String Product = txtProduct.getText().toString();
                String Quantity = txtQuantity.getText().toString();
                String Cost = txtCost.getText().toString();

                putData(Id, Product, Quantity, Cost, Image);
                break;

            case R.id.btnDel:
                startActivity(new Intent(Change.this, MainActivity.class));
                break;

            case R.id.btnDelImage:
                Image = "null";
                imageView.setImageBitmap(getImgBitmap(Image));
                break;
        }
    }
}