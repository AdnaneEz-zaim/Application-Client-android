package com.example.client;

public class ProductsAllreadyReserved {
    private String nomProduit;
    private String categProduit;
    private String imagePath;
    private int quantiteProduit;
    private int idClient;
    private int idVendeur;
    private int idReserve;
    private int idProduit;
    private float prixProduit;
    private String date;
    private boolean checkedDecline;
    private boolean checkedConfirme;

    public ProductsAllreadyReserved(String nomProduit, String categProduit, int quantiteProduit, int idClient, int idVendeur, int idReserve, int idProduit, float prixProduit,String date,boolean checkedDecline,boolean checkedConfirme,String imagePath) {
        this.nomProduit = nomProduit;
        this.categProduit = categProduit;
        this.quantiteProduit = quantiteProduit;
        this.idClient = idClient;
        this.idVendeur = idVendeur;
        this.idReserve = idReserve;
        this.idProduit = idProduit;
        this.prixProduit = prixProduit;
        this.date=date;
        this.checkedConfirme=checkedConfirme;
        this.checkedDecline=checkedDecline;
        this.imagePath=imagePath;
    }

    public boolean isCheckedDecline() {
        return checkedDecline;
    }

    public boolean isCheckedConfirme() {
        return checkedConfirme;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public String getCategProduit() {
        return categProduit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getQuantiteProduit() {
        return quantiteProduit;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public int getIdReserve() {
        return idReserve;
    }

    public float getPrixProduit() {
        return prixProduit;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public String getDate() {
        return date;
    }
}
