package com.bugsolver.service;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.BugSearchCriteria;
import com.bugsolver.entity.Category;
import com.bugsolver.entity.User;
import com.bugsolver.exception.bug.BugNotFoundException;
import com.bugsolver.exception.category.CategoryNotFoundException;
import com.bugsolver.repository.BugRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BugServiceTests {

    @Autowired
    private BugService bugService;

    @MockBean
    private BugRepository bugRepository;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private BugSpecification bugSpecification;

    @DisplayName("WHEN save an bug with valid categories SHOULD save and return new bug")
    @Test
    public void saveNewBugWithValidCategories(){
        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(2L);

        Bug newBug = new Bug();
        newBug.setCategories(Set.of(category1, category2));
        newBug.setTitle("New bug");
        newBug.setDescription("New description");

        when(categoryService.findById(1L)).thenReturn(category1);
        when(categoryService.findById(2L)).thenReturn(category2);
        when(bugRepository.save(any(Bug.class))).thenReturn(newBug);

        assertThat(bugService.save(newBug)).isEqualTo(newBug);
    }

    @DisplayName("WHEN save an bug with any invalid category SHOULD throw CategoryNotFoundException")
    @Test
    public void saveNewBugWithAnyInvalidCategory(){
        Category category1 = new Category();
        category1.setId(99L);

        Bug newBug = new Bug();
        newBug.setCategories(Set.of(category1));
        newBug.setTitle("New bug");
        newBug.setDescription("New description");

        when(categoryService.findById(anyLong())).thenThrow(new CategoryNotFoundException());

        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> bugService.save(newBug)
        );
    }

    @DisplayName("WHEN save an bug with any category with any id SHOULD throw CategoryNotFoundException")
    @Test
    public void saveNewBugWithAnyCategoryWithAnyId(){
        Category category1 = new Category();

        Bug newBug = new Bug();
        newBug.setCategories(Set.of(category1));
        newBug.setTitle("New bug");
        newBug.setDescription("New description");

        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> bugService.save(newBug)
        );
    }

    @DisplayName("WHEN update an bug with valid id SHOULD update and return bug")
    @Test
    public void updateBugWithValidId(){
        Bug oldBug = new Bug();
        oldBug.setId(1L);

        Bug newBug = new Bug();
        newBug.setTitle("New bug");
        newBug.setDescription("New description");

        when(bugRepository.findById(anyLong())).thenReturn(Optional.of(oldBug));
        when(bugRepository.save(any(Bug.class))).thenReturn(oldBug);

        assertThat(bugService.update(oldBug.getId(),newBug)).isEqualTo(oldBug);
    }

    @DisplayName("WHEN update an bug with invalid id SHOULD throw BugNotFoundException")
    @Test
    public void updateBugWithInvalidId(){

        Bug newBug = new Bug();
        newBug.setTitle("New bug");
        newBug.setDescription("New description");

        when(bugRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(BugNotFoundException.class,
                () -> bugService.update(anyLong(),newBug)
        );
    }

    @DisplayName("WHEN find bug by id with valid id SHOULD return bug")
    @Test
    public void findBugWithValidId(){
        Bug bug = new Bug();
        bug.setId(1L);

        when(bugRepository.findById(anyLong())).thenReturn(Optional.of(bug));

        assertThat(bugService.findById(bug.getId())).isEqualTo(bug);
    }

    @DisplayName("WHEN find bug by id with invalid id SHOULD throw BugNotFoundException")
    @Test
    public void findBugWithInvalidId(){

        when(bugRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(BugNotFoundException.class,
                () -> bugService.findById(anyLong())
        );
    }

    @DisplayName("WHEN delete bug by id with valid id SHOULD delete bug")
    @Test
    public void deleteBugWithValidId(){
        Bug bug = new Bug();
        bug.setId(1L);

        when(bugRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(bugRepository).deleteById(anyLong());

        bugService.deleteById(bug.getId());
        verify(bugRepository, times(1)).deleteById(bug.getId());
    }

    @DisplayName("WHEN delete bug by id with invalid id SHOULD throw BugNotFoundException")
    @Test
    public void deleteBugWithInvalidId(){

        when(bugRepository.existsById(anyLong())).thenReturn(false);
        doNothing().when(bugRepository).deleteById(anyLong());

        Assertions.assertThrows(BugNotFoundException.class,
                () -> bugService.deleteById(anyLong()));
    }

    @DisplayName("WHEN check if bug exists by id with existent id SHOULD return true")
    @Test
    public void checkIfBugExistsWithExistentId(){
        Bug bug = new Bug();
        bug.setId(1L);

        when(bugRepository.existsById(anyLong())).thenReturn(true);

        assertThat(bugService.existsById(bug.getId())).isTrue();
    }

    @DisplayName("WHEN check if bug exists by id with non existent id SHOULD return false")
    @Test
    public void checkIfBugExistsWithNonExistentId(){

        when(bugRepository.existsById(anyLong())).thenReturn(false);

        assertThat(bugService.existsById(anyLong())).isFalse();
    }

    @DisplayName("WHEN find all bugs paginated with search criteria SHOULD return all bugs ")
    @Test
    public void getAllBugsPaginatedWithCriteria(){
        Bug bug1 = new Bug();
        bug1.setId(1L);

        Bug bug2 = new Bug();
        bug2.setId(2L);

        PageRequest pageable = PageRequest.of(1, 10);
        Page<Bug> result = new PageImpl<>(List.of(bug1,bug2), pageable, 2);

        when(bugSpecification.getBugSpecification(any(BugSearchCriteria.class))).thenReturn(Specification.where(null));
        when(bugRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(result);
    }

    @DisplayName("WHEN check if is bug author by id and author name with valid data SHOULD return true")
    @Test
    public void checkIfIsBugAuthorWithValidData(){
        User user = new User();
        user.setId(1L);
        user.setUsername("Michael");

        Bug bug = new Bug();
        bug.setId(1L);
        bug.setUser(user);

        when(bugRepository.existsBugByIdAndUser_Username(anyLong(),anyString())).thenReturn(true);

        assertThat(bugService.isBugAuthor(bug.getId(), bug.getUser().getUsername())).isTrue();
    }

    @DisplayName("WHEN check if is bug author with different username from author SHOULD return false")
    @Test
    public void checkIfIsBugAuthorWithDifferentUsernameFromAuthor(){
        User user = new User();
        user.setId(1L);
        user.setUsername("Michael");

        User nonAuthor = new User();
        nonAuthor.setUsername("João");

        Bug bug = new Bug();
        bug.setId(1L);
        bug.setUser(user);

        when(bugRepository.existsBugByIdAndUser_Username(anyLong(),anyString())).thenReturn(false);

        assertThat(bugService.isBugAuthor(bug.getId(), nonAuthor.getUsername())).isFalse();
    }
}
