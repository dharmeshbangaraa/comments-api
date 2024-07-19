package com.pertsol.controller;

import com.pertsol.dto.CommentRequestDto;
import com.pertsol.dto.CommentResponseDto;
import com.pertsol.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    CommentRequestDto requestDto = new CommentRequestDto(
            "Test Comment",
            "Test User"
    );
    CommentResponseDto responseDto = new CommentResponseDto(
            1L,
            "Test Comment",
            "Test User",
            LocalDate.now()
    );

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testPostComment() {


        when(commentService.saveComment(any(CommentRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<CommentResponseDto> response = commentController.postComment(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).saveComment(requestDto);
    }

    @Test
    public void testGetComments() {
        List<CommentResponseDto> responseList = Arrays.asList(responseDto,responseDto);

        when(commentService.getAllComments()).thenReturn(responseList);

        ResponseEntity<List<CommentResponseDto>> response = commentController.getComments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void testGetCommentByUsernameOrDate_UsernameOnly() {
        List<CommentResponseDto> responseList = Collections.singletonList(responseDto);

        when(commentService.getAllCommentsByUsername(anyString())).thenReturn(responseList);

        ResponseEntity<List<CommentResponseDto>> response = commentController.getCommentByUsernameOrDate("Test User", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).getAllCommentsByUsername("Test User");
    }

    @Test
    public void testGetCommentByUsernameOrDate_DateOnly() {
        List<CommentResponseDto> responseList = Collections.singletonList(responseDto);

        when(commentService.getAllCommentsByDate(any(LocalDate.class))).thenReturn(responseList);

        ResponseEntity<List<CommentResponseDto>> response = commentController.getCommentByUsernameOrDate(null, LocalDate.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).getAllCommentsByDate(any(LocalDate.class));
    }

    @Test
    public void testGetCommentByUsernameOrDate_UsernameAndDate() {
        List<CommentResponseDto> responseList = Arrays.asList(responseDto);

        when(commentService.getAllCommentsByUsernameAndDate(anyString(), any(LocalDate.class))).thenReturn(responseList);

        ResponseEntity<List<CommentResponseDto>> response = commentController.getCommentByUsernameOrDate("Test User", LocalDate.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).getAllCommentsByUsernameAndDate(anyString(), any(LocalDate.class));
    }

    @Test
    public void testDeleteCommentsByUsername() {
        List<CommentResponseDto> responseList = Arrays.asList(responseDto, responseDto);

        when(commentService.deleteAllCommentsByUsername(anyString())).thenReturn(responseList);

        ResponseEntity<List<CommentResponseDto>> response = commentController.deleteCommentsByUsername("Test User");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).deleteAllCommentsByUsername("Test User");
    }

    @Test
    public void testUpdateComment() {

        when(commentService.updateCommentByUsername(anyString(), anyLong(), any(CommentRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<CommentResponseDto> response = commentController.updateComment("user1", 1L, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).updateCommentByUsername(anyString(), anyLong(), any(CommentRequestDto.class));
    }
}
