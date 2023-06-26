package com.bugsolver.repository;

import com.bugsolver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public
interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT Users.* FROM Reply " +
            "JOIN Bugs ON Reply.bug_id = Bugs.id AND Reply.id = :replyId " +
            "JOIN Users ON Bugs.user_id = Users.id", nativeQuery = true)
    Optional<User> findBugAuthorByReplyId(@Param("replyId") Long replyId);
}
