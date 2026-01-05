package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.model.Item;
import com.example.multi_tanent.warehouse.repository.InventoryRepository;
import com.example.multi_tanent.warehouse.repository.ItemRepository;
import com.example.multi_tanent.warehouse.repository.LocationRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    public final ItemRepository itemRepository;

    public final LocationRepository locationRepository;

    public final InventoryRepository inventoryRepository;

    private final com.example.multi_tanent.warehouse.mapper.ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, LocationRepository locationRepository,
            InventoryRepository inventoryRepository, com.example.multi_tanent.warehouse.mapper.ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemMapper = itemMapper;
    }

    public void uploadItem(Item item) {

        ItemEntity itemEntity = itemMapper.toEntity(item);
        ItemEntity savedItem = itemRepository.save(itemEntity);

        // Create inventory rows for all existing locations
        List<LocationEntity> locations = locationRepository.findAll();
        for (LocationEntity location : locations) {
            InventoryEntity inv = new InventoryEntity();
            inv.setLocation(location);
            inv.setItem(savedItem);
            inv.setQuantity(0L);
            inventoryRepository.save(inv);
        }
    }

    @Transactional(readOnly = true)
    public List<Item> findAll() {
        List<ItemEntity> entities = itemRepository.findAll();
        return entities.stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}
