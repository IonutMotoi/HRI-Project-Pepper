package com.example.myapplication;

public class FoodItem {
    final public String name;
    final public int price;
    public int number;

    public FoodItem(String name, int price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public String num2str() {
        return String.valueOf(number);
    }

}


