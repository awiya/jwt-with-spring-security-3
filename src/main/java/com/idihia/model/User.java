package com.idihia.model;

import com.idihia.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static jakarta.persistence.EnumType.STRING;
import static java.util.List.of;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_USER")
public class User implements UserDetails {
    @Id @GeneratedValue
    private Integer id;
    @NotEmpty(message = "Please fill the first name")
    @Column(length = 25)
    private String firstName;
    @NotEmpty(message = "Please fill the last name")
    @Column(length = 25)
    private String lastName;
    @NotEmpty(message = "Please fill the email")
    @Email
    @Column(length = 40)
    private String email;
    @NotEmpty(message = "Password is empty")
    @Column(length = 60)
    private String password;

    @Enumerated(STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
