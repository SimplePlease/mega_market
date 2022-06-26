package com.example.megamarket.service;

import com.example.megamarket.model.ShopUnit;
import com.example.megamarket.model.ShopUnitImport;
import com.example.megamarket.model.ShopUnitImportRequest;
import com.example.megamarket.model.ShopUnitType;
import com.example.megamarket.repo.ShopUnitRepo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShopUnitService {

    private final ShopUnitRepo shopUnitRepo;
    private final Clock clock;

    public ShopUnitService(ShopUnitRepo shopUnitRepo, Clock clock) {
        this.shopUnitRepo = shopUnitRepo;
        this.clock = clock;
    }

    public void imports(List<ShopUnitImport> imports, LocalDateTime date) {
        Set<UUID> importIds = new HashSet<>();
        for (var i : imports) {
            if (importIds.contains(i.getId())) {
                throw new RuntimeException("Одинаковые id в запросе");
            }
            if (i.getName() == null) {
                throw new RuntimeException("Название элемента null");
            }
            if (i.getType() == ShopUnitType.CATEGORY && i.getPrice() != null) {
                throw new RuntimeException("Цена категории не null");
            }
            if (i.getType() == ShopUnitType.OFFER && (i.getPrice() == null || i.getPrice() < 0)) {
                throw new RuntimeException("Цена товара null или меньше 0");
            }
            importIds.add(i.getId());
        }

        Map<UUID, ShopUnit> toUpdate = shopUnitRepo.loadMapByIds(importIds);
        Map<UUID, ShopUnit> toSave = imports.stream()
                .filter(i -> !toUpdate.containsKey(i.getId()))
                .map(i -> new ShopUnit(i.getId(), i.getName(), date, null, i.getType(), i.getPrice()))
                .collect(Collectors.toMap(ShopUnit::getId, u -> u));

        Set<UUID> parentIds = imports.stream()
                .map(ShopUnitImport::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<UUID, ShopUnit> savedParents = shopUnitRepo.loadMapByIds(parentIds);

        for (var i : imports) {
            ShopUnit parent = null;
            if (i.getParentId() != null) {
                parent = Optional.ofNullable(savedParents.get(i.getParentId()))
                        .or(() -> Optional.ofNullable(toSave.get(i.getParentId())))
                        .orElseThrow(() -> new RuntimeException("Не найден родитель элемента"));
                if (parent.getType() == ShopUnitType.OFFER) {
                    throw new RuntimeException("Родитель элемента - товар");
                }
            }
            if (toUpdate.containsKey(i.getId())) {
                ShopUnit u = toUpdate.get(i.getId());
                if (u.getType() != i.getType()) {
                    throw new RuntimeException("Изменение типа элемента");
                }
                u.setName(i.getName());
                u.setDate(date);
                u.setParent(parent);
                if (parent != null) {
                    parent.addToChildren(u);
                }
                u.setPrice(i.getPrice());
            } else {
                ShopUnit u = toSave.get(i.getId());
                u.setParent(parent);
                if (parent != null) {
                    parent.addToChildren(u);
                }
            }
        }

        if (!toUpdate.isEmpty()) {
            shopUnitRepo.saveAll(toUpdate.values());
        }
        if (!toSave.isEmpty()) {
            // toSave.values() могут быть в неправильном порядке
            List<ShopUnit> toSaveOrdered = new ArrayList<>();
            for (var node : toSave.values()) {
                if (node.getParent() == null || savedParents.containsKey(node.getParentId())) {
                    buildOrderedList(node, toSaveOrdered);
                }
            }
            shopUnitRepo.saveAll(toSaveOrdered);
        }
    }

    private void buildOrderedList(ShopUnit node, List<ShopUnit> list) {
        list.add(node);
        if (node.getType() == ShopUnitType.OFFER) {
            return;
        }
        for (var child : node.getChildren()) {
            buildOrderedList(child, list);
        }
    }

    public void delete(UUID id) {
        if (!shopUnitRepo.existsById(id)) {
            throw new EntityNotFoundException("Категория/товар не найден");
        }

        shopUnitRepo.deleteSubtree(id);
    }

    public ShopUnit nodes(UUID id) {
        if (!shopUnitRepo.existsById(id)) {
            throw new EntityNotFoundException("Категория/товар не найден");
        }

        List<ShopUnit> subtree = shopUnitRepo.loadSubtree(id);
        ShopUnit root = null;
        for (var node : subtree) {
            if (node.getId().equals(id)) {
                root = node;
            } else {
                ShopUnit parent = node.getParent();
                assert parent != null;
                parent.addToChildren(node);
            }
        }
        assert root != null;
        calcPriceInSubtree(root);
        return root;
    }

    private TotalPriceWithCount calcPriceInSubtree(ShopUnit node) {
        if (node.getType() == ShopUnitType.OFFER) {
            assert node.getPrice() != null;
            return new TotalPriceWithCount(node.getPrice(), 1);
        }
        TotalPriceWithCount total = new TotalPriceWithCount(0, 0);
        for (var child : node.getChildren()) {
            TotalPriceWithCount f = calcPriceInSubtree(child);
            total.totalPrice += f.totalPrice;
            total.count += f.count;
        }
        if (total.count > 0) {
            long price = total.totalPrice / total.count;
            node.setPrice(price);
        } else {
            node.setPrice(null);
        }
        return total;
    }

    private static class TotalPriceWithCount {
        long totalPrice;
        int count;

        public TotalPriceWithCount(long totalPrice, int count) {
            this.totalPrice = totalPrice;
            this.count = count;
        }
    }

}
