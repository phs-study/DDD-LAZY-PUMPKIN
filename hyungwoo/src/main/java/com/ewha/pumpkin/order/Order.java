package com.ewha.pumpkin.order;

import java.util.List;

public class Order {
    private String orderNumber;
    private OrderState state;
    private ShippingInfo shippingInfo;
    private List<OrderLine> orderLines;
    private Money totalAmounts;

    public Order(List<OrderLine> orderLines, ShippingInfo shippingInfo) {
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo); // (5)
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        if (shippingInfo == null) {
            throw new IllegalArgumentException("no ShippingInfo");
        }
        this.shippingInfo = shippingInfo;
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyLeastOneOrMoreOrderLines(orderLines); // (1)
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    private void calculateTotalAmounts() { // (3)
        int sum = orderLines.stream()
                .mapToInt(OrderLine::getAmounts)
                .sum();
        this.totalAmounts = new Money(sum);
    }

    private void verifyLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("no OrderLine");
        }
    }

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        verifyNotYetShipped();
        if (!isShippingChangeable()) {
            throw new IllegalArgumentException("can't change shipping in " + state);
        }
        this.shippingInfo = newShippingInfo;
    }

    public void cancel() {
        verifyNotYetShipped();
        this.state = OrderState.CANCELED;
    }

    private void verifyNotYetShipped() {
        if (state != OrderState.PAYMENT_WAITING && state != OrderState.PREPARING) { // (7), (8)
            throw new IllegalArgumentException("already shipped");
        }
    }

    private boolean isShippingChangeable() {
        return state == OrderState.PAYMENT_WAITING ||
                state == OrderState.PREPARING;
    }
}
