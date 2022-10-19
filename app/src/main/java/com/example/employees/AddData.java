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
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

public class AddData extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    Button btnAdd;
    TextView txtSurname;
    TextView txtName;
    TextView txtAge;
    ImageView imageView;
    String Image;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(AddData.this, MainActivity.class);
            startActivity(intent);
        }));

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        txtSurname = findViewById(R.id.Surname);
        txtSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtSurname.setHint(null);
            else
                txtSurname.setHint(R.string.product);
        });

        txtName = findViewById(R.id.Name);
        txtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtName.setHint(null);
            else
                txtName.setHint(R.string.quantity);
        });

        txtAge = findViewById(R.id.Age);
        txtAge.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtAge.setHint(null);
            else
                txtAge.setHint(R.string.cost);
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
        String Surname = txtSurname.getText().toString();
        String Name = txtName.getText().toString();
        String Age = txtAge.getText().toString();

        switch (v.getId()) {

            case R.id.btnAdd:
                try {
                    ConnectionHelper dbHelper = new ConnectionHelper();
                    connection = dbHelper.connectionClass();

                    if (connection != null) {
                        String query = "INSERT INTO Employees VALUES('" + Surname + "', '" + Name +
                                "', " + Age + ", '" + Image + "')";
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
        }
    }
}