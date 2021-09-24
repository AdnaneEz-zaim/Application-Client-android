package com.example.client;

public class ProdutsAllreadyDemande {
    private String nomProduit;
    private String categProduit;
    private String imagePath;
    private int quantiteProduit;
    private int idClient;
    private int idVendeur;
    private int idDemande;
    private int idProduit;
    private float prixProduit;
    private boolean checked;
    private int promo;



    public ProdutsAllreadyDemande(String nomProduit, String categProduit, int quantiteProduit, int idClient, int idVendeur, int idDemande, int idProduit, float prixProduit, boolean checked,String imagePath,int promo) {
        this.nomProduit = nomProduit;
        this.categProduit = categProduit;
        this.quantiteProduit = quantiteProduit;
        this.idClient = idClient;
        this.idVendeur = idVendeur;
        this.idDemande = idDemande;
        this.idProduit = idProduit;
        this.prixProduit = prixProduit;
        this.checked=checked;
        this.imagePath=imagePath;
        this.promo=promo;
    }

    public boolean isChecked() {
        return checked;
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

    public int getIdDemande() {
        return idDemande;
    }

    public float getPrixProduit() {
        return prixProduit;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getPromo() {
        return promo;
    }
}
