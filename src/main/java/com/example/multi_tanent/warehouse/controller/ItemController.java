package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.Item;
import com.example.multi_tanent.warehouse.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whItemController")
@RequestMapping("/api/warehouse-items")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Item Master", description = "Manage item master data")
public class ItemController {

    public final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @Operation(summary = "Create item", description = "Add a new item to the master data")
    public ResponseEntity<String> uploadItem(@RequestBody Item item) {
        itemService.uploadItem(item);
        return ResponseEntity.ok("Item uploaded");
    }

    @GetMapping
    @Operation(summary = "List all items", description = "Get all items in the system")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.findAll();
        return ResponseEntity.ok(items);
    }
}
