package com.example.client;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class chosingQuantiteForDemande extends AppCompatActivity {
    SessionManager sessionManager;
    RequestOptions option;
    private RequestQueue queue;
    private MyRequest request;
    int PLACE_PICKER_REQUEST=1;
    private String idP;
    private String quant;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosing_quantite_for_demande);
        option = new RequestOptions().placeholder(R.drawable.white_shape).error(R.drawable.white_shape);

        sessionManager=new SessionManager(this);
        Intent intent=getIntent();
        String insertName=intent.getStringExtra("nom");
        String unity=intent.getStringExtra("unity");
        String insertImage=intent.getStringExtra("image");
        double insertPrix=intent.getDoubleExtra("prix",0);
        idP=intent.getStringExtra("idP");

        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);


        ImageView imageView=(ImageView) findViewById(R.id.itemImg);
        TextView nom=(TextView) findViewById(R.id.prodName);
        TextView Oldprix=(TextView) findViewById(R.id.oldPrix);
        TextView OldprixDH=(TextView) findViewById(R.id.oldPrixDH);
        TextView unityText=(TextView) findViewById(R.id.unity);
        submit=(Button) findViewById(R.id.submit);


        DecimalFormat df = new DecimalFormat("0.0");

        //for promo
        if (intent.hasExtra("percent")){
            int percent=intent.getIntExtra("percent",0);
            LinearLayout promoLayout = (LinearLayout) findViewById(R.id.promoLayout);
            TextView newPrix = (TextView) findViewById(R.id.prix);
            TextView promoPercent = (TextView) findViewById(R.id.percent);
            ImageView promoIcon = (ImageView) findViewById(R.id.promoIcon);
            promoLayout.setVisibility(View.VISIBLE);
            promoIcon.setVisibility(View.VISIBLE);
            newPrix.setText(df.format(insertPrix-(insertPrix*percent/100))+"");
            promoPercent.setText("-"+percent+"%");
            Oldprix.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            OldprixDH.setVisibility(View.GONE);
        }


        if (sessionManager.isLogged()){

            Glide.with(this).load(insertImage).apply(option).into(imageView);
            nom.setText(insertName);
            Oldprix.setText(df.format(insertPrix)+"");
            unityText.setText(unity);

            Spinner spinner = (Spinner) findViewById(R.id.quant);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.quantite,android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);


            final int[] qunt = new int[1];
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            qunt[0] =1;
                            break;
                        case 1:
                            qunt[0]=2;
                            break;
                        case 2:
                            qunt[0]=5;
                            break;
                        case 3:
                            qunt[0]=10;
                            break;
                        case 4:
                            qunt[0]=20;
                            break;
                        case 5:
                            qunt[0]=50;
                            break;
                        case 6:
                            qunt[0]=100;
                            break;
                        default:
                            qunt[0]=0;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quant=qunt[0]+"";
                    Intent intent =new Intent(getApplicationContext(),MapView.class);
                    startActivityForResult(intent, 2);
                    /*
                    intent.putExtra("idP",idP+"");
                    intent.putExtra("quant",qunt[0]+"");
                    intent.putExtra("idC",sessionManager.getClient().getId_Client()+"");
                    /*
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(chosingQuantiteForDemande.this),PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    //
                    request.addProductsDemande(idP, qunt[0], sessionManager.getClient().getId_Client(), 3, new MyRequest.RegisterCallBack() {
                        @Override
                        public void onSuccess(String message) {
                            Intent intent =new Intent(getApplicationContext(),DemandeClient.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void inputErr(Map<String, String> errors) {
                        }

                        @Override
                        public void onErr(String message) {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                        }
                    });

                     */
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (data !=null) {
                if (data.hasExtra("lat")) {
                    String lat = data.getStringExtra("lat");
                    String lng = data.getStringExtra("lng");
                    submit.setClickable(false);
                    sendAndGoback(lng, lat);
                }else{
                    //DONTDO NOTH
                }
            }
        }
        /*
        Place place = PlacePicker.getPlace(data,this);
        latitude=String.valueOf(place.getLatLng().latitude);
        longitude=String.valueOf(place.getLatLng().longitude);
        Log.i("TAG", "onActivityResult: "+latitude+", "+longitude);

         */
    }

    private void sendAndGoback(String longitude,String latitude) {

        request.addProductsDemande(longitude,latitude,idP, quant,sessionManager.getClient().getId_Client()+"", new MyRequest.RegisterCallBack() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(),"Demande est ajouté",Toast.LENGTH_LONG).show();
                Intent intent =new Intent(chosingQuantiteForDemande.this,DemandeClient.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void inputErr(Map<String, String> errors) {
            }

            @Override
            public void onErr(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                submit.setClickable(true);
            }
        });
    }

    public void espaceClient(View view){
        finish();
    }
}
