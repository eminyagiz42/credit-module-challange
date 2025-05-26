package com.eminyagiz.creditmodule.security;

import com.eminyagiz.creditmodule.common.exception.AuthorizationTokenException;
import com.eminyagiz.creditmodule.model.entity.User;
import com.eminyagiz.creditmodule.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            log.debug("JWTFilter invoked for {}", request.getRequestURL());

            if (authorization != null && authorization.startsWith("Bearer ")) {
                validateToken(authorization);
            }
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(String authorizationtoken) {
        final String jwt = authorizationtoken.substring(7);
        final String userNameJWT = jwtUtil.extractUsername(jwt);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            User user = userService.getUserByName(userNameJWT);
            if (jwtUtil.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(user.getRoleName())
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } else {
            throw new AuthorizationTokenException("Cannot validate user token");
        }
    }
}


