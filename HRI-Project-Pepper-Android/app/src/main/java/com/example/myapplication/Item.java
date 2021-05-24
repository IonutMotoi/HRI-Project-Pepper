package com.example.myapplication;

public class Item {
    private String name;
    private int number;
    private float price;

    public Item(String name, int number, float price) {
        this.name = name;
        this.number = number;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }
}


