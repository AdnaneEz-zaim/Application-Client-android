package com.example.client;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private double prix;
    private String image;
    private Bitmap imageBitmap;
    private String categ;
    private int idProduit;
    public Item(String name, double prix, String image,String categ,int idProduit) {
        this.name = name;
        this.prix = prix;
        this.image = image;
        this.categ = categ;
        this.idProduit = idProduit;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public String getCateg() {
        return categ;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getName() {
        return name;
    }

    public double getPrix() {
        return prix;
    }

    public String getImage() {
        return image;
    }
}
