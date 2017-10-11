package com.github.mydogtom.baseapp

import android.content.Context
import com.github.mydogtom.persistence.Storage
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(PersistenceModule::class))
interface LoggedOutComponent {
    fun storage(): Storage
}

@Module
class PersistenceModule {
    @Provides
    @Singleton
    fun provideStorage(): Storage = Storage()
}


interface LoggedOutComponentProvider {
    fun provideLoggedOutComponent(): LoggedOutComponent
}

fun Context.loggedOutComponent() =
        (this.applicationContext as LoggedOutComponentProvider).provideLoggedOutComponent()