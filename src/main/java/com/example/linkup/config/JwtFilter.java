package com.example.linkup.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String username;
        String jwt = null;
        Cookie[] cookies = request.getCookies(); // Получаем все cookies из запроса

        // Ищем cookie с именем "token"
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    jwt = cookie.getValue(); // Извлекаем значение токена
                    break;
                }
            }
        }

        // Если токен не найден, продолжаем фильтрацию без авторизации
        if (jwt == null) {
            filterChain.doFilter(request, response); // Пропускаем запрос дальше
            return;
        }

        try {
            // Извлекаем имя пользователя из токена
            username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Загружаем данные о пользователе
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Проверяем, валиден ли токен
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // Если токен истёк, но остаётся валидным, генерируем новый токен
                    if (jwtService.isTokenExpired(jwt)) {
                        String newToken = jwtService.generateToken(userDetails); // Генерация нового токена

                        // Создаём новый cookie с токеном
                        Cookie newTokenCookie = new Cookie("token", newToken);
                        newTokenCookie.setHttpOnly(true); // Сделать cookie доступным только по протоколу HTTP
                        newTokenCookie.setPath("/"); // Устанавливаем путь для cookie
                        newTokenCookie.setMaxAge(60 * 60 * 24); // Устанавливаем срок действия на 1 день
                        response.addCookie(newTokenCookie); // Добавляем новый cookie в ответ
                    }

                    // Устанавливаем аутентификацию в SecurityContext
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // Пользователь
                            null, // Пароль не нужен, так как аутентификация основана на токене
                            userDetails.getAuthorities() // Присваиваем права пользователя
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request) // Добавляем детали запроса
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken); // Сохраняем аутентификацию в контексте безопасности
                }
            }
        } catch (Exception e) {
            // Обработка ошибки JWT (например, недействительный токен)
            Cookie expiredCookie = new Cookie("token", null); // Удаляем истёкший или недействительный токен
            expiredCookie.setPath("/"); // Устанавливаем путь для удаления
            expiredCookie.setMaxAge(0); // Устанавливаем срок действия cookie на 0 для удаления
            response.addCookie(expiredCookie); // Добавляем удалённый cookie в ответ
            response.sendRedirect("/authPage"); // Перенаправляем на страницу авторизации
            return;
        }

        filterChain.doFilter(request, response); // Продолжаем фильтрацию запроса
    }
}