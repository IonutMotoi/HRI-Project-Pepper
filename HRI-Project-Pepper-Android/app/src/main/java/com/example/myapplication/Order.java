package com.example.myapplication;

public class Order {
    //Main
    public FoodItem hamburger = new FoodItem("Hamburger", 250, 0);
    public FoodItem taco = new FoodItem("Taco", 250, 0);
    public FoodItem wrap = new FoodItem("Wrap", 250, 0);
    public FoodItem chicken = new FoodItem("Chicken wings", 200, 0);
    public FoodItem toast = new FoodItem("Toast", 100, 0);
    public FoodItem pizza = new FoodItem("Pizza", 150, 0);

    //Sides
    public FoodItem fries = new FoodItem("Fries", 150, 0);
    public FoodItem onion = new FoodItem("Onion rings", 150, 0);
    public FoodItem sticks = new FoodItem("Sticks", 200, 0);
    public FoodItem nuggets = new FoodItem("Nuggets", 200, 0);
    public FoodItem wings = new FoodItem("Chicken wings", 250, 0);
    public FoodItem salad = new FoodItem("Salad", 250, 0);

    //Beverages
    public FoodItem coke = new FoodItem("Coke", 200, 0);
    public FoodItem sprite = new FoodItem("Sprite", 200, 0);
    public FoodItem water = new FoodItem("Water", 100, 0);
    public FoodItem fanta = new FoodItem("Fanta", 200, 0);
    public FoodItem tea = new FoodItem("Tea", 150, 0);
    public FoodItem beer = new FoodItem("Beer", 250, 0);

    //Desserts
    public FoodItem cake = new FoodItem("Cake", 200, 0);
    public FoodItem donut = new FoodItem("Donut", 150, 0);
    public FoodItem milkshake = new FoodItem("Milkshake", 150, 0);
    public FoodItem crepes = new FoodItem("Crepes", 300, 0);
    public FoodItem icecream = new FoodItem("Ice Cream", 100, 0);
    public FoodItem pancake = new FoodItem("Pancake", 200, 0);

    public String getOrderText() {
        String str = "";

        str += hamburger.getOrderString() + taco.getOrderString()
                + wrap.getOrderString() + chicken.getOrderString()
                + toast.getOrderString() + pizza.getOrderString();

        str += fries.getOrderString() + onion.getOrderString()
                + sticks.getOrderString() + nuggets.getOrderString()
                + wings.getOrderString() + salad.getOrderString();

        str += coke.getOrderString() + sprite.getOrderString()
                + water.getOrderString() + fanta.getOrderString()
                + tea.getOrderString() + beer.getOrderString();

        str += cake.getOrderString() + donut.getOrderString()
                + milkshake.getOrderString() + crepes.getOrderString()
                + icecream.getOrderString() + pancake.getOrderString();

        return str;
    }

    public int getTotal() {
        int total = 0;

        total += hamburger.getSubtotal() + taco.getSubtotal()
                + wrap.getSubtotal() + chicken.getSubtotal()
                + toast.getSubtotal() + pizza.getSubtotal();

        total += fries.getSubtotal() + onion.getSubtotal()
                + sticks.getSubtotal() + nuggets.getSubtotal()
                + wings.getSubtotal() + salad.getSubtotal();

        total += coke.getSubtotal() + sprite.getSubtotal()
                + water.getSubtotal() + fanta.getSubtotal()
                + tea.getSubtotal() + beer.getSubtotal();

        total += cake.getSubtotal() + donut.getSubtotal()
                + milkshake.getSubtotal() + crepes.getSubtotal()
                + icecream.getSubtotal() + pancake.getSubtotal();

        return total;
    }
}

