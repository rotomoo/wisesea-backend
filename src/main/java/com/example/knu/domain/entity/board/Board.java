package com.example.knu.domain.entity.board;

import com.example.knu.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String name;
    private int priority;
    private String description;

    @Builder
    public Board(String name, int priority, String description) {
        this.name = name;
        this.priority = priority;
        this.description = description;
    }
}
