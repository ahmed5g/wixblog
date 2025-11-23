package com.tech.wixblog.config;

import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if user already exists
        if (userRepository.findByEmail("ahmed.ghorbel@howa.com").isEmpty()) {
            User user = new User();
            user.setEmail("ahmed.ghorbel@howa.com");
            user.setName("Ahmed Ghorbel");
            user.setPassword(passwordEncoder.encode("ahmed1234"));

            user.setEnabled(true);

            user.setPublicProfile(true);
            user.setShowOnlineStatus(true);
            user.setNewsletterSubscribed(false);

            userRepository.save(user);
            System.out.println("Demo user created: ahmed.ghorbel@howa.com / ahmed1234");
        }
    }
}
