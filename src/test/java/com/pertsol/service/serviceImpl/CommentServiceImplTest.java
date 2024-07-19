package com.pertsol.service.serviceImpl;

import com.pertsol.dto.CommentRequestDto;
import com.pertsol.dto.CommentResponseDto;
import com.pertsol.entity.Comment;
import com.pertsol.exception.CommentException;
import com.pertsol.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Test
    void testSaveComment() {
        CommentRequestDto requestDto = new CommentRequestDto("testBody", "testUser");
        Comment comment = new Comment(1L, "testBody", "testUser", LocalDate.now());

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto response = commentServiceImpl.saveComment(requestDto);

        assertNotNull(response);
        assertEquals("testUser", response.username());
        assertEquals("testBody", response.body());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testGetAllComments() {
        List<Comment> commentList = Arrays.asList(new Comment(1L, "testUser", "testBody", LocalDate.now()));
        when(commentRepository.findAll()).thenReturn(commentList);

        List<CommentResponseDto> responseList = commentServiceImpl.getAllComments();

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCommentsByUsername() {
        List<Comment> commentList = Arrays.asList(new Comment(1L, "testUser", "testBody", LocalDate.now()));
        when(commentRepository.findByUsername(anyString())).thenReturn(Optional.of(commentList));

        List<CommentResponseDto> responseList = commentServiceImpl.getAllCommentsByUsername("testUser");

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(commentRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void testGetAllCommentsByDate() {
        List<Comment> commentList = Arrays.asList(new Comment(1L, "testUser", "testBody", LocalDate.now()));
        when(commentRepository.findByDate(any(LocalDate.class))).thenReturn(Optional.of(commentList));

        List<CommentResponseDto> responseList = commentServiceImpl.getAllCommentsByDate(LocalDate.now());

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(commentRepository, times(1)).findByDate(any(LocalDate.class));
    }

    @Test
    void testGetAllCommentsByUsernameAndDate() {
        List<Comment> commentList = Arrays.asList(new Comment(1L, "testUser", "testBody", LocalDate.now()));
        when(commentRepository.findByDateAndUsername(any(LocalDate.class), anyString())).thenReturn(Optional.of(commentList));

        List<CommentResponseDto> responseList = commentServiceImpl.getAllCommentsByUsernameAndDate("testUser", LocalDate.now());

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(commentRepository, times(1)).findByDateAndUsername(any(LocalDate.class), anyString());
    }

    @Test
    void testDeleteAllCommentsByUsername() {
        List<Comment> commentList = Arrays.asList(new Comment(1L, "testUser", "testBody", LocalDate.now()));
        when(commentRepository.findByUsername(anyString())).thenReturn(Optional.of(commentList));

        List<CommentResponseDto> responseList = commentServiceImpl.deleteAllCommentsByUsername("testUser");

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(commentRepository, times(1)).findByUsername(anyString());
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void testUpdateCommentByUsername() {
        CommentRequestDto requestDto = new CommentRequestDto("updatedBody", "testUser");
        Comment comment = new Comment(1L, "testUser", "testBody", LocalDate.now());

        when(commentRepository.findByUsernameAndCommentId(anyString(), anyLong())).thenReturn(Optional.of(comment));

        CommentResponseDto response = commentServiceImpl.updateCommentByUsername("testUser", 1L, requestDto);

        assertNotNull(response);
        assertEquals("testUser", response.username());
        assertEquals("updatedBody", response.body());
        verify(commentRepository, times(1)).findByUsernameAndCommentId(anyString(), anyLong());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testGetAllCommentsByUsername_NotFound() {
        when(commentRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CommentException.class, () -> {
            commentServiceImpl.getAllCommentsByUsername("unknownUser");
        });

        String expectedMessage = "Username not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAllCommentsByDate_NotFound() {
        when(commentRepository.findByDate(any(LocalDate.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(CommentException.class, () -> {
            commentServiceImpl.getAllCommentsByDate(LocalDate.now());
        });

        String expectedMessage = "Date not found or date not valid!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAllCommentsByUsernameAndDate_NotFound() {
        when(commentRepository.findByDateAndUsername(any(LocalDate.class), anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CommentException.class, () -> {
            commentServiceImpl.getAllCommentsByUsernameAndDate("unknownUser", LocalDate.now());
        });

        String expectedMessage = "No comments found for username and date";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteAllCommentsByUsername_NotFound() {
        when(commentRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CommentException.class, () -> {
            commentServiceImpl.deleteAllCommentsByUsername("unknownUser");
        });

        String expectedMessage = "Username not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateCommentByUsername_NotFound() {
        when(commentRepository.findByUsernameAndCommentId(anyString(), anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CommentException.class, () -> {
            commentServiceImpl.updateCommentByUsername("unknownUser", 1L, new CommentRequestDto("testUser", "updatedBody"));
        });

        String expectedMessage = "No comment found with username and id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
