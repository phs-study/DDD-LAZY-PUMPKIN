package com.ewha.pumpkin.order;

public class OrderLine {
    private Product product;
    private int price;
    private int quantity; // (2)
    private int amounts;

    public OrderLine(Product product, int price, int quantity, int amounts) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.amounts = amounts;
    }

    private int calculateAmounts() { // (4)
        return price * quantity;
    }

    public int getAmounts() {
        return amounts;
    }
}
