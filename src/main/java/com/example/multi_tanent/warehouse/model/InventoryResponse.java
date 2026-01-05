package com.example.multi_tanent.warehouse.model;

public class InventoryResponse {
    private Long id;
    private Long locationId;
    private String locationName;
    private Long itemId;
    private String itemName;
    private Long quantity;

    public InventoryResponse() {
    }

    public InventoryResponse(Long id, Long locationId, String locationName, Long itemId, String itemName,
            Long quantity) {
        this.id = id;
        this.locationId = locationId;
        this.locationName = locationName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
