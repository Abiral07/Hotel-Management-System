package com.SpringBootProject.hms.entity;

import java.math.BigDecimal;

public enum PaymentGateway {
    ESEWA(new BigDecimal(50000)),
    KHALTI(new BigDecimal(10000)),
    CIPS(new BigDecimal(500000)),
    IMEPAY(new BigDecimal(200000)),
    PRVUPAY(new BigDecimal(1000000)),
    SBL(new BigDecimal(5000000)),
    EBL(new BigDecimal(900000)),
    LBL(new BigDecimal(800000)),
    GBL(new BigDecimal(100));

    BigDecimal money;

    PaymentGateway(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }
}
