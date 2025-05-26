package com.example.demo.info.domain.repository;

import com.example.demo.info.domain.entity.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {
}
