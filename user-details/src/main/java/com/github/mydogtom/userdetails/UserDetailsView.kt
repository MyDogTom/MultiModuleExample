package com.github.mydogtom.userdetails

import com.github.mydogtom.persistence.UserName

interface UserDetailsView {
    fun showData(userName: UserName)
    fun showAddress(address: String)
}