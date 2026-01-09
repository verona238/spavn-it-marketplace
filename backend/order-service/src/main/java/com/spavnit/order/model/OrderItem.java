package com.spavnit.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

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
    private Integer quantity = 1;

    @Column(name = "download_link", length = 500)
    private String downloadLink;

    @CreationTimestamp
    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;
}