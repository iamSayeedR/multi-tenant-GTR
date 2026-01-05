package com.example.multi_tanent.warehouse.model;

import com.example.multi_tanent.warehouse.entity.LocationEntity;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationNode {

    private Long id;
    private String code;
    private String name;
    private String type;
    private List<LocationNode> children;

    public LocationNode(LocationEntity e) {
        this.id = e.getId();
        this.name = e.getName();
        this.type = e.getLocationType();
        this.code = e.getCode();
    }
}
