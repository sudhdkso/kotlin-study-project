package com.study.boardproject.core.exception

import com.study.boardproject.core.response.ErrorResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    //InvalidInputException이 발생하면 아래 함수를 호출한다.
    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException) : ResponseEntity<ErrorResponse> {
        val errors = ErrorResponse(ex.message ?: "Not Exception Message")
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ErrorResponse {
        return ErrorResponse(
            errorType = "INVALID_ARGUMENT",
            message = ex.message ?: "Invalid arguments provided"
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<List<ErrorResponse>> {
        val errors = ex.bindingResult.allErrors.map { error ->
            val errorMessage = (error as FieldError).defaultMessage ?: "Invalid value"
            ErrorResponse(message = errorMessage)
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun invalidInputException(ex: ConstraintViolationException) : ResponseEntity<List<ErrorResponse>> {
        val errors = ex.constraintViolations.map { violation ->
            ErrorResponse(message = violation.message)
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}