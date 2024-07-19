package com.pertsol.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @NotNull(message = "Comment body must not be null")
        String body,
        @NotNull(message = "Username must not be null")
        @Size(min = 2, max = 20, message = "Username should be between 2 and 20 characters")
        String username
) {
}
