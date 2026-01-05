package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.entity.RFIDTagEntity;
import com.example.multi_tanent.warehouse.model.RFIDTagRequest;
import com.example.multi_tanent.warehouse.model.RFIDTagResponse;
import com.example.multi_tanent.warehouse.repository.ItemRepository;
import com.example.multi_tanent.warehouse.repository.RFIDTagRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RFIDTagService {

    private final RFIDTagRepository rfidRepo;
    private final ItemRepository itemRepo;

    public RFIDTagService(RFIDTagRepository rfidRepo, ItemRepository itemRepo) {
        this.rfidRepo = rfidRepo;
        this.itemRepo = itemRepo;
    }

    public RFIDTagResponse register(RFIDTagRequest req) {

        ItemEntity item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        RFIDTagEntity tag = new RFIDTagEntity();
        tag.setTagCode(req.getTagCode());
        tag.setItem(item);
        tag.setActive(true);

        RFIDTagEntity saved = rfidRepo.save(tag);
        return toResponse(saved);
    }

    public List<RFIDTagResponse> getAll() {
        return rfidRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RFIDTagResponse toResponse(RFIDTagEntity e) {
        RFIDTagResponse r = new RFIDTagResponse();
        r.setId(e.getId());
        r.setTagCode(e.getTagCode());
        r.setActive(e.getActive());
        r.setItemId(e.getItem().getId());
        r.setItemCode(e.getItem().getCode());
        r.setItemName(e.getItem().getName());
        return r;
    }
}
