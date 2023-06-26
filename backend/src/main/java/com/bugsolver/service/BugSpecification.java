package com.bugsolver.service;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.Category;
import com.bugsolver.entity.BugSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Set;

@Service
public class BugSpecification {

    public Specification<Bug> getBugSpecification(BugSearchCriteria bugSearchCriteria){
        Specification<Bug> specification = Specification.where(null);
        
        if(!bugSearchCriteria.getTitle().isBlank()){
            specification = specification.and(specBugByTitle(bugSearchCriteria.getTitle()));
        }
        if(!bugSearchCriteria.getCategories().isEmpty()){
            specification = specification.and(specBugByCategories(bugSearchCriteria.getCategories()));
        }
        if(bugSearchCriteria.getUserId() != null){
            specification = specification.and(specBugFromUser(bugSearchCriteria.getUserId()));
        }

        return specification;
    }

    private Specification<Bug> specBugByTitle(String title){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%"+title+"%");
    }

    private Specification<Bug> specBugByCategories(Set<String> categories){
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<Bug> bug = subQuery.from(Bug.class);
            Join<Bug, Category> categoriesJoin = bug.join("categories");

            Predicate predicate1 = categoriesJoin.get("name").in(categories);

            subQuery.select(bug.get("id"))
                    .where(predicate1)
                    .groupBy(bug.get("id"))
                    .having(
                            criteriaBuilder.ge(
                                    criteriaBuilder.count(bug.get("id")),
                                    categories.size()
                            )
                    );

            return criteriaBuilder.and(root.get("id").in(subQuery));
        };
    }

    private Specification<Bug> specBugFromUser(Long userId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
