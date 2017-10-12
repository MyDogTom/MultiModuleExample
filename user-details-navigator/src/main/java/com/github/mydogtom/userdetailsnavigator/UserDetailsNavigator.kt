package com.github.mydogtom.userdetailsnavigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class UserDetailsNavigator @Inject constructor() {
    fun openUserDetails(context: Context) {
        val path = context.getString(R.string.user_details_path)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("s-urn://$path"))
        context.startActivity(intent)
    }
}
