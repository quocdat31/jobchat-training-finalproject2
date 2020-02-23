package com.example.finalproject2.ultis

import com.example.finalproject2.model.SignInRequest
import com.example.finalproject2.model.SignUpRequest
import java.util.regex.Pattern

object ValidationCheck {

    private const val EMAIL_REGEX =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    private const val MIN_PASSWORD_LENGTH = 6

    fun isEmailValid(email: String) = Pattern.matches(EMAIL_REGEX, email)

    fun isPasswordValid(password: String) = password.length >= MIN_PASSWORD_LENGTH

    fun isConfirmPasswordMatch(password: String, confirmPassword: String) =
        isPasswordValid(password) && password == confirmPassword

    fun isSignUpValid(registerRequest: SignUpRequest): Boolean {
        return isEmailValid(registerRequest.email.toString()) && isConfirmPasswordMatch(
            registerRequest.password.toString(),
            registerRequest.confirmPassword.toString()
        )
    }

    fun isSignInValid(loginRequest: SignInRequest) =
        isEmailValid(loginRequest.email.toString()) && isPasswordValid(
            loginRequest.password.toString()
        )
}
