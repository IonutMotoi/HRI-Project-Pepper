package com.example.myapplication;

public class FoodItem {
    final private String name;
    final private int price;
    private int number;

    public FoodItem(String name, int price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int newNumber) {
        number = newNumber;
    }
}


