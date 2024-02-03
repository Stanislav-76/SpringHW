package ru.gb.springdemo.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.gb.springdemo.model.Role;
import ru.gb.springdemo.model.User;
import ru.gb.springdemo.repository.RoleRepository;
import ru.gb.springdemo.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = user.getRoles().stream().map(it -> new SimpleGrantedAuthority(it.getName())).toList();
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), simpleGrantedAuthorityList);
    }

    @PostConstruct
    public void generateUser() {
        roleRepository.saveAll(List.of(
                new Role("admin"),
                new Role("reader"),
                new Role("auth")
        ));

        BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
        userRepository.saveAll(List.of(
                new ru.gb.springdemo.model.User("Иван",bCryptPasswordEncoder.encode("333"), roleRepository.findAllById(List.of(1L))),
                new ru.gb.springdemo.model.User("Петр", bCryptPasswordEncoder.encode("444"), roleRepository.findAllById(List.of(2L))),
                new ru.gb.springdemo.model.User("Сергей", bCryptPasswordEncoder.encode("555"), roleRepository.findAllById(List.of(3L)))
        ));
    }

}
