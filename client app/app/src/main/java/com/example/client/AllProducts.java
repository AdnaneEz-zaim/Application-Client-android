package com.example.client;

public class AllProducts {
    private int idProd;
    private String nomProd;
    private String categProd;
    private float prixProd;
    private String imagePath;

    public AllProducts(int idProd, String nomProd, String categProd, float prixProd,String imagePath) {
        this.idProd = idProd;
        this.nomProd = nomProd;
        this.categProd = categProd;
        this.prixProd = prixProd;
        this.imagePath=imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getIdProd() {
        return idProd;
    }

    public String getNomProd() {
        return nomProd;
    }

    public String getCategProd() {
        return categProd;
    }

    public float getPrixProd() {
        return prixProd;
    }
}
