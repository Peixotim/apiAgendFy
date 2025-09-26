package marketing.digital.rj.apiagendfy.infra.security;

import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import marketing.digital.rj.apiagendfy.Users.repository.usersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final usersRepository repository;

    public CustomUserDetailsService(usersRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) { //Aqui tem que passar o email em username
        var u = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));

        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUserMail())
                .password(u.getPassword())
                .authorities(authorities)
                .disabled(!u.isEnabled())
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .build();
    }
}
