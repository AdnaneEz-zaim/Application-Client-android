package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout mail,pass;
    private RequestQueue queue;
    private MyRequest request;
    private SessionManager sessionManager;
    //private LocationManger locationManger;
    //code from github for geocoding
    //SamLocationRequestService samLocationRequestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager(this);
        //locationManger=new LocationManger(this,new LocationCallback());
        if (sessionManager.isLogged()){
            //getLastLocation();
            Intent intent = new Intent(getApplicationContext(), DemandeClient.class);
            startActivity(intent);
            finish();
        }


        mail = (TextInputLayout) findViewById(R.id.logmail);
        pass = (TextInputLayout) findViewById(R.id.logpass);
        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);
    }
    public void espaceClient(View view){
        String _mail = mail.getEditText().getText().toString().trim();
        String _pass = pass.getEditText().getText().toString().trim();

        if (_mail.length()>0 && _pass.length()>0) {
            request.connexion(_mail, _pass, new MyRequest.LoginCallBack() {
                @Override
                public void onSuccess(Client client) {
                    Log.i("APP", "onSuccess: "+client.getEmail()+"                 <=====");

                    sessionManager.insertClient(client);
                    Intent intent = new Intent(getApplicationContext(), DemandeClient.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onErr(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"Remplir tous les champs",Toast.LENGTH_LONG).show();
        }
    }

    public void newAccount(View view){
        Intent intent =new Intent(this,NewAccount.class);
        startActivity(intent);
    }

    /*
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (locationManger.checkPermissions()) {
            if (locationManger.isLocationEnabled()) {
                locationManger.mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    locationManger.requestNewLocationData();
                                } else {
                                    Log.i("TAG", "onComplete: "+location.getLatitude()+"");
                                    Log.i("TAG", "onComplete: "+location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            locationManger.requestPermissions();
        }
    }
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
        if (requestCode == locationManger.PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }

         */
    }
/*
    @Override
    public void onResume(){
        super.onResume();
        if (locationManger.checkPermissions()) {
            getLastLocation();
        }

    }

 */
}
