package com.example.dip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card_item", schema = "shop_schema", catalog = "shop")
public class CardItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "item_id", nullable = false, insertable=false, updatable=false)
    private Long item_id;

    @Column(name = "card_id", nullable = false, insertable=false, updatable=false)
    private Long card_id;

    private int count;


    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    private Item item;
//
    @ManyToOne
    @JoinColumn(name = "card_id",referencedColumnName = "id")
    @JsonBackReference
    private Card card;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardItem cardItem = (CardItem) o;
        return id.equals(cardItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
