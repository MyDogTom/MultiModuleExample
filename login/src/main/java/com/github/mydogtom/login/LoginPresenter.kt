package com.github.mydogtom.login

import com.github.mydogtom.persistence.UserRepository

class LoginPresenter(private val userRepository: UserRepository) {
    fun performLogIn(name: String) {
        userRepository.setUserName(name)
    }
}