package com.example.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class MyRequest {

    private Context context;
    private RequestQueue queue;
    private StringRequest request;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void register(final String fname, final String lname, final String adrr, final String email, final String tele, final String pass, final String conf, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/reg.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String,String> errors = new HashMap<>();
                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        callBack.onSuccess("vous étes bien enregestrer !");
                    }else{
                        Log.i("tagconvertstr", "["+response+"]");
                        JSONObject messages = (JSONObject) json.getJSONObject("messages");
                        if (messages.has("lname"))
                            errors.put("lname",messages.getString("lname"));
                        if (messages.has("email"))
                            errors.put("email",messages.getString("email"));
                        if (messages.has("tele"))
                            errors.put("tele",messages.getString("tele"));
                        if (messages.has("conf"))
                            errors.put("conf",messages.getString("conf"));
                    }
                    callBack.inputErr(errors);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("fname", fname);
                map.put("lname", lname);
                map.put("adrr", adrr);
                map.put("email", email);
                map.put("tele", tele);
                map.put("pass", pass);
                map.put("conf", conf);

                return map;
            }
        };
        request.setTag(TAG);
        queue.add(request);

    }

    public void verify(final String fname, final String lname, final String adrr, final String email, final String tele, final String pass, final String conf, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/verifyInfos.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String,String> errors = new HashMap<>();
                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        callBack.onSuccess("vous étes bien enregestrer !");
                    }else{
                        Log.i("tagconvertstr", "["+response+"]");
                        JSONObject messages = (JSONObject) json.getJSONObject("messages");
                        if (messages.has("lname"))
                            errors.put("lname",messages.getString("lname"));
                        if (messages.has("email"))
                            errors.put("email",messages.getString("email"));
                        if (messages.has("tele"))
                            errors.put("tele",messages.getString("tele"));
                        if (messages.has("conf"))
                            errors.put("conf",messages.getString("conf"));
                    }
                    callBack.inputErr(errors);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("fname", fname);
                map.put("lname", lname);
                map.put("adrr", adrr);
                map.put("email", email);
                map.put("tele", tele);
                map.put("pass", pass);
                map.put("conf", conf);

                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }

    public interface RegisterCallBack{
        void onSuccess(String message);
        void inputErr(Map<String,String> errors);
        void onErr(String message);
    }
    public interface LoginCallBack{
        void onSuccess(Client client);
        void onErr(String message);
    }
    public interface PDCallBack{
        void onSuccess(Client client);
        void onErr(String message);
    }
    public interface PCallBack{
        void onSuccess(AllProdutsManager produtsManager);
        void onErr(String message);
    }

    public void connexion(final String mail, final String pass, final LoginCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/login.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        int idU = json.getInt("idU");
                        int idC = json.getInt("idC");
                        String mail = json.getString("mail");
                        String nom = json.getString("nom");
                        String prenom = json.getString("prenom");
                        String tele = json.getString("tele");
                        String adrr = json.getString("adrr");
                        String profileImg = json.getString("profileImg");
                        float x =(float) json.getDouble("x");
                        float y =(float) json.getDouble("y");
                        Log.i(TAG, "onResponse1: "+profileImg);
                        callBack.onSuccess(new Client(idC,idU,mail,nom,prenom,tele,adrr,profileImg));
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("mail", mail);
                map.put("pass", pass);

                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }

    public void loadProductsDemandes(final Client client,final PDCallBack callBack){
        String url = "https://rzbusinessma.000webhostapp.com/Test/getDemandeProduits.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONArray jsonArray= new JSONArray(response);
                    for (int i = 0; i <jsonArray.length()-1 ; i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nom=jsonObject.getString("nomProduit").trim();
                        String categ=jsonObject.getString("categ").trim();
                        String img=jsonObject.getString("img").trim();
                        int idP=jsonObject.getInt("idProduit");
                        int idD=jsonObject.getInt("idD");
                        int quantite=jsonObject.getInt("quantite");
                        int idV=jsonObject.getInt("idVendeur");
                        int promo=jsonObject.getInt("promo");
                        float prix=(float) jsonObject.getLong("prix");
                        client.addToPrductsDemandes(new ProdutsAllreadyDemande(nom,categ,quantite,client.getId_Client(),idV,idD,idP,prix,true,img,promo));
                    }
                    callBack.onSuccess(client);
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idClient", client.getId_Client()+"");
                return map;
            }
        };
        request.setTag(TAG);
        queue.add(request);
    }

    public void loadProductsReserves(final Client client,final PDCallBack callBack){
        String url = "https://rzbusinessma.000webhostapp.com/Test/getReserveProduits.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                client.clearReserves();
                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONArray jsonArray= new JSONArray(response);
                    for (int i = 0; i <jsonArray.length()-1 ; i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nom=jsonObject.getString("nomProduit").trim();
                        String categ=jsonObject.getString("categ").trim();
                        String img=jsonObject.getString("img").trim();
                        String date=jsonObject.getString("date");
                        int idP=jsonObject.getInt("idProduit");
                        int idD=jsonObject.getInt("idD");
                        int quantite=jsonObject.getInt("quantite");
                        int idV=jsonObject.getInt("idVendeur");
                        float prix=(float) jsonObject.getLong("prix");
                        client.addToPrductsReserves(new ProductsAllreadyReserved(nom,categ,quantite,client.getId_Client(),idV,idD,idP,prix,date,true,true,img));
                    }
                    callBack.onSuccess(client);
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idClient", client.getId_Client()+"");
                return map;
            }
        };
        request.setTag(TAG);
        queue.add(request);
    }

    /*
    public void loadAllProducts(final AllProdutsManager manager, final PCallBack callBack){

        Log.i("APP", "hello me: ");
        String url = "https://rzbusinessma.000webhostapp.com/Test/getProducts.php";

        Log.i("APP", "hello you: ");

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.i("APP", "hello us: ");
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONArray jsonArray= new JSONArray(response);
                    for (int i = 0; i <jsonArray.length()-1 ; i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nom=jsonObject.getString("nom_produit").trim();
                        String categ=jsonObject.getString("categorie").trim();
                        int idP=jsonObject.getInt("id_Produit");
                        float prix=(float) jsonObject.getLong("prix");
                        manager.addProd(new AllProducts(idP,nom,categ,prix));
                    }
                    callBack.onSuccess(manager);
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idClient", "");
                return map;
            }
        };
        queue.add(request);
    }


     */

    public void addProductsDemande(final String longitude, final String latitude, final String idP, final String quant, final String idC, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/addDemande.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    Log.i(TAG, "onResponse: this "+response);
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        callBack.onSuccess("Bien ajouté");
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("quantite", quant);
                map.put("idC", idC);
                map.put("lat", latitude);
                map.put("lng", longitude);
                map.put("idP", idP);
                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }


    public void addProductsReserve(final String date,final String longitude, final String latitude,final String idP, final String quant, final int idC, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/addReserve.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {

                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        callBack.onSuccess("Bien ajouté");
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("quantite", quant+"");
                map.put("idC", idC+"");
                map.put("lat", latitude);
                map.put("lng", longitude);
                map.put("idP", idP+"");
                map.put("date", date);

                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }


    public void Confirmer(final String type,final String cmnt, final String avis, final int idD, final RegisterCallBack callBack){
        String url = null;
        if (type.equals("reserve")){
             url = "https://rzbusinessma.000webhostapp.com/Test/confirmerR.php";
        }else{
             url = "https://rzbusinessma.000webhostapp.com/Test/confirmerD.php";
        }

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {

                    Log.i(TAG, "nchof hna "+response);
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        callBack.onSuccess("Bien Confirmer");
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idD", idD+"");
                map.put("cmnt", cmnt);
                map.put("avis", avis);

                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }


    public void decline(final String cmnt, final String avis,final int idR, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/decline.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {

                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        callBack.onSuccess("Bien Rejeter");
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idR", idR+"");
                map.put("cmnt", cmnt);
                map.put("avis", avis);

                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }


    public void UploadProfileImg(final String image,final int idU, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/uploadProfileImg.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {

                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        callBack.onSuccess("Bien Ajouter");
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idU", idU+"");
                map.put("img", image);
                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }

    public void getImageLink(final int idU, final RegisterCallBack callBack){

        String url = "https://rzbusinessma.000webhostapp.com/Test/getImg.php";

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {

                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        callBack.onSuccess(json.getString("image").toString().trim());
                    }else{
                        callBack.onErr(json.getString("messages"));
                    }
                } catch (JSONException e) {
                    callBack.onErr("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    callBack.onErr("Impoussible de se connecter");
                else if (error instanceof VolleyError)
                    callBack.onErr("Une erreur s'est produite");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idU", idU+"");
                return map;
            }
        };

        request.setTag(TAG);
        queue.add(request);

    }

    public void kill(){
        if (queue!=null){
            queue.cancelAll(TAG);
        }
    }
}
