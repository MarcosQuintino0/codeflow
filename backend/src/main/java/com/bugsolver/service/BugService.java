package com.bugsolver.service;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.Category;
import com.bugsolver.exception.bug.BugNotFoundException;
import com.bugsolver.exception.category.CategoryNotFoundException;
import com.bugsolver.repository.BugRepository;
import com.bugsolver.entity.BugSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BugService {

    private final BugRepository bugRepository;
    private final CategoryService categoryService;
    private final BugSpecification bugSpecification;

    public Bug save(Bug bug){
        Set<Category> categories = bug.getCategories().stream()
                .map(c -> {
                    Category category;
                    if (c.getId() != null) category = categoryService.findById(c.getId());
                    else throw new CategoryNotFoundException();
                    return category;
                }).collect(Collectors.toSet());

        bug.setCategories(categories);
        return bugRepository.save(bug);

    }

    public Bug update(Long id, Bug bug){
        Bug oldBug = findById(id);

        oldBug.setDescription(bug.getDescription());
        oldBug.setTitle(bug.getTitle());
        oldBug.setCategories(bug.getCategories());

        return bugRepository.save(oldBug);
    }

    public Bug findById(Long id){
        return bugRepository.findById(id).orElseThrow(
                () -> new BugNotFoundException()
        );
    }

    public void deleteById(Long id){
        if(!bugRepository.existsById(id)){
            throw new BugNotFoundException();
        }

        bugRepository.deleteById(id);
    }

    public Boolean existsById(Long id){
        return bugRepository.existsById(id);
    }

    public Page<Bug> findAll(Pageable pageable, BugSearchCriteria searchCriteria){
        return bugRepository.findAll(bugSpecification.getBugSpecification(searchCriteria), pageable);
    }

    public Long countAllBugsWithBestAnswer(){
        return bugRepository.countAllBugsWithBestAnswer();
    }

    public boolean isBugAuthor(Long bugId, String author){
        return bugRepository.existsBugByIdAndUser_Username(bugId, author);
    }

}
