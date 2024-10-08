package com.study.boardproject.core.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider
): GenericFilterBean()  {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?,chain: FilterChain?) {
        val token = tokenProvider.resolveToken(request as HttpServletRequest)

        if (token != null && tokenProvider.validateToken(token)) {
            val authentication = tokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain?.doFilter(request, response)
    }

}