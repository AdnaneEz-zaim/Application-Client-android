package com.example.client;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Vector;

public class PromoAdapter extends ArrayAdapter<PromoItem> {
    Activity context;
    RequestOptions option;

    public PromoAdapter(Activity context, Vector<PromoItem> itemArrayList) {
        super(context, 0, itemArrayList);
        this.context=context;
        option = new RequestOptions().placeholder(R.drawable.promo_holder).error(R.drawable.white_shape);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.promo_item, parent, false);
        }

        PromoItem current = getItem(position);

        ImageView img = (ImageView) listItemView.findViewById(R.id.promoIMG);
        //img.setImageResource(current.getImage());
        Glide.with(context).load(current.getImagePromo()).apply(option).into(img);

        return listItemView;

    }


}
