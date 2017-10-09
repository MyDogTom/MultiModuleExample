package com.github.mydogtom.login.navigator

import dagger.Module
import dagger.Provides

@Module
class LoginNavigatorModule {

    @Provides
    fun provideNavigator():LoginNavigator = LoginNavigator()
}