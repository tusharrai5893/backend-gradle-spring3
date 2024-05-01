package com.reddit.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Subreddit implements Serializable {


    //non-owning side of relationship
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "subreddit")
    @JsonManagedReference
    @OrderBy("createdDate")
    public Set<Post> posts = new TreeSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subredditId;


    @NotBlank(message = "Subreddit name is required")
    private String subredditName;

    //    @NotBlank(message = "Subreddit Description is Required")
    private String subredditDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @JsonIgnore
    private User user;

    private Instant createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subreddit subreddit = (Subreddit) o;
        return subredditId == subreddit.subredditId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subredditId);
    }

}
