package com.example.employees;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterMask extends BaseAdapter {

    private Context mContext;
    List<Mask> maskList;

    public AdapterMask(Context mContext, List<Mask> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

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
        Cost.setText(Integer.toString(mask.getCost()));
        Image.setImageBitmap(Images.getImgBitmap(mContext, mask.getImage()));

        return v;
    }
}