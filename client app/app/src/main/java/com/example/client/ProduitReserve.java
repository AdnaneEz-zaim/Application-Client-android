package com.example.client;

public class ProduitReserve {
    private String nomProd;
    private float quantiteProd;
    private String imageProd;
    private double price;
    private IconMaker icon;
    private String date;

    public ProduitReserve(String nomProd, float quantiteProd, boolean clickedConfirme, boolean clickedDecline, String imageProd, double price,int day,int mounth,int year) {
        this.nomProd = nomProd;
        this.quantiteProd = quantiteProd;
        icon = new IconMaker(clickedConfirme,clickedDecline);
        this.imageProd = imageProd;
        this.price = price;
        date = new String(day+"/"+mounth+"/"+year);

    }

    public String getNomProd() {
        return nomProd;
    }

    public float getQuantiteProd() {
        return quantiteProd;
    }

    public String  getImageProd() {
        return imageProd;
    }

    public double getPrice() {
        return price;
    }

    public IconMaker getIcon() {
        return icon;
    }
    public String getDate(){
        return date;
    }

}
