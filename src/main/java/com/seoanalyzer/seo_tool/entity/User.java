package com.seoanalyzer.seo_tool.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity                         // this class = a MySQL table
@Table(name = "users")           // table name in MySQL
@Data                           // Lombok: generates getters + setters
@NoArgsConstructor              // Lombok: empty constructor (JPA needs this)
@AllArgsConstructor             // Lombok: constructor with all fields
@Builder                        // Lombok: User.builder().email("x").build()
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;             // auto-increments: 1, 2, 3...

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;         // unique = no two users same email

    @Column(nullable = false)
    private String password;      // stored as BCrypt hash, never plain text

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;           // stored as "USER" or "ADMIN" in DB

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLogin; // updated every time user logs in

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true; // admin can ban user by setting false

    @PrePersist                    // runs automatically before INSERT
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Spring Security needs these — tells it how to read your User

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; } // email = username

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}