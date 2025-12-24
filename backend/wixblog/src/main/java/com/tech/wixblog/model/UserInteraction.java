package com.tech.wixblog.model;

import com.tech.wixblog.model.enums.InteractionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionType type;
    @Column
    private Double value;
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime interactedAt = LocalDateTime.now();

    public int getWeight () {
        return type.getWeight();
    }
}