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

public class Change extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    Button btnSafe;
    Button btnDel;
    Button btnDelImage;
    TextView txtSurname;
    TextView txtName;
    TextView txtAge;
    Connection connection;
    ImageView imageView;
    String Image;

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

        setText();
    }

    public final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //Код для переворота изображения, если оно горизонтальное, проверить не смог,
                    //не помогает, но пишут, что рабочий.
                    //Не работает, потому что exif всегда null

                    /*String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    ExifInterface exif = null;
                    try {
                        File pictureFile = new File(picturePath);
                        exif = new ExifInterface(pictureFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int orientation = ExifInterface.ORIENTATION_NORMAL;

                    if (exif != null)
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            bitmap = rotateBitmap(bitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            bitmap = rotateBitmap(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            bitmap = rotateBitmap(bitmap, 270);
                            break;
                    }*/


                    imageView.setImageBitmap(bitmap);
                    Image = MainActivity.encodeImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void setText() {
        try {
            ConnectionHelper dbHelper = new ConnectionHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                String query = "Select * FROM Employees WHERE Id = " + MainActivity.id;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                String img = "null";
                while (resultSet.next()) {
                    txtSurname.setText(resultSet.getString(2));
                    txtName.setText(resultSet.getString(3));
                    txtAge.setText(resultSet.getString(4));
                    img = resultSet.getString(5);
                }
                imageView.setImageBitmap(getImgBitmap(img));

            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }

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
                String Surname = txtSurname.getText().toString();
                String Name = txtName.getText().toString();
                String Age = txtAge.getText().toString();

                String query = "UPDATE Employees SET Surname = '" + Surname +
                        "', Firstname ='" + Name + "', Age = " + Age + ", Image = '" + Image +
                        "' WHERE Id = " + MainActivity.id;
                updateQuery(query, "Данные успешно изменены");
                break;

            case R.id.btnDel:
                query = "DELETE FROM Employees WHERE Id = " + MainActivity.id;
                updateQuery(query, "Сотрудник успешно удалён");

                Intent intent = new Intent(Change.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.btnDelImage:
                Image = "null";
                imageView.setImageBitmap(getImgBitmap(Image));
                break;
        }
    }

    private void updateQuery(String query, String yesMessage) {

        try {
            ConnectionHelper dbHelper = new ConnectionHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

                txtSurname.clearFocus();
                txtName.clearFocus();
                txtAge.clearFocus();

                Toast.makeText(this, yesMessage, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }
}