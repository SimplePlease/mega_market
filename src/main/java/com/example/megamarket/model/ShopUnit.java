package com.example.megamarket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "public", name = "shop_unit")
public class ShopUnit {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Nullable
    @JoinColumn(name = "parent_id")
    @ManyToOne
    private ShopUnit parent;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    @Nullable
    private Long price;

    @Transient
    private List<ShopUnit> children;

    public ShopUnit() {
    }

    public ShopUnit(UUID id, String name, LocalDateTime date, @Nullable ShopUnit parent, ShopUnitType type, @Nullable Long price) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.parent = parent;
        this.type = type;
        this.price = price;
        if (type == ShopUnitType.CATEGORY) {
            children = new ArrayList<>();
        }
    }

    @PostLoad
    public void postLoad() {
        if (type == ShopUnitType.CATEGORY) {
            children = new ArrayList<>();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Nullable
    @JsonIgnore
    public ShopUnit getParent() {
        return parent;
    }

    @Nullable
    @JsonProperty("parentId")
    public UUID getParentId() {
        return parent != null ? parent.id : null;
    }

    public void setParent(@Nullable ShopUnit parent) {
        this.parent = parent;
    }

    public ShopUnitType getType() {
        return type;
    }

    public void setType(ShopUnitType type) {
        this.type = type;
    }

    @Nullable
    public Long getPrice() {
        return price;
    }

    public void setPrice(@Nullable Long price) {
        this.price = price;
    }

    public void addToChildren(ShopUnit node) {
        children.add(node);
    }

    public List<ShopUnit> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        UUID parentId = parent != null ? parent.id : null;
        return "ShopUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", parent=" + parentId +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
