package com.example.client;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class chosingQuantiteForReserve extends AppCompatActivity {
    SessionManager sessionManager;
    private RequestQueue queue;
    private MyRequest request;
    RequestOptions option;
    private String date;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String idP;
    private String quant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosing_quantite_for_reserve);
        option = new RequestOptions().placeholder(R.drawable.white_shape).error(R.drawable.white_shape);

        sessionManager=new SessionManager(this);
        Intent intent=getIntent();
        String insertName=intent.getStringExtra("nom");
        String unity=intent.getStringExtra("unity");
        String insertImage=intent.getStringExtra("image");
        Double insertPrix=intent.getDoubleExtra("prix",0);
        idP=intent.getStringExtra("idP");

        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);


        ImageView imageView=(ImageView) findViewById(R.id.itemImg);
        TextView nom=(TextView) findViewById(R.id.prodName);
        TextView oldPrix=(TextView) findViewById(R.id.oldPrix);
        TextView prix=(TextView) findViewById(R.id.prix);
        TextView unityText=(TextView) findViewById(R.id.unity);
        Button submit=(Button) findViewById(R.id.submit);


        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);

        if (sessionManager.isLogged()){

            DecimalFormat df = new DecimalFormat("0.00");

            Glide.with(this).load(insertImage).apply(option).into(imageView);
            nom.setText(insertName);
            prix.setText(df.format(insertPrix-(insertPrix*0.05))+"");
            oldPrix.setText(df.format(insertPrix)+"");
            oldPrix.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
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
                    request.addProductsReserve(date,longitude,latitude,idP, qunt[0], sessionManager.getClient().getId_Client(), 3, new MyRequest.RegisterCallBack() {
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


        mDisplayDate = (TextView) findViewById(R.id.calendar);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        chosingQuantiteForReserve.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = day + ","+month + "," + year;
                mDisplayDate.setText(day + "/"+month + "/" + year);
            }
        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            assert data != null;
            String lat = data.getStringExtra("lat");
            String lng = data.getStringExtra("lng");
            sendAndGoback(lng,lat);
        }
    }

    private void sendAndGoback(String longitude, String latitude) {

        request.addProductsReserve(date,longitude,latitude,idP, quant, sessionManager.getClient().getId_Client(), new MyRequest.RegisterCallBack() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(),"Réservation est ajouté",Toast.LENGTH_LONG).show();
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
    }

    public void espaceClient(View view){
        finish();
    }
}
