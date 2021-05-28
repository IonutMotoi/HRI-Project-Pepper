package com.example.myapplication;

public class FoodItem {
    final private String name;
    final private int price;
    public int number;

    public FoodItem(String name, int price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public String num2str() {
        return String.valueOf(number);
    }

    public int getSubtotal() {
        return price * number;
    }

    public String getOrderString() {
        String str = "";
        if(number > 0) {
            str = name + " x"  + number + " --> "
                    + String.format("%.2f", getSubtotal() / 100.0f) + " €\n";
        }
        return str;
    }

}


