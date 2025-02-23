package com.tech.wixblog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;



    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode () {
        return Objects.hash(id, name);
    }
}
