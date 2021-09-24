package com.example.client;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static final String PREFS_NAMES= "app_prefs";
    private static final String IS_LOGGED= "isLogged";
    private static final int PRIVATE_MODE= 0;

    /*
    private static final String EMAIL= "email";
    private static final String ID= "id";
    private static final String PRENOM= "prenom";
    private static final String NOM= "nom";
    private static final String ADRESSE= "adresse";
    private static final String TELE= "tele";
    */

    private static final String CLIENT="client";
    private static final String PRODS="prods";
    private static final String ITEMS="items";
    Gson gson;

    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences(PREFS_NAMES,PRIVATE_MODE);
        editor=preferences.edit();
    }


    public boolean isLogged(){
        return preferences.getBoolean(IS_LOGGED,false);
    }

    public Client getClient(){
        gson=new Gson();
        String json = preferences.getString("client", null);
        return gson.fromJson(json, Client.class);
    }


    public void insertClient(Client client){
        editor.putBoolean(IS_LOGGED,true);
        gson= new Gson();
        String str=gson.toJson(client,Client.class);
        editor.putString(CLIENT,str);
        editor.apply();
    }

    public AllProdutsManager getProds() {
        gson=new Gson();
        String json = preferences.getString("prods", "");
        return gson.fromJson(json, AllProdutsManager.class);
    }

    public void setProds(AllProdutsManager prods){
        gson= new Gson();
        String str=gson.toJson(prods,AllProdutsManager.class);
        editor.putString(PRODS,str);
        editor.apply();
    }

    public ItemManager getItems() {
        gson=new Gson();
        String json = preferences.getString("items", "");
        return gson.fromJson(json, ItemManager.class);
    }

    public void setItems(ItemManager list){
        gson= new Gson();
        String str=gson.toJson(list,ItemManager.class);
        editor.putString(ITEMS,str);
        editor.apply();
    }

    public void logout(){
        editor.clear().commit();
    }


}
