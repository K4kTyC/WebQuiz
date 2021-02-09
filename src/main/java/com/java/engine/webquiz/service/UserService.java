package com.java.engine.webquiz.service;

import com.java.engine.webquiz.model.*;
import com.java.engine.webquiz.repository.CompletedQuizRepository;
import com.java.engine.webquiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompletedQuizRepository completedQuizRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       CompletedQuizRepository completedQuizRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.completedQuizRepository = completedQuizRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found a user with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean registerUser(UserDto userDto) {
        User user = new User(userDto);
        if (isUserExists(user)) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return true;
    }

    public void saveCompletedQuiz(CompletedQuiz completedQuiz) {
        completedQuizRepository.save(completedQuiz);
    }

    public Page<CompletedQuizDto> getCompletedQuizzesByUserId(int pageNumber, long id) {
        PageRequest paging = PageRequest.of(pageNumber, 10);
        Page<CompletedQuiz> paged = completedQuizRepository.findByUserIdOrderByCompletedAtDesc(id, paging);

        return paged.map(CompletedQuizDto::new);
    }

    public boolean isUserExists(User user) {
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }
}
