package com.example.demo.info.domain.repository.address;

import com.example.demo.info.domain.entity.address.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {
}
