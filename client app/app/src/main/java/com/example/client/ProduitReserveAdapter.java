package com.example.client;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProduitReserveAdapter extends ArrayAdapter<ProduitReserve> {
    Context context;
    private RequestQueue queue;
    private MyRequest request;
    private SessionManager sessionManager;
    private String cmnt;
    private String avis;
    RequestOptions option;
    ArrayList<ProduitReserve> currentList;

    public interface OnItemClickListner{
        public void remove(int position);
    }


    public ProduitReserveAdapter(Activity context, ArrayList<ProduitReserve> androidFlavors) {
        super(context, 0, androidFlavors);
        this.context= context;
        option = new RequestOptions().placeholder(R.drawable.white_shape).error(R.drawable.white_shape);
        currentList=androidFlavors;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.produit_reserve, parent, false);
        }


        queue = VolleySinglton.getInstance(context).getRequestQueue();
        request=new MyRequest(context,queue);
        sessionManager=new SessionManager(context);

        final ProduitReserve current = getItem(position);

        TextView nomProduit = (TextView) listItemView.findViewById(R.id.nomProdR);
        nomProduit.setText(current.getNomProd());


        TextView quantite = (TextView) listItemView.findViewById(R.id.quantProdR);
        quantite.setText("" + current.getQuantiteProd());


        ImageView img = (ImageView) listItemView.findViewById(R.id.imageProdR);
        //img.setImageResource(current.getImageProd());
        Glide.with(context).load(current.getImageProd()).apply(option).into(img);


        Button b1 = (Button) listItemView.findViewById(R.id.confirmeR);
        b1.setBackgroundResource(current.getIcon().getIconConfirme());

        Button b2 = (Button) listItemView.findViewById(R.id.declineR);
        b2.setBackgroundResource(current.getIcon().getIconDecline());

        TextView price = (TextView) listItemView.findViewById(R.id.prixR);
        price.setText(current.getQuantiteProd()*current.getPrice()+"");

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        Log.i(TAG, "getView: here i am "+ current.getDate());
        try {
            date.setText(current.getDate());
        }catch (NullPointerException e){
            Log.i(TAG, "getView: here i am "+ e.getMessage());
        }

        final Button confirme= (Button) listItemView.findViewById(R.id.confirmeR);
        final Button decline= (Button) listItemView.findViewById(R.id.declineR);

        try {
            confirme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClickAction: "+sessionManager.getClient().getProductsReserve().get(position).getIdReserve());
                    actionOnClick(R.layout.dialog_costum,position);
                }
            });
        }catch (Exception e){
        }
        try {
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClickAction: "+sessionManager.getClient().getProductsReserve().get(position).getIdReserve());
                    actionOnClick(R.layout.cancel_daialog,position);
                }
            });
        }catch (Exception e){
        }
        return listItemView;

    }

    public void actionOnClick(int id,int position){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(id);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RadioGroup group=dialog.findViewById(R.id.group);
        Button mActionOk = dialog.findViewById(R.id.butn_ok);Button mActionCancel = dialog.findViewById(R.id.butn_cancel);
        final EditText mInput = dialog.findViewById(R.id.cmnt);
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=group.getCheckedRadioButtonId();
                switch (temp){
                    case R.id.verybad:
                        avis="very bad";
                        break;
                    case R.id.bad:
                        avis="bad";
                        break;
                    case R.id.normal:
                        avis="normal";
                        break;
                    case R.id.good:
                        avis="good";
                        break;
                    case R.id.verygood:
                        avis="very good";
                        break;
                    default:
                        avis="";
                        break;
                }
                cmnt=mInput.getText().toString();
                if (id==R.layout.cancel_daialog){
                    request.decline(cmnt,avis,sessionManager.getClient().getProductsReserve().get(position).getIdReserve(), new MyRequest.RegisterCallBack() {
                        @Override
                        public void onSuccess(String message) {
                            remove(getItem(position));
                            notifyDataSetChanged();
                        }

                        @Override
                        public void inputErr(Map<String, String> errors) {

                        }

                        @Override
                        public void onErr(String message) {

                        }
                    });
                }else{
                    request.Confirmer("reserve",cmnt,avis,sessionManager.getClient().getProductsReserve().get(position).getIdReserve(), new MyRequest.RegisterCallBack() {
                        @Override
                        public void onSuccess(String message) {
                            dialog.dismiss();
                            remove(getItem(position));
                            notifyDataSetChanged();
                        }

                        @Override
                        public void inputErr(Map<String, String> errors) {

                        }

                        @Override
                        public void onErr(String message) {

                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
