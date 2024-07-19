package com.pertsol.controller;

import com.pertsol.dto.CommentRequestDto;
import com.pertsol.dto.CommentResponseDto;
import com.pertsol.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v2/comments")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    /**
     * Endpoint to post a new comment.
     *
     * @param commentRequestDto the DTO object containing comment details.
     * @return ResponseEntity containing the saved comment's response DTO and HTTP status.
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> postComment(@RequestBody @Valid CommentRequestDto commentRequestDto) {
        CommentResponseDto response = this.commentService.saveComment(commentRequestDto);
        if (Objects.isNull(response))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to get all comments.
     *
     * @return ResponseEntity containing the list of all comment response DTOs and HTTP status.
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments() {
        List<CommentResponseDto> commentList = this.commentService.getAllComments();
        if (Objects.isNull(commentList))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(commentList);
    }

    /**
     * Endpoint to search for comments by username or date.
     *
     * @param username the username to search comments by (optional).
     * @param date the date to search comments by (optional).
     * @return ResponseEntity containing the list of found comment response DTOs and HTTP status.
     */
    @GetMapping("/search")
    public ResponseEntity<List<CommentResponseDto>> getCommentByUsernameOrDate(
            @Nullable @RequestParam String username,
            @Nullable @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (Objects.isNull(date) && Objects.nonNull(username)) {
            List<CommentResponseDto> commentList = this.commentService.getAllCommentsByUsername(username);
            if (Objects.isNull(commentList))
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(commentList);
        } else if (Objects.isNull(username) && Objects.nonNull(date)) {
            List<CommentResponseDto> commentList = this.commentService.getAllCommentsByDate(date);
            if (Objects.isNull(commentList))
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(commentList);
        } else if (Objects.nonNull(username)) {
            List<CommentResponseDto> commentList = this.commentService.getAllCommentsByUsernameAndDate(username, date);
            if (Objects.isNull(commentList))
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(commentList);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Endpoint to delete comments by username.
     *
     * @param username the username whose comments are to be deleted.
     * @return ResponseEntity containing the list of deleted comment response DTOs and HTTP status.
     */
    @DeleteMapping
    public ResponseEntity<List<CommentResponseDto>> deleteCommentsByUsername(@RequestParam @Validated String username) {
        List<CommentResponseDto> deletedCommentsByUsername = this.commentService.deleteAllCommentsByUsername(username);
        return ResponseEntity.ok(deletedCommentsByUsername);
    }

    /**
     * Endpoint to update a comment by username and comment ID.
     *
     * @param username the username of the comment to be updated.
     * @param id the ID of the comment to be updated.
     * @param commentRequestDto the DTO object containing updated comment details.
     * @return ResponseEntity containing the updated comment response DTO and HTTP status.
     */
    @PutMapping
    public ResponseEntity<CommentResponseDto> updateComment(@RequestParam @Validated String username,
                                                            @RequestParam @Validated Long id,
                                                            @RequestBody @Validated CommentRequestDto commentRequestDto) {
        CommentResponseDto response = this.commentService.updateCommentByUsername(username, id, commentRequestDto);
        return ResponseEntity.ok(response);
    }
}
