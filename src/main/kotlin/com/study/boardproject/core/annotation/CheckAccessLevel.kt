package com.study.boardproject.core.annotation

import com.study.boardproject.user.entity.enums.Level
import com.study.boardproject.user.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckAccessLevel(val requiredLevels: IntArray)

@Aspect
@Component
class AccessLevelAspect(private val userService: UserService) {
    @Around("@annotation(checkAccessLevel)")
    fun checkAccessLevel(joinPoint: ProceedingJoinPoint, checkAccessLevel: CheckAccessLevel): Any? {
        val user = userService.getCurrentUser()
        val userLevel = user?.level ?: Level.EMPTY

        if (!checkAccessLevel.requiredLevels.any { it <= userLevel.value }) {
            throw IllegalAccessException("접근 권한이 없습니다.")
        }

        return joinPoint.proceed()
    }
}