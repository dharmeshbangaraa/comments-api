package com.pertsol.repository;

import com.pertsol.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByDate(LocalDate date);

    Optional<List<Comment>> findByDateAndUsername(LocalDate date, String username);

    Optional<Comment> findByUsernameAndCommentId(String username, Long id);

    Optional<List<Comment>> findByUsername(String username);
}
