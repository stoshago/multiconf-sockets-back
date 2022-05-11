package ua.nix.multiconfback.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper = true)
@Entity
public class User extends AbstractIdModel implements UserDetails {

    @Column(name = "login", updatable = false, nullable = false, unique = true)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return login;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return login.equals(user.login) && password.equals(user.password) && displayName.equals(user.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, displayName);
    }
}
