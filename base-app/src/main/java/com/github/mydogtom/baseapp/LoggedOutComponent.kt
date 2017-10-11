package com.github.mydogtom.baseapp

import android.content.Context
import com.github.mydogtom.persistence.Storage
import com.github.mydogtom.persistence.UserRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(PersistenceModule::class, SubComponentsBindigsModule::class))
interface LoggedOutComponent {
    fun userRepository(): UserRepository

    fun loggedInComponentBuilder(): LoggedInComponent.Builder
}

@Module
class PersistenceModule {
    @Provides
    @Singleton
    fun provideStorage(): Storage = Storage()

    @Provides
    @Singleton
    fun provideUserRepository(storage: Storage): UserRepository = UserRepository(storage)
}


interface LoggedOutComponentProvider {
    fun provideLoggedOutComponent(): LoggedOutComponent
}

@Suppress("UnsafeCast")
fun Context.loggedOutComponent() =
        (this.applicationContext as LoggedOutComponentProvider).provideLoggedOutComponent()
