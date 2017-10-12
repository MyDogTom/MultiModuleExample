package com.github.mydogtom.login.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri

class LoginNavigator {
    fun openLogin(context: Context) {
        val path = context.getString(R.string.login_path)
        val startActivityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("s-urn://$path"))
        context.startActivity(startActivityIntent)
    }
}
