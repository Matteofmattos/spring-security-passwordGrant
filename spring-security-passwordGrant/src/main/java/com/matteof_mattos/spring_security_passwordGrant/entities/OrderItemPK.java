package com.matteof_mattos.spring_security_passwordGrant.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode(of = "product")
public class OrderItemPK {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItemPK() {
    }

    public OrderItemPK(Product product, Order order) {
        this.product = product;
        this.order = order;
    }
}
