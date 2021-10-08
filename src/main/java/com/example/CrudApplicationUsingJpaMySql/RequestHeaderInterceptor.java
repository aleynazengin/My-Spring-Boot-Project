package com.example.CrudApplicationUsingJpaMySql;

import com.example.CrudApplicationUsingJpaMySql.annotation.AllowAnnonymous;
import com.example.CrudApplicationUsingJpaMySql.entity.User;
import com.example.CrudApplicationUsingJpaMySql.exception.NotAuthorizedException;
import com.example.CrudApplicationUsingJpaMySql.repo.UserRepository;
import com.example.CrudApplicationUsingJpaMySql.service.JwtUtil;
import com.example.CrudApplicationUsingJpaMySql.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j //Causes lombok to generate a logger field
@Component
public class RequestHeaderInterceptor implements HandlerInterceptor {
    private UserService userService;
    private UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RequestHeaderInterceptor(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository=userRepository;
    }
    @SneakyThrows //allows you to throw checked exceptions without using the throws declaration
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
       final AllowAnnonymous allowAnnonymous = ((HandlerMethod)handler).getMethod().getAnnotation((AllowAnnonymous.class));
        String tokenHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if(allowAnnonymous != null){
            response.addHeader("Interceptor", "Authorized");
            return true;
        }
        if (allowAnnonymous == null && tokenHeader==null) {
            response.addHeader("Interceptor", "Authorization not sent");
            log.info("Authorization not sent.");
            throw new NotAuthorizedException();


        }
        else{
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
                email = jwtUtil.getSubject(token);

            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = (UserDetails) userRepository.findByEmail(email);
                User user=userRepository.findByEmail(email);
                if (jwtUtil.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    response.addHeader("Interceptor", "Authorization OK");
                    log.info("Validation OK.");
                }
            }}
            return true;

    }  }

