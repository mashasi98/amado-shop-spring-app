package com.example.dip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "item", schema = "shop_schema", catalog = "shop")
public class Item  implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "category_id", insertable=false, updatable=false)
    private Long category_id;

    @Column(name = "brand_id", insertable=false, updatable=false)
    private Long brand_id;

    private String title;
    private int price;
    private String description;
    private String image;
    private String other_image;

//    private boolean stock;
//    private Long count;
//    @Type(type = "org.hibernate.type.NumericBooleanType")
//    private boolean isSale;
//    @Type(type = "org.hibernate.type.NumericBooleanType")
//    private boolean isNew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" ,referencedColumnName = "id")
    @JsonBackReference
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id" ,referencedColumnName = "id")
    @JsonBackReference
    private Brand brand;

    @ManyToMany(mappedBy = "items",fetch = FetchType.LAZY)
    private Set<User> users;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
