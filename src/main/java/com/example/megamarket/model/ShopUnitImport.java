package com.example.megamarket.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.UUID;

public class ShopUnitImport {

    private UUID id;

    private String name;

    @Nullable
    private UUID parentId;

    private ShopUnitType type;

    @Nullable
    private Long price;

    @JsonCreator
    public ShopUnitImport(@JsonProperty(required = true) UUID id,
                          @JsonProperty(required = true) String name,
                          @Nullable @JsonProperty UUID parentId,
                          @JsonProperty(required = true) ShopUnitType type,
                          @Nullable @JsonProperty Long price) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public UUID getParentId() {
        return parentId;
    }

    public ShopUnitType getType() {
        return type;
    }

    @Nullable
    public Long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ShopUnitImport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
