package com.ewha.pumpkin.order;

public class OrderLine {
    private Product product;
    private Money price;
    private int quantity; // (2)
    private Money amounts;

    public OrderLine(Product product, Money price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private Money calculateAmounts() { // (4)
        return price.multiply(quantity);
    }

    public int getAmounts() {
        return amounts.getValue();
    }
}
