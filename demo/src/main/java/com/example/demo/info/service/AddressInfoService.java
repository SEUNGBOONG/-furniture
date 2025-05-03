package com.example.demo.info.service;

import com.example.demo.info.domain.AddressInfo;
import com.example.demo.info.domain.AddressInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressInfoService {

    private final AddressInfoRepository repository;

    public AddressInfoService(AddressInfoRepository repository) {
        this.repository = repository;
    }

    public AddressInfo save(AddressInfo info) {
        return repository.save(info);
    }

    public Optional<AddressInfo> getById(Long id) {
        return repository.findById(id);
    }
}
