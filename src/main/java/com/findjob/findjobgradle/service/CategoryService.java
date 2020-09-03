package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.domain.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Getter
@Setter
public class CategoryService {

    public CategoryService() {

    }

    public List<Category> getAllCategory() {
        return Arrays.asList(Category.values());
    }
}
