package com.github.mydogtom.userdetailsnavigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject


class UserDetailsNavigator @Inject constructor(){
    fun openUserDetails(context: Context){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("s-urn://user-details"))
        context.startActivity(intent)
    }
}