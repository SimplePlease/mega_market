package com.example.megamarket.model;

import com.sun.istack.NotNull;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

public class ShopUnitStatisticUnit {

    private UUID id;

    private String name;

    @Nullable
    private UUID parentId;

    private ShopUnitType type;

    @Nullable
    private Long price;

    private LocalDateTime date;


}
