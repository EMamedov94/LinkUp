package com.example.linkup.models;

import com.example.linkup.enums.Role;
import com.example.linkup.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"posts", "sentFriendships", "receivedFriendships"})
public class User extends BaseEntity implements UserDetails {

    private String username;
    private String password;

//    @Pattern(regexp = "^[А-Яа-я]", message = "Имя должно начинаться с заглавной буквы и быть написано русскими буквами")
    private String firstName;

//    @Pattern(regexp = "^[А-Яа-я]", message = "Фамилия должно начинаться с заглавной буквы и быть написано русскими буквами")
    private String lastName;

    @Transient
    private String token;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Friendship> sentFriendships;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Friendship> receivedFriendships;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    public User(Long id) {
        super(id);
    }
}
