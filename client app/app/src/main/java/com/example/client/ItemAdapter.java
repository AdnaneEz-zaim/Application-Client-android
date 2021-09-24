package com.example.client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ItemAdapter extends ArrayAdapter<Item> {
    SessionManager sessionManager;
    Bitmap bitmap;
    Activity context;
    ArrayList<Item> itemArrayList;
    RequestOptions option;


    public ItemAdapter(Activity context, ArrayList<Item> itemArrayList) {
        super(context, 0, itemArrayList);
        this.context=context;
        this.itemArrayList=itemArrayList;
        option = new RequestOptions().placeholder(R.drawable.white_shape).error(R.drawable.white_shape);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sessionManager=new SessionManager(context);
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }

        Item current = getItem(position);

        TextView nomProduit = (TextView) listItemView.findViewById(R.id.prodName);
        try {
            nomProduit.setText(current.getName());
        }catch (NullPointerException e){
            Log.i(TAG, "getView: "+e.getMessage());
        }


        ImageView img = (ImageView) listItemView.findViewById(R.id.itemImg);
        //img.setImageResource(current.getImage());
        Glide.with(context).load(current.getImage()).apply(option).into(img);
        //new GetImageFromURL(img).execute(current.getImage());
        //itemArrayList.get(position).setImageBitmap(bitmap);
        //sessionManager.setItems(new ItemManager(itemArrayList));

        TextView price = (TextView) listItemView.findViewById(R.id.prix);
        price.setText("" + current.getPrix());

        return listItemView;

    }


/*
    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap>
    {

        ImageView imgView;
        public GetImageFromURL(ImageView imgv)
        {
            this.imgView=imgv;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay=url[0];
            bitmap=null;

            try{

                InputStream ist=new java.net.URL(urldisplay).openStream();
                bitmap= BitmapFactory.decodeStream(ist);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){

            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }

 */

}
