package com.study.boardproject.core.configuration

import com.study.boardproject.jwt.JwtAuthenticationFilter
import com.study.boardproject.jwt.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    private val entryPoint: AuthenticationEntryPoint
) {
    private val whiteListURL = arrayOf("/","/signup","/login","/error")

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity) = http
        .csrf { it.disable() }
        .cors { it.disable() }
        .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
        .authorizeHttpRequests {
            it.requestMatchers(*whiteListURL).permitAll()
                .requestMatchers("/api/**").hasAnyAuthority("USER")
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
        .build()

}