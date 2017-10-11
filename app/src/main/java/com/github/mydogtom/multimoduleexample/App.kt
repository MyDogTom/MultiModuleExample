package com.github.mydogtom.multimoduleexample

import android.app.Application
import com.github.mydogtom.baseapp.DaggerLoggedOutComponent
import com.github.mydogtom.baseapp.LoggedOutComponent
import com.github.mydogtom.baseapp.LoggedOutComponentProvider

class App : Application(), LoggedOutComponentProvider {
    private val loggedOutComponent: LoggedOutComponent by lazy {
        DaggerLoggedOutComponent.builder()
                .build()
    }

    override fun provideLoggedOutComponent(): LoggedOutComponent = loggedOutComponent
}