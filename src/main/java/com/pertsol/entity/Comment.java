package com.pertsol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long commentId;
    @Column(name = "Text")
    private String body;
    @Column(name = "Comment_By")
    private String username;
    @Column(name = "dateofcomment")
    private LocalDate date = LocalDate.now();
}
