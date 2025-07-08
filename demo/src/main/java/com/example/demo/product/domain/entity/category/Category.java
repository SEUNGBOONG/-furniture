package com.example.demo.product.domain.entity.category;

import com.example.demo.common.exception.Setting;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

    public Category(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(Setting.CATEGORY_NAME_NOT_EMPTY.toString());
        }
        this.name = name;
    }

    public void updateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(Setting.CATEGORY_NAME_NOT_EMPTY.toString());
        }
        this.name = name;
    }

}
