package com.example.demo.login.address.controller;

import com.example.demo.login.address.controller.dto.AddressApiResponse;
import com.example.demo.login.address.controller.dto.AddressKeyword;
import com.example.demo.login.address.util.AddressProviderUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressSearchController {

    private final AddressProviderUtil service;

    public AddressSearchController(AddressProviderUtil service) {
        this.service = service;
    }

    @PostMapping("/search")
    public AddressApiResponse.Juso getAddress(@RequestBody AddressKeyword keyword) {
        return service.search(keyword.getKeyword());
    }
}
