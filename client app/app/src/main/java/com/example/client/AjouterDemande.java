package com.example.client;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
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

public class AjouterDemande extends AppCompatActivity {
    JSONArray jsonArray= null;
    SessionManager sessionManager;
    AllProdutsManager produtsManager;
    private RequestQueue queue;
    private MyRequest request;
    private Activity context;
    private GetPromo getPromo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_demande);
        context= this;

        produtsManager=new AllProdutsManager();
        Spinner spinner = (Spinner) findViewById(R.id.categ);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categProd,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        sessionManager=new SessionManager(this);
        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);

        if (sessionManager.isLogged()){

            getPromo = new GetPromo("https://rzbusinessma.000webhostapp.com/Test/getPromo.php");
            getPromo.execute();
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
                    GridView gridView = (GridView) findViewById(R.id.gridViewAddDemande);
                    gridView.setAdapter(itemAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent =new Intent(context, chosingQuantiteForDemande.class);
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
                    for (int i = 0; i <jsonArray.length() ; i++) {
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

    class GetPromo extends AsyncTask<Void, Void, String> {
        String urlWebService;
        public GetPromo(String urlWebService){
            this.urlWebService=urlWebService;
        }
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
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String nom=jsonObject.getString("nomProduit").trim();
                    String categ=jsonObject.getString("categ").trim();
                    String image=jsonObject.getString("image").trim();
                    String imagePromo=jsonObject.getString("imgPromo").trim();
                    int idP=jsonObject.getInt("idProduit");
                    int percent=jsonObject.getInt("percent");
                    float prix=(float) jsonObject.getLong("prix");
                    produtsManager.addPromo(new PromoItem(nom,prix,image,categ,idP,percent,imagePromo));
                }

            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;

            Dialog dialog =new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.promo_dialog);
            Button buttonOk= dialog.findViewById(R.id.btn_close);
            ListView listView= dialog.findViewById(R.id.liste_promo);
            PromoAdapter adapter=new PromoAdapter(context,produtsManager.getPromoItems());
            Log.i("TAG", "my_info: size"+produtsManager.getPromoItems().size());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PromoItem promoItem= produtsManager.getPromoItems().get(position);
                    Intent intent=new Intent(context,chosingQuantiteForDemande.class);
                    intent.putExtra("nom",promoItem.getName());
                    intent.putExtra("prix",promoItem.getPrix());
                    intent.putExtra("idP",promoItem.getIdProduit()+"");
                    intent.putExtra("image",promoItem.getImage());
                    intent.putExtra("unity",promoItem.getCateg());
                    intent.putExtra("percent",promoItem.getPercent());
                    dialog.dismiss();
                    startActivity(intent);
                }
            });
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setLayout((6 * width)/7, WindowManager.LayoutParams.WRAP_CONTENT);

        }
    }


    @Override
    protected void onDestroy() {
        if (getPromo!=null){
            getPromo.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (getPromo!=null){
            getPromo.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPromo == null){
            getPromo = new GetPromo("https://rzbusinessma.000webhostapp.com/Test/getPromo.php");
            getPromo.execute();
        }
    }
}
