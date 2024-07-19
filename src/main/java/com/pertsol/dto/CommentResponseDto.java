package com.pertsol.dto;

import java.time.LocalDate;

public record CommentResponseDto(
        Long commentId,
        String body,
        String username,
        LocalDate date
) {
}
