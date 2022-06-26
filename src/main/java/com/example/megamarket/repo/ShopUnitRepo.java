package com.example.megamarket.repo;

import com.example.megamarket.model.ShopUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import java.util.stream.Collectors;

public interface ShopUnitRepo extends JpaRepository<ShopUnit, UUID> {

    List<ShopUnit> findAllByIdIn(Collection<UUID> ids);

    default Map<UUID, ShopUnit> loadMapByIds(Collection<UUID> ids) {
        return findAllByIdIn(ids).stream()
                .collect(Collectors.toMap(ShopUnit::getId, u -> u));
    }

    @Query(value = """
    WITH RECURSIVE r (id) AS (
        SELECT id
        FROM shop_unit
        WHERE id = :id
        UNION ALL
        SELECT u.id
        FROM shop_unit u
            JOIN r ON u.parent_id = r.id
    )
    DELETE FROM shop_unit WHERE id in (SELECT * FROM r)
    """, nativeQuery = true)
    void deleteSubtree(@Param("id") UUID id);

    @Query(value = """
    WITH RECURSIVE r (id, name, date, parent_id, type, price) AS (
        SELECT id, name, date, parent_id, type, price
        FROM shop_unit
        WHERE id = :id
        UNION ALL
        SELECT u.id, u.name, u.date, u.parent_id, u.type, u.price
        FROM shop_unit u
            JOIN r ON u.parent_id = r.id
    )
    SELECT * FROM r
    """, nativeQuery = true)
    List<ShopUnit> loadSubtree(@Param("id") UUID id);
}
