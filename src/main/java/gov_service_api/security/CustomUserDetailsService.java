package gov_service_api.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import gov_service_api.model.User;  // Ваш пользовательский класс
import gov_service_api.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Преобразуем username в Long id
        Long id = Long.parseLong(username);

        // Ищем пользователя по id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Возвращаем пользователя с его паролем
        // Здесь создаем объект org.springframework.security.core.userdetails.User
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),  // Имя пользователя (String)
                user.getPassword(),  // Зашифрованный пароль
                // Указываем роли пользователя
                List.of(new SimpleGrantedAuthority("ROLE_USER"))  // Пример роли "USER"
        );
    }

    public boolean validateUser(Long id, String rawPassword) {
        // Получаем пользователя
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;  // Пользователь не найден
        }

        // Сравниваем введённый пароль с зашифрованным
        return PasswordUtil.checkPassword(rawPassword, user.getPassword());
    }
}

