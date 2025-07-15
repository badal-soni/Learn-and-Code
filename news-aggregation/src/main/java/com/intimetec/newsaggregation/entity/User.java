package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"email"}, callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private String username;

    @Column(
            nullable = false,
            unique = true
    )
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "savedBy", fetch = FetchType.EAGER)
    private List<SavedArticle> savedArticles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<NotificationConfiguration> notificationConfigurations;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Keyword> keywords;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private List<Notification> notifications;

    @OneToMany(
            mappedBy = "interactionUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<NewsLikes> userInteractions = new ArrayList<>();

    @OneToMany(mappedBy = "reportedBy")
    private List<ReportedNews> reportedNews = new ArrayList<>();

    @OneToMany(mappedBy = "readBy")
    private List<NewsReadHistory> newsRead = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
