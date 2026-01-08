package com.spavnit.cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель товара в корзине
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 1; // Всегда 1 экземпляр цифрового товара

    // Запрещаем изменение quantity
    public void setQuantity(Integer quantity) {
        // Ничего не делаем - quantity всегда = 1
    }

    @CreationTimestamp
    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;

    /**
     * Получить общую стоимость позиции (цена * количество)
     */
    public BigDecimal getTotalPrice() {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }
}