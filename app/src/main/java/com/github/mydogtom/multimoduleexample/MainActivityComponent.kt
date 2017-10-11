package com.github.mydogtom.multimoduleexample

import com.github.mydogtom.login.navigator.LoginNavigatorModule
import dagger.Component
import dagger.Module
import dagger.Subcomponent

@Component(modules = arrayOf(LoginNavigatorModule::class))
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}