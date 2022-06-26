package com.example.megamarket.model;

import com.sun.istack.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ShopUnitImportRequest {

    private List<ShopUnitImport> items;

    private LocalDateTime updateDate;

    public List<ShopUnitImport> getItems() {
        return items;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
