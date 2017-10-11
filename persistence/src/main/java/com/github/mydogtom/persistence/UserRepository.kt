package com.github.mydogtom.persistence

private const val FIELD_USER_NAME = "user.name"
private const val FIELD_USER_ADDRESS = "user.address"
class UserRepository(private val storage: Storage) {

    fun setUserName(name: String) {
        storage.putValue(FIELD_USER_NAME, name)
    }

    fun getUserName(): UserName = UserName(storage.getValue<String>(FIELD_USER_NAME).orEmpty())


    fun setAddress(address: String) {
        storage.putValue(FIELD_USER_ADDRESS, address)
    }

    fun getAddress(): String = storage.getValue<String>(FIELD_USER_ADDRESS).orEmpty()
}
