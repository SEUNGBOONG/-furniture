package com.example.demo.info.controller.company;

import com.example.demo.info.controller.dto.CompanyHistoryImageDTO;
import com.example.demo.info.controller.dto.CompanyHistoryItemDTO;
import com.example.demo.info.service.CompanyHistoryItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyHistoryItemController {

    private final CompanyHistoryItemService service;

    public CompanyHistoryItemController(final CompanyHistoryItemService service) {
        this.service = service;
    }

    @GetMapping("/history")
    public ResponseEntity<List<CompanyHistoryItemDTO>> getHistoryItems() {
        return ResponseEntity.ok(service.getAllHistoryItems());
    }

    @GetMapping("/historyImage")
    public ResponseEntity<CompanyHistoryImageDTO> getHistoryImage() {
        CompanyHistoryImageDTO imageDTO = service.getHistoryImageById(1L);
        if (imageDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageDTO);
    }

}
