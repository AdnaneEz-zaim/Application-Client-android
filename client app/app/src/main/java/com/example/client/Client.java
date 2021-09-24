package com.example.client;

import java.util.Vector;

public class Client {
     private int id_Client;
     private int id_User;
     private String email;
     private String nom;
     private String prenom;
     private String tele;
     private String adrr;
     private String profileImg;
     private Vector<ProdutsAllreadyDemande> productsDemande;
     private Vector<ProductsAllreadyReserved> productsReserve;

    public Client() {
        productsDemande=new Vector<ProdutsAllreadyDemande>();
        productsReserve=new Vector<ProductsAllreadyReserved>();
    }

    public Client(int id_Client, int id_User, String email, String nom, String prenom, String tele, String adrr,String profileImg) {
        this.id_Client = id_Client;
        this.id_User = id_User;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.tele = tele;
        this.adrr = adrr;
        this.profileImg=profileImg;
        productsDemande=new Vector<ProdutsAllreadyDemande>();
        productsReserve=new Vector<ProductsAllreadyReserved>();
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public int getId_Client() {
        return id_Client;
    }

    public int getId_User() {
        return id_User;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTele() {
        return tele;
    }

    public String getAdrr() {
        return adrr;
    }

    public void addToPrductsDemandes(ProdutsAllreadyDemande prod){
        productsDemande.add(prod);
    }

    public void addToPrductsReserves(ProductsAllreadyReserved prod){
        productsReserve.add(prod);
    }

    public Vector<ProdutsAllreadyDemande> getProductsDemande(){
        return productsDemande;
    }
    public Vector<ProductsAllreadyReserved> getProductsReserve(){
        return productsReserve;
    }

    public void clearDemandes(){
        productsDemande.clear();
    }
    public void clearReserves(){
        productsReserve.clear();
    }
}
