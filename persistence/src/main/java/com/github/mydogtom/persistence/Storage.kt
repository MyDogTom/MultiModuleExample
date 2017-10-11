package com.github.mydogtom.persistence

class Storage {
    private val values = HashMap<String, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String): T? = values[key] as T?

    fun putValue(key: String, value: Any) {
        values.put(key, value)
    }
}