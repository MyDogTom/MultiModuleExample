package com.github.mydogtom.multimoduleexample

import com.github.mydogtom.login.navigator.LoginNavigatorModule
import dagger.Component

@Component(modules = arrayOf(LoginNavigatorModule::class))
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}