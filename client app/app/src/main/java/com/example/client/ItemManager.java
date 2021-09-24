package com.example.client;

import java.util.ArrayList;

public class ItemManager {
    ArrayList<Item> items;

    public ItemManager(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
