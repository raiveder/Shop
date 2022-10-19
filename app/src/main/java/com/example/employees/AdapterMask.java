package com.example.employees;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterMask extends BaseAdapter {
    public AdapterMask(Context mContext, List<Mask> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

    private Context mContext;
    List<Mask> maskList;

    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return maskList.get(i).getId();
    }

    private Bitmap getUserImage(String encodedImg) {
        if (!encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else
            return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_mask, null);

        TextView Product = v.findViewById(R.id.Product);
        TextView Quantity = v.findViewById(R.id.Quantity);
        TextView Cost = v.findViewById(R.id.Cost);
        ImageView Image = v.findViewById(R.id.imageView);

        Mask mask = maskList.get(position);
        Product.setText(mask.getProduct());
        Quantity.setText(Integer.toString(mask.getQuantity()));
        Cost.setText(Double.toString(mask.getCost()));
        if (!mask.getImage().equals("null")) {
            Image.setImageBitmap(getUserImage(mask.getImage()));
        }

        return v;
    }
}