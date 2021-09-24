package com.example.client;

import java.util.Vector;

public class AllProdutsManager {
    Vector<AllProducts> prods;
    Vector<PromoItem> promoItems;

    public AllProdutsManager(Vector<AllProducts> prods) {
        this.prods = prods;
    }

    public AllProdutsManager() {
        prods=new Vector<AllProducts>();
        promoItems=new Vector<PromoItem>();
    }

    public void addProd(AllProducts product){
        if (! prods.contains(product))
            prods.add(product);
    }

    public void addPromo(PromoItem promo){
        if (! promoItems.contains(promo))
            promoItems.add(promo);
    }

    public Vector<AllProducts> getProds() {
        return prods;
    }

    public Vector<PromoItem> getPromoItems() {
        return promoItems;
    }

    public void setPromoItems(Vector<PromoItem> promoItems) {
        this.promoItems = promoItems;
    }

    public void setProds(Vector<AllProducts> prods) {
        this.prods = prods;
    }

    public Vector<AllProducts> getProdsByCateg(String categ) {
        Vector<AllProducts> toReturn=new Vector<AllProducts>();
        for (AllProducts prod: prods) {
            if (prod.getCategProd().equals(categ))
                toReturn.add(prod);
        }
        return toReturn;
    }
}
