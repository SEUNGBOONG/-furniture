package com.example.demo.info.controller;

import com.example.demo.info.domain.AddressInfo;
import com.example.demo.info.domain.AddressInfoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
public class AddressInfoController {

    private final AddressInfoRepository repository;

    public AddressInfoController(AddressInfoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressInfo> getAddress(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
