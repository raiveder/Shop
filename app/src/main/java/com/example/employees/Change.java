package com.example.employees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
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

public class Change extends AppCompatActivity implements View.OnClickListener {

    TextView txtProduct;
    TextView txtQuantity;
    TextView txtCost;
    ImageView imageView;
    String Image;
    ProgressBar PBWait;
    int Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        PBWait = findViewById(R.id.pbWait);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDel = findViewById(R.id.btnDel);
        Button btnDelImage = findViewById(R.id.btnDelImage);

        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnDelImage.setOnClickListener(this);

        txtProduct = findViewById(R.id.Product);
        txtQuantity = findViewById(R.id.Quantity);
        txtCost = findViewById(R.id.Cost);
        Other.setDynamicHint(txtProduct, txtQuantity, txtCost);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);

        setData();
    }

    private void setData() {

        Bundle arg = getIntent().getExtras();
        Id = arg.getInt("Id");
        Image = arg.getString("Image");

        txtProduct.setText(arg.getString("Product"));
        txtQuantity.setText(String.valueOf(arg.getInt("Quantity")));
        txtCost.setText(String.valueOf(arg.getInt("Cost")));

        imageView.setImageBitmap(Images.getImgBitmap(Change.this, Image));
    }

    private void putData(int id, String product, String quantity, String cost, String image) {

        PBWait.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/СергеевДЕ/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask(id, product, Integer.parseInt(quantity),
                Integer.parseInt(cost), image);

        Call<Mask> call = retrofitAPI.updateData(id, mask);

        call.enqueue(new Callback<Mask>() {

            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {

                Toast.makeText(Change.this, "Товар успешно изменён",
                        Toast.LENGTH_SHORT).show();

                PBWait.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(Change.this, "Ошибка: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void deleteData(int id) {

        PBWait.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/СергеевДЕ/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Mask> call = retrofitAPI.deleteData(id);

        call.enqueue(new Callback<Mask>() {

            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(Change.this, "Товар успешно удалён",
                        Toast.LENGTH_SHORT).show();

                PBWait.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(Change.this, "Ошибка: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnBack:
                startActivity(new Intent(Change.this, MainActivity.class));
                break;

            case R.id.btnSave:

                String Product = txtProduct.getText().toString();
                String Quantity = txtQuantity.getText().toString();
                String Cost = txtCost.getText().toString();

                if (Other.checkData(Change.this, Product, Quantity, Cost)) {
                    return;
                }

                putData(Id, Product, Quantity, Cost, Image);
                new Handler().postDelayed(() -> startActivity(
                        new Intent(Change.this, MainActivity.class)), 1000);
                break;

            case R.id.btnDel:
                deleteData(Id);

                new Handler().postDelayed(() -> startActivity(
                        new Intent(Change.this, MainActivity.class)), 1000);
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