package com.github.mydogtom.login.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri

class LoginNavigator {
    fun openLogin(context: Context) {
        val startActivityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("s-urn://login"))
        context.startActivity(startActivityIntent)
    }
}
