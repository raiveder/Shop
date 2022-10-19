package com.example.employees;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetProducts extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("http://10.0.2.2:8090/api/Products");
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
        try
        {
            JSONArray tempArray = new JSONArray(s);
            for (int i = 0;i<tempArray.length();i++)
            {

                JSONObject productJson = tempArray.getJSONObject(i);
                Mask tempProduct = new Mask(
                        productJson.getInt("Id"),
                        productJson.getString("Product"),
                        productJson.getInt("Quantity"),
                        productJson.getDouble("Cost"),
                        productJson.getString("Image")
                );
                //listProduct.add(tempProduct);
                //pAdapter.notifyDataSetInvalidated();
            }
        } catch (Exception ignored) {

        }
    }
}
