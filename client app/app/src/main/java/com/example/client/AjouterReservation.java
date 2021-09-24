package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;

public class AjouterReservation extends AppCompatActivity {

    JSONArray jsonArray= null;
    SessionManager sessionManager;
    AllProdutsManager produtsManager;
    private RequestQueue queue;
    private MyRequest request;
    private Activity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajouter_reservation);

        context=this;
        produtsManager=new AllProdutsManager();
        Spinner spinner = (Spinner) findViewById(R.id.categ);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categProd,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        sessionManager=new SessionManager(this);
        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);

        if (sessionManager.isLogged()){
            getJSON("https://rzbusinessma.000webhostapp.com/Test/getProducts.php");
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ArrayList<Item> data = new ArrayList<Item>();
                    switch (position){
                        case 1:
                            data=showListe(produtsManager,"kg");
                            break;
                        case 2:
                            data=showListe(produtsManager,"l");
                            break;
                        case 3:
                            data=showListe(produtsManager,"unité");
                            break;
                    }
                    sessionManager.setItems(new ItemManager(data));
                    ItemAdapter itemAdapter = new ItemAdapter(context,data);
                    GridView gridView = findViewById(R.id.listviewAddReserve);
                    gridView.setAdapter(itemAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent =new Intent(context, chosingQuantiteForReserve.class);
                            intent.putExtra("nom",sessionManager.getItems().getItems().get(position).getName());
                            intent.putExtra("prix",sessionManager.getItems().getItems().get(position).getPrix());
                            intent.putExtra("idP",sessionManager.getItems().getItems().get(position).getIdProduit()+"");
                            intent.putExtra("image",sessionManager.getItems().getItems().get(position).getImage());
                            intent.putExtra("unity",sessionManager.getItems().getItems().get(position).getCateg());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

    public void espaceClient(View view){
        finish();
    }

    public ArrayList<Item> showListe(AllProdutsManager manager,String categ){
        ArrayList<Item> data = new ArrayList<Item>();
        data.clear();

        Vector<AllProducts> produts = manager.getProdsByCateg(categ);
        for (int i = 0; i <produts.size() ; i++) {
            data.add(new Item(produts.get(i).getNomProd(),produts.get(i).getPrixProd(),produts.get(i).getImagePath(),produts.get(i).getCategProd(),produts.get(i).getIdProd()));
        }

        return data;
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                String json="";
                try {
                    URL url = new URL(urlWebService);
                    URLConnection con = url.openConnection();
                    InputStreamReader inputStreamReader=new InputStreamReader(con.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    json=bufferedReader.readLine();
                    jsonArray=new JSONArray(json);
                    for (int i = 0; i <jsonArray.length()-1 ; i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nom=jsonObject.getString("nom_produit").trim();
                        String categ=jsonObject.getString("categorie").trim();
                        String image=jsonObject.getString("image").trim();
                        int idP=jsonObject.getInt("id_Produit");
                        float prix=(float) jsonObject.getLong("prix");
                        produtsManager.addProd(new AllProducts(idP,nom,categ,prix,image));
                    }

                } catch (Exception e) {
                    return null;
                }
                return null;
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

}
