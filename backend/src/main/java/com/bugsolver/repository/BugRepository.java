package com.bugsolver.repository;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BugRepository extends JpaRepository<Bug, Long>, JpaSpecificationExecutor<Bug> {

    @Query(value = "SELECT count(b.id) FROM Bugs as b " +
            "JOIN Reply as r ON r.bug_id = b.id AND r.best_answer = true", nativeQuery = true)
    Long countAllBugsWithBestAnswer();

    Boolean existsBugByIdAndUser_Username(Long id, String username);
}
