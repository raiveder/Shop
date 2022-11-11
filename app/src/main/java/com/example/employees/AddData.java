package com.example.employees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnDelImage = findViewById(R.id.btnDelImage);

        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDelImage.setOnClickListener(this);

        txtProduct = findViewById(R.id.Product);
        txtQuantity = findViewById(R.id.Quantity);
        txtCost = findViewById(R.id.Cost);
        Other.setDynamicHint(txtProduct, txtQuantity, txtCost);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            InputStream is = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            imageView.setImageBitmap(bitmap);
                            Image = Images.encodeImage(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void postData(String product, String quantity, String cost, String image) {

        ProgressBar PBWait = findViewById(R.id.pbWait);
        PBWait.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/СергеевДЕ/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask(product, Integer.parseInt(quantity), Integer.parseInt(cost), image);

        Call<Mask> call = retrofitAPI.createPost(mask);

        call.enqueue(new Callback<Mask>() {

            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(AddData.this, "Товар успешно добавлен",
                        Toast.LENGTH_LONG).show();

                Image = "null";
                imageView.setImageResource(R.drawable.stub);

                txtProduct.setText("");
                txtQuantity.setText("");
                txtCost.setText("");

                txtProduct.clearFocus();
                txtQuantity.clearFocus();
                txtCost.clearFocus();

                PBWait.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(AddData.this, "Ошибка: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnBack:
                startActivity(new Intent(AddData.this, MainActivity.class));
                break;

            case R.id.btnAdd:
                String Product = txtProduct.getText().toString();
                String Quantity = txtQuantity.getText().toString();
                String Cost = txtCost.getText().toString();

                if (Other.checkData(AddData.this, Product, Quantity, Cost)) {
                    return;
                }

                Other.hideKeyboard(AddData.this);

                postData(Product, Quantity, Cost, Image);
                break;

            case R.id.btnDelImage:
                Image = "null";
                imageView.setImageResource(R.drawable.stub);
                break;

            case R.id.imageView:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImg.launch(intent);
                break;
        }
    }
}