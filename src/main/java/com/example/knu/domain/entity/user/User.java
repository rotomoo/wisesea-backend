package com.example.knu.domain.entity.user;

import com.example.knu.domain.entity.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String loginId;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean emailReceiveYn;

    private String nickname;

    private String profileImageUrl;



    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateUser(String username, UserType userType, Boolean emailReceiveYn) {
        this.username = username;
        this.userType = userType;
        this.emailReceiveYn = emailReceiveYn;
    }
}
