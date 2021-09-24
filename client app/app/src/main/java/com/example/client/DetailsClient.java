package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsClient extends AppCompatActivity {
    TextView b;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_client);
        TextView nom = (TextView) findViewById(R.id.nom);
        TextView prenom = (TextView) findViewById(R.id.prenom);
        TextView email = (TextView) findViewById(R.id.email);
        TextView tele = (TextView) findViewById(R.id.tele);
        TextView adrr = (TextView) findViewById(R.id.adresse);

        sessionManager=new SessionManager(this);
        if (sessionManager.isLogged()){
            Client client=sessionManager.getClient();
            nom.setText(client.getNom());
            prenom.setText(client.getPrenom());
            email.setText(client.getEmail());
            tele.setText(client.getTele());
            adrr.setText(client.getAdrr());
        }

        b = findViewById(R.id.deconnect);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logout();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });


    }
    public void espaceClient(View view){
        startActivity(new Intent(getApplicationContext(),DemandeClient.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DemandeClient.class));
        finish();
        super.onBackPressed();
    }
}
