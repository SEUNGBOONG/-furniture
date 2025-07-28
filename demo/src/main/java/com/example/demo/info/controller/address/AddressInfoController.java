package com.example.demo.info.controller.address;

import com.example.demo.info.domain.entity.address.AddressInfo;
import com.example.demo.info.domain.repository.address.AddressInfoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
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
