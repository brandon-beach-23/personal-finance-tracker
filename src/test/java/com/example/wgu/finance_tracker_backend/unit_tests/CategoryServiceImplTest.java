package com.example.wgu.finance_tracker_backend.unit_tests;

import com.example.wgu.finance_tracker_backend.DTOs.CategoryRequest;
import com.example.wgu.finance_tracker_backend.DTOs.CategoryResponse;
import com.example.wgu.finance_tracker_backend.models.Category;
import com.example.wgu.finance_tracker_backend.repositories.CategoryRepository;
import com.example.wgu.finance_tracker_backend.services.implementations.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    //Mock Repositories
    @Mock
    CategoryRepository categoryRepository;

    //Tested Service
    @InjectMocks
    CategoryServiceImpl categoryService;

    //Test Data
    private final static Long CATEGORY_ID = 1L;
    private final static String CATEGORY_NAME = "Test Category";

    CategoryRequest request;
    CategoryResponse response;

    @BeforeEach
    void setUp() {
        //New Request
        request = new CategoryRequest(CATEGORY_ID, CATEGORY_NAME);
        //Reset Repo
        Mockito.reset(categoryRepository);
    }

    @Test
    void createCategory_Success() {
        //Arrange
        //Verifies the object the service is trying to save
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenAnswer(invocation -> {
            Category saved = invocation.getArgument(0);
            saved.setId(CATEGORY_ID);
            return saved;
        });

        //Action
        response = categoryService.createCategory(request);

        //Assert
        assertNotNull(response);
        assertEquals(CATEGORY_ID, response.getId());
        assertEquals(CATEGORY_NAME, response.getCategoryName());
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());

    }

    @Test
    void createCategory_AlreadyExists() {
        //Arrange
        Category existingCategory = new Category();
        existingCategory.setId(99L);
        existingCategory.setName(CATEGORY_NAME);

        Mockito.when(categoryRepository.findByName(CATEGORY_NAME)).thenReturn(Optional.of(existingCategory));

        //Action and Assert
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(request));
        Mockito.verify(categoryRepository, Mockito.times(0)).save(existingCategory);
    }

    @Test
    void getCategoryByName_Found(){
        //Arrange
        Category exisitingCategory = new Category();
        exisitingCategory.setId(CATEGORY_ID);
        exisitingCategory.setName(CATEGORY_NAME);

        Mockito.when(categoryRepository.findByName(CATEGORY_NAME)).thenReturn(Optional.of(exisitingCategory));

        //Action
        Optional<CategoryResponse> optionalCategoryResponse = categoryService.getCategoryByName(CATEGORY_NAME);

        //Assert
        assertTrue(optionalCategoryResponse.isPresent());
        assertEquals(CATEGORY_ID, optionalCategoryResponse.get().getId());
        assertEquals(CATEGORY_NAME, optionalCategoryResponse.get().getCategoryName());

        Mockito.verify(categoryRepository, Mockito.times(1)).findByName(CATEGORY_NAME);
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    void getCategoryByName_NotFound(){
        //Arrange
        Mockito.when(categoryRepository.findByName(CATEGORY_NAME)).thenReturn(Optional.empty());

        //Action
        Optional<CategoryResponse> optionalCategoryResponse = categoryService.getCategoryByName(CATEGORY_NAME);

        //Assert
        assertFalse(optionalCategoryResponse.isPresent());

        Mockito.verify(categoryRepository, Mockito.times(1)).findByName(CATEGORY_NAME);
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    void getAllCategories_WithResults(){
        //Arrange
        Category category1 = new Category();
        category1.setId(CATEGORY_ID);
        category1.setName(CATEGORY_NAME);
        Category category2 = new Category();
        category2.setId(CATEGORY_ID);
        category2.setName(CATEGORY_NAME);

        List<Category> categories = Arrays.asList(category1, category2);

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        //Action
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();

        //Assert
        assertNotNull(categoryResponses);
        assertEquals(2, categoryResponses.size());

        CategoryResponse categoryResponse1 = categoryResponses.get(0);
        CategoryResponse categoryResponse2 = categoryResponses.get(1);

        assertEquals(CATEGORY_ID, categoryResponse1.getId());
        assertEquals(CATEGORY_NAME, categoryResponse1.getCategoryName());
        assertEquals(CATEGORY_ID, categoryResponse2.getId());
        assertEquals(CATEGORY_NAME, categoryResponse2.getCategoryName());

        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAllCategories_NotFound(){
        //Arrange
        Mockito.when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        //Action
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();

        //Assert
        assertNotNull(categoryResponses);
        assertTrue(categoryResponses.isEmpty());

        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());

    }

}
