package com.pertsol.service.serviceImpl;

import com.pertsol.dto.CommentRequestDto;
import com.pertsol.dto.CommentResponseDto;
import com.pertsol.entity.Comment;
import com.pertsol.exception.CommentException;
import com.pertsol.repository.CommentRepository;
import com.pertsol.service.CommentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto saveComment(CommentRequestDto commentRequestDto) {
        logger.info("Saving a new comment for username: {}", commentRequestDto.username());

        Comment comment = Comment.builder()
                .body(commentRequestDto.body())
                .username(commentRequestDto.username())
                .date(LocalDate.now())
                .build();

        comment = commentRepository.save(comment);

        logger.info("Saved comment with ID: {}", comment.getCommentId());

        return mapToResponseDto(comment);
    }

    @Override
    public List<CommentResponseDto> getAllComments() {
        logger.info("Fetching all comments");

        List<Comment> commentList = commentRepository.findAll();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(mapToResponseDto(comment));
        }

        logger.info("Fetched {} comments", commentResponseDtoList.size());

        return commentResponseDtoList;
    }

    @Override
    public List<CommentResponseDto> getAllCommentsByUsername(String username) {
        logger.info("Fetching comments for username: {}", username);

        List<Comment> commentList = commentRepository.findByUsername(username).orElseThrow(
                () -> new CommentException("Username not found!"));

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(mapToResponseDto(comment));
        }

        logger.info("Fetched {} comments for username: {}", commentResponseDtoList.size(), username);

        return commentResponseDtoList;
    }

    @Override
    public List<CommentResponseDto> getAllCommentsByDate(LocalDate date) {
        logger.info("Fetching comments for date: {}", date);

        List<Comment> commentList = commentRepository.findByDate(date).orElseThrow(
                () -> new CommentException("Date not found or date not valid!"));

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(mapToResponseDto(comment));
        }

        logger.info("Fetched {} comments for date: {}", commentResponseDtoList.size(), date);

        return commentResponseDtoList;
    }

    @Override
    public List<CommentResponseDto> getAllCommentsByUsernameAndDate(String username, LocalDate date) {
        logger.info("Fetching comments for username: {} and date: {}", username, date);

        List<Comment> commentList = commentRepository.findByDateAndUsername(date, username).orElseThrow(
                () -> new CommentException("No comments found for username and date"));

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(mapToResponseDto(comment));
        }

        logger.info("Fetched {} comments for username: {} and date: {}", commentResponseDtoList.size(), username, date);

        return commentResponseDtoList;
    }

    @Override
    public List<CommentResponseDto> deleteAllCommentsByUsername(String username) {
        logger.info("Deleting comments for username: {}", username);

        List<Comment> commentList = commentRepository.findByUsername(username).orElseThrow(
                () -> new CommentException("Username not found"));

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(mapToResponseDto(comment));
            commentRepository.delete(comment);
        }

        logger.info("Deleted {} comments for username: {}", commentResponseDtoList.size(), username);

        return commentResponseDtoList;
    }

    @Override
    public CommentResponseDto updateCommentByUsername(String username, Long id, CommentRequestDto commentRequestDto) {
        logger.info("Updating comment for username: {} and comment ID: {}", username, id);

        Comment comment = commentRepository.findByUsernameAndCommentId(username, id).orElseThrow(
                () -> new CommentException("No comment found with username and id"));

        comment.setUsername(commentRequestDto.username());
        comment.setBody(commentRequestDto.body());
        comment.setDate(LocalDate.now());

        comment = commentRepository.save(comment);

        logger.info("Updated comment with ID: {}", comment.getCommentId());

        return mapToResponseDto(comment);
    }

    private CommentResponseDto mapToResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getBody(),
                comment.getUsername(),
                comment.getDate()
        );
    }
}
