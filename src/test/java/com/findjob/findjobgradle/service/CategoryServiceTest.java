package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.domain.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CategoryServiceTest {

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories(){
        //given

        //when
        when(categoryService.getAllCategory()).thenReturn(Arrays.asList(Category.values()));
        List<Category> allCategory = categoryService.getAllCategory();
        //then
        assertThat(Category.values().length).isEqualTo(allCategory.size());
    }
}