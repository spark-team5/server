package com.bamboo.log.domain.user.oauth.entity;

import com.bamboo.log.juksoon.domain.Juksoon;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;


    private String name;
    private String email;
    private String role;
    private String profile_img_url;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Juksoon juksooni;

    @Builder
    public UserEntity(String username, String name, String email, String role,String profile_img_url) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.profile_img_url = profile_img_url;
    }

    public UserEntity updateEmailAndName(String email, String name) {
        return UserEntity.builder()
                .username(this.username)
                .name(name)
                .email(email)
                .role(this.role)
                .profile_img_url(this.profile_img_url)
                .build();
    }
}