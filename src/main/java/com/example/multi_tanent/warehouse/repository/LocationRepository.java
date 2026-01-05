package com.example.multi_tanent.warehouse.repository;

import com.example.multi_tanent.warehouse.entity.LocationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("warehouseLocationRepository")
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    List<LocationEntity> findByParentLocationId(Long parentId);

    List<LocationEntity> findByWarehouseId(Long warehouseId);

    List<LocationEntity> findByLocationType(String type);
}
