package com.example.myapplication;

import android.util.Log;

public class Order {

    public FoodItem [] list = {
        // Mains
        new FoodItem("Hamburger", 250, 0), // 0
        new FoodItem("Taco", 250, 0), // 1
        new FoodItem("Wrap", 250, 0), // 2
        new FoodItem("Chicken", 200, 0), // 3
        new FoodItem("Toast", 100, 0), // 4
        new FoodItem("Pizza", 150, 0), // 5

        // Sides
        new FoodItem("Fries", 150, 0), // 6
        new FoodItem("Onion rings", 150, 0), // 7
        new FoodItem("Sticks", 200, 0), // 8
        new FoodItem("Nuggets", 200, 0), // 9
        new FoodItem("Chicken wings", 250, 0), // 10
        new FoodItem("Salad", 250, 0), // 11

        // Beverages
        new FoodItem("Coke", 200, 0), // 12
        new FoodItem("Sprite", 200, 0), // 13
        new FoodItem("Water", 100, 0), // 14
        new FoodItem("Fanta", 200, 0), // 15
        new FoodItem("Tea", 150, 0), // 16
        new FoodItem("Beer", 250, 0), // 17

        // Desserts
        new FoodItem("Cake", 200, 0), // 18
        new FoodItem("Donut", 150, 0), // 19
        new FoodItem("Milkshake", 150, 0), // 20
        new FoodItem("Crepes", 300, 0), // 21
        new FoodItem("Ice cream", 100, 0), // 22
        new FoodItem("Pancake", 200, 0) // 23
    };

    public String getOrderText() {
        StringBuilder str = new StringBuilder();

        for (FoodItem foodItem : list) {
            str.append(foodItem.getOrderString());
        }
        
        return str.toString();
    }

    public int getTotal() {
        int total = 0;

        for (FoodItem foodItem : list) {
            total += foodItem.getSubtotal();
        }

        return total;
    }

    public FoodItem getFoodItem(String name) {
        for (FoodItem foodItem : list) {
            if (foodItem.getName().equalsIgnoreCase(name)){
                return foodItem;
            }
        }
        return new FoodItem("null", 0, 0);
    }

    public String getMissingCategory() {
        String missingCategory = "Mains";

        // Mains
        int i=0;
        for (; i<6; ++i) {
            if (list[i].number > 0) {
                missingCategory = "Sides";
                break;
            }
        }
        if (missingCategory.equals("Mains")) return missingCategory;

        // Sides
        for (i=6; i<12; ++i) {
            if (list[i].number > 0) {
                missingCategory = "Beverages";
                break;
            }
        }
        if (missingCategory.equals("Sides")) return missingCategory;

        // Beverages
        for (i=12; i<18; ++i) {
            if (list[i].number > 0) {
                missingCategory = "Desserts";
                break;
            }
        }
        if (missingCategory.equals("Beverages")) return missingCategory;

        // Desserts
        for (i=18; i<24; ++i) {
            if (list[i].number > 0) {
                missingCategory = "Null";
                break;
            }
        }

        return missingCategory;
    }
}

