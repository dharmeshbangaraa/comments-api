package com.pertsol.service;

import com.pertsol.dto.CommentRequestDto;
import com.pertsol.dto.CommentResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(CommentRequestDto commentRequestDto);
    List<CommentResponseDto> getAllComments();
    List<CommentResponseDto> getAllCommentsByUsername(String username);
    List<CommentResponseDto> getAllCommentsByDate(LocalDate date);
    List<CommentResponseDto> getAllCommentsByUsernameAndDate(String username, LocalDate date);
    List<CommentResponseDto> deleteAllCommentsByUsername(String username);
    CommentResponseDto updateCommentByUsername(String username, Long id, CommentRequestDto commentRequestDto);
}
