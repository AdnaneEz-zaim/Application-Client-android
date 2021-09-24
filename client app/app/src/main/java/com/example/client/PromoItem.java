package com.example.client;

public class PromoItem extends Item {
    private int percent;
    private String imagePromo;

    public PromoItem(String name, double prix, String image, String categ, int idProduit,int percent,String imagePromo) {
        super(name, prix, image, categ, idProduit);
        this.percent=percent;
        this.imagePromo=imagePromo;
    }

    public int getPercent() {
        return percent;
    }
    public String getImagePromo() {
        return imagePromo;
    }
}
