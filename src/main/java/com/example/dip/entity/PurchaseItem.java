package com.example.dip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "purchased_item", schema = "shop_schema", catalog = "shop")
public class PurchaseItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "item_id", nullable = false, insertable=false, updatable=false)
    private Long item_id;
    @Column(name = "purchase_id", nullable = false, insertable=false, updatable=false)
    private Long purchase_id;
    private double price;
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id",referencedColumnName = "id")
    @JsonBackReference
    private Purchase purchase;
//
    @ManyToOne//no lazy
    @JsonBackReference
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseItem that = (PurchaseItem) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
