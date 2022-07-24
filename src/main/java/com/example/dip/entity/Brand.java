package com.example.dip.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "brands", schema = "shop_schema", catalog = "shop")
public class Brand {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonSerialize
    private List<Item> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand)) return false;
        Brand brands = (Brand) o;
        return id.equals(brands.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
