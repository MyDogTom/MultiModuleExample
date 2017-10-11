package com.github.mydogtom.multimoduleexample

import android.app.Application
import com.github.mydogtom.baseapp.DaggerLoggedOutComponent
import com.github.mydogtom.baseapp.LoggedInComponent
import com.github.mydogtom.baseapp.LoggedInComponentProvider
import com.github.mydogtom.baseapp.LoggedOutComponent
import com.github.mydogtom.baseapp.LoggedOutComponentProvider

class App : Application(), LoggedOutComponentProvider, LoggedInComponentProvider {
    private val loggedOutComponent: LoggedOutComponent by lazy {
        DaggerLoggedOutComponent.builder()
                .build()
    }

    private val loggedInComponent: LoggedInComponent by lazy {
        loggedOutComponent.loggedInComponentBuilder()
                .build()
    }

    override fun provideLoggedOutComponent(): LoggedOutComponent = loggedOutComponent

    override fun provideLoggedInComponent(): LoggedInComponent = loggedInComponent
}
