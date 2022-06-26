package com.example.megamarket.api;

import com.example.megamarket.model.Error;
import com.example.megamarket.model.ShopUnit;
import com.example.megamarket.model.ShopUnitImportRequest;
import com.example.megamarket.service.ShopUnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
public class MainController {

    private final ShopUnitService shopUnitService;

    public MainController(ShopUnitService shopUnitService) {
        this.shopUnitService = shopUnitService;
    }

    @GetMapping("/home")
    public String home() {
        return "THIS IS MEGA MARKET!";
    }

    @PostMapping("/imports")
    public ResponseEntity<?> imports(@RequestBody ShopUnitImportRequest request) {
        try {
            shopUnitService.imports(request.getItems(), request.getUpdateDate());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Error(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            shopUnitService.delete(UUID.fromString(id));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(new Error(404, ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Error(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<?> nodes(@PathVariable("id") String id) {
        try {
            ShopUnit shopUnit = shopUnitService.nodes(UUID.fromString(id));
            return new ResponseEntity<>(shopUnit, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(new Error(404, ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Error(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
