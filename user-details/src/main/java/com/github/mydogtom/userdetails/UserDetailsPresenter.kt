package com.github.mydogtom.userdetails

import com.github.mydogtom.baseapp.FeatureScope
import com.github.mydogtom.persistence.UserName
import com.github.mydogtom.persistence.UserRepository
import javax.inject.Inject

@FeatureScope
class UserDetailsPresenter @Inject constructor(private val userName: UserName,
                                               private val view: UserDetailsView,
                                               private val userRepository: UserRepository) {
    init {
        view.showData(userName)
        view.showAddress(userRepository.getAddress())
    }

    fun save(address: String) {
        userRepository.setAddress(address)

    }
}
