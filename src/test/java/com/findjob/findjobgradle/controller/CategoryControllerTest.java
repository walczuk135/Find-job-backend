package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.service.CategoryService;
import com.findjob.findjobgradle.service.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        //given
        given(categoryService.getAllCategory()).willReturn(Arrays.asList(Category.values()));

        //when
        ResultActions result = mockMvc.perform(get("/api/categories/"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("ADMINISTRATION")))
                .andExpect(jsonPath("$[1]", is("CONSULTING")))
                .andExpect(jsonPath("$[2]", is("RESEARCH_AND_0DEVELOPMENT")))
                .andExpect(jsonPath("$[3]", is("BANKING")))
                .andExpect(jsonPath("$[4]", is("CONSTRUCTION")))
                .andExpect(jsonPath("$[5]", is("CALL_CENTER")))
                .andExpect(jsonPath("$[6]", is("EDUCATION")))
                .andExpect(jsonPath("$[7]", is("FINANCE")))
                .andExpect(jsonPath("$[8]", is("FRANCYZA")))
                .andExpect(jsonPath("$[9]", is("HOSPITALITY")))
                .andExpect(jsonPath("$[10]", is("INTERNET_ECOMMERCE")))
                .andExpect(jsonPath("$[11]", is("ENGINEERING")))
                .andExpect(jsonPath("$[12]", is("IT")))
                .andExpect(jsonPath("$[13]", is("MEDIA")))
                .andExpect(jsonPath("$[14]", is("CUSTOMER_SERVICE")))
                .andExpect(jsonPath("$[15]", is("PHYSICAL_WORK")))
                .andExpect(jsonPath("$[16]", is("LAW")))
                .andExpect(jsonPath("$[17]", is("PRODUCTION")))
                .andExpect(jsonPath("$[18]", is("ADVERTISING")))
                .andExpect(jsonPath("$[19]", is("PUBLIC_SECTOR")))
                .andExpect(jsonPath("$[20]", is("SALES")))
                .andExpect(jsonPath("$[21]", is("TRANSPORT")))
                .andExpect(jsonPath("$[22]", is("INSURANCE")))
                .andExpect(jsonPath("$[23]", is("SHOPPING")))
                .andExpect(jsonPath("$[24]", is("QUALITY_CONTROL")))
                .andExpect(jsonPath("$[25]", is("ENERGY")))
                .andExpect(jsonPath("$[26]", is("OTHER")));
    }
}