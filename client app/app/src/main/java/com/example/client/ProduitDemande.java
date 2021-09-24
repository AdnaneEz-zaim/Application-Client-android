package com.example.client;

public class ProduitDemande {
    private String nomProd;
    private float quantiteProd;
    private String imageProd;
    private double price;
    private IconMaker icon;
    private int promo;


    public ProduitDemande(String nomProd, float quantiteProd, boolean clickedConfirme, String imageProd, double price, int promo) {
        this.nomProd = nomProd;
        this.quantiteProd = quantiteProd;
        icon = new IconMaker(clickedConfirme,false);
        this.imageProd = imageProd;
        this.price = price;
        this.promo=promo;
    }

    public String getNomProd() {
        return nomProd;
    }

    public float getQuantiteProd() {
        return quantiteProd;
    }

    public String getImageProd() {
        return imageProd;
    }

    public double getPrice() {
        return price;
    }

    public IconMaker getIcon() {
        return icon;
    }

    public int getPromo() {
        return promo;
    }
}
