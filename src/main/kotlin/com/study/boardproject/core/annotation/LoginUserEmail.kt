package com.study.boardproject.core.annotation

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginUserEmail

@Aspect
@Component
class LoginUserEmailAspect {
    @Around("execution(* com.study.boardproject.board.controller..*(.., @LoginUserEmail (*), ..))")
    fun injectLoginUserEmail(joinPoint: ProceedingJoinPoint): Any? {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication?.name ?: throw IllegalArgumentException("Unauthorized access")

        val methodSignature = joinPoint.signature as MethodSignature
        val args = joinPoint.args

        val newArgs = args.mapIndexed { index, arg ->
            val parameter = methodSignature.method.parameters[index]
            if (parameter.isAnnotationPresent(LoginUserEmail::class.java)) {
                email
            } else {
                arg
            }
        }.toTypedArray()

        return joinPoint.proceed(newArgs)
    }
}
