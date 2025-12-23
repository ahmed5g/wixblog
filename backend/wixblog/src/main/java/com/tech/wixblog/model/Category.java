package com.tech.wixblog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categorie")
@Getter // Instead of @Data
@Setter // Instead of @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// This is the critical part: Only use simple fields for equality checks
@EqualsAndHashCode(callSuper = false, exclude = {"parent", "children", "suggestedTags"})
@ToString(exclude = {"parent", "children", "suggestedTags"})
public class Category extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String slug;
    private boolean featured = false;
    // Self-referential for parent-child hierarchy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference // Prevents parent from serializing children again
    private Category parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC")
    @Builder.Default
    @JsonManagedReference // Normal serialization of children
    private Set<Category> children = new HashSet<>();
    @OneToMany(mappedBy = "suggestedCategory")
    @JsonManagedReference
    private Set<Tag> suggestedTags = new HashSet<>();


}