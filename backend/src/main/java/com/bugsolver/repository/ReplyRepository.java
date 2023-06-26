package com.bugsolver.repository;

import com.bugsolver.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Boolean existsByIdAndUser_Username(Long id, String username);

    @Query(value = "SELECT r FROM Reply r WHERE r.bug.id = :bugId")
    Page<Reply> findAllByBugId(Pageable pageable, @Param("bugId") Long bugId);

    @Transactional
    @Modifying
    @Query(value = "update Reply r set r.bestAnswer = false WHERE r.bug.id = :bugId")
    void removeActualBestAnswer(@Param("bugId") Long bugId);
}
