package com.bwee.webit.service;

import com.bwee.webit.exception.UserNotFoundException;
import com.bwee.webit.datasource.WebitUserDbService;
import com.bwee.webit.exception.UsernameExistsException;
import com.bwee.webit.model.WebitUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WebitUserService extends SimpleCrudService<WebitUser> implements UserDetailsService {

    private final WebitUserDbService dbService;
    private final PasswordEncoder passwordEncoder;
    private final RandomIdGenerator idGenerator;

    @Autowired
    public WebitUserService(final WebitUserDbService dbService,
                            final PasswordEncoder passwordEncoder,
                            final RandomIdGenerator idGenerator) {
        super(dbService);
        this.dbService = dbService;
        this.passwordEncoder = passwordEncoder;
        this.idGenerator = idGenerator;
    }

    @Override
    public WebitUser findByIdStrict(final String id) {
        return dbService.findById(id).orElseThrow(() -> UserNotFoundException.ofId(id));
    }

    public Optional<WebitUser> findByUsername(final String username) {
        return dbService.findByUsername(username);
    }

    public WebitUser findByUsernameStrict(final String username) {
        return findByUsername(username).orElseThrow(() -> UserNotFoundException.ofUsername(username));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return findByUsername(username).map(WebitUserDetails::new)
                .orElseThrow(() -> UserNotFoundException.ofUsername(username));
    }

    public WebitUser createNewUser(final CreateNewUser createNewUser) {
        if (findByUsername(createNewUser.getUsername()).isPresent()) {
            throw new UsernameExistsException();
        }

        final WebitUser user = new WebitUser()
                .setUsername(createNewUser.getUsername())
                .setName(createNewUser.getName())
                .setPassword(passwordEncoder.encode(createNewUser.password))
                .setRoles(createNewUser.getRoles());

        user.setId(idGenerator.generateId(user));

        save(user);

        return user;
    }

    @Data
    @Accessors(chain = true)
    public static class CreateNewUser {
        private String name;
        private String username;
        private String password;
        private List<String> roles = Collections.emptyList();
    }

    public static class WebitUserDetails implements UserDetails {
        private final WebitUser user;

        public WebitUserDetails(final WebitUser user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return user.getRoles().stream().map(role -> (GrantedAuthority) () -> role).collect(Collectors.toList());
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
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
}
