package com.ewha.pumpkin;

public enum OrderState {
    PAYMENT_WATTING {
        public boolean isShippingChangeable() {
            return true;
        };
    },
    PREPARING {
        public boolean isShippingChangeable() {
            return true;
        };
    },
    SHIPPED, DELIVERING, DELIVERY_COMPLETED;

    public boolean isShippingChangeable() {
        return false;
    }
}
