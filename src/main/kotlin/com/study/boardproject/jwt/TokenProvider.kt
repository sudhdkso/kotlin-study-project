package com.study.boardproject.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*


@PropertySource("classpath:jwt.yml")
@Service
class TokenProvider(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${access-token-expire-time}")
    private val accessExpireTime: Long,
    @Value("\${refresh-token-expire-time}")
    private val refreshExpireTime: Long,
    private val userDetailsService: UserDetailsService
) {

    fun createToken(authentication: Authentication): String {
        val claims = Jwts.claims().setSubject(authentication.getName())
        val now = Date()
        val expireDate = Date(now.time + accessExpireTime)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun createRefreshToken(authentication: Authentication): String {
        val claims = Jwts.claims().setSubject(authentication.name)
        val now = Date()
        val expireDate = Date(now.time + refreshExpireTime)
        val refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

        return refreshToken
    }

    fun getEmail(token: String?) : String {
        return Jwts
            .parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun getAuthentication(token: String?): Authentication {
        val userPrincipal = Jwts.parserBuilder().setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body.subject
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(userPrincipal)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token)
            true
        } catch (e: ExpiredJwtException) {
            log.error(e.message)
            throw IllegalArgumentException("토큰이 만료되었습니다.")
        } catch (e: JwtException) {
            log.error(e.message)
            throw IllegalArgumentException("옳지않은 토큰입니다.")
        }
    }
}