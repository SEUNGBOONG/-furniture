package com.example.demo.login.address.controller;

import com.example.demo.login.address.controller.dto.AddressApiResponse;
import com.example.demo.login.address.controller.dto.AddressKeyword;
import com.example.demo.login.address.service.AddressSearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressSearchController {

    private final AddressSearchService service;

    public AddressSearchController(AddressSearchService service) {
        this.service = service;
    }

    @PostMapping("/search")
    public AddressApiResponse.Juso getAddress(@RequestBody AddressKeyword keyword) {
        return service.search(keyword.getKeyword());
    }
}


