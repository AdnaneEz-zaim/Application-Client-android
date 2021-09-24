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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProduitDemandeAdapter extends ArrayAdapter<ProduitDemande> {
    private Context context;
    private RequestQueue queue;
    private MyRequest request;
    private SessionManager sessionManager;
    private String cmnt;
    private String avis;
    JSONArray jsonArray= null;
    RequestOptions option;
    onDeleteListner listner;
    ArrayList<ProduitDemande> correntListProds;


    public interface onDeleteListner{
        void onDelete(int position);
    }

    public void setOnItemDeleteListner(onDeleteListner listner){
        this.listner=listner;
    }


    public ProduitDemandeAdapter(Activity context, ArrayList<ProduitDemande> androidFlavors) {

        super(context, 0, androidFlavors);
        this.context = context;
        option = new RequestOptions().placeholder(R.drawable.white_shape).error(R.drawable.white_shape);
        correntListProds=androidFlavors;

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater ();
        View row = inflater.inflate(R.layout.produit_demande,parent,false);
        View listItemView = convertView;


        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.produit_demande, parent, false);
        }


        queue = VolleySinglton.getInstance(context).getRequestQueue();
        request=new MyRequest(context,queue);
        sessionManager=new SessionManager(context);


        final ProduitDemande currentAndroidFlavor = getItem(position);
        TextView nomProduit = (TextView) listItemView.findViewById(R.id.nomProd);
        nomProduit.setText(currentAndroidFlavor.getNomProd());


        TextView quantite = (TextView) listItemView.findViewById(R.id.quantProd);
        quantite.setText(""+currentAndroidFlavor.getQuantiteProd());


        ImageView img = (ImageView) listItemView.findViewById(R.id.imageProd);
        //img.setImageResource(currentAndroidFlavor.getImageProd());
        Glide.with(context).load(currentAndroidFlavor.getImageProd()).apply(option).into(img);


        Button b1 = (Button) listItemView.findViewById(R.id.confirme);
        b1.setBackgroundResource(currentAndroidFlavor.getIcon().getIconConfirme());

        TextView price = (TextView) listItemView.findViewById(R.id.prix);
        price.setText((currentAndroidFlavor.getPrice()-(currentAndroidFlavor.getPrice()*currentAndroidFlavor.getPromo()/100))*currentAndroidFlavor.getQuantiteProd()+"");


        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_costum);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RadioGroup group=dialog.findViewById(R.id.group);
        Button mActionOk = dialog.findViewById(R.id.butn_ok);
        Button mActionCancel = dialog.findViewById(R.id.butn_cancel);
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
                        avis="no avis";
                        break;
                }
                cmnt=(!mInput.getText().toString().equals("")) ? mInput.getText().toString() : "no comment";
                ProdutsAllreadyDemande produitDemande=sessionManager.getClient().getProductsDemande().get(position);
                request.Confirmer("demande",cmnt,avis,sessionManager.getClient().getProductsDemande().get(position).getIdDemande(), new MyRequest.RegisterCallBack() {
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
                dialog.dismiss();
            }
        });


        final Button confirme= (Button) listItemView.findViewById(R.id.confirme);

        try {
            confirme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }catch (Exception e){
            Log.i(TAG, "getView: "+e.getMessage());
        }
        return listItemView;
    }

}
