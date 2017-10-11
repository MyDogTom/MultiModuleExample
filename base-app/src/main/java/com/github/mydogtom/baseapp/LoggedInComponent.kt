package com.github.mydogtom.baseapp

import android.content.Context
import com.github.mydogtom.persistence.UserName
import com.github.mydogtom.persistence.UserRepository
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@LoggedInScope
@Subcomponent(modules = arrayOf(LoggedInModule::class))
interface LoggedInComponent {

    fun provideUser(): UserName

    fun provideUserRepository(): UserRepository

    @Subcomponent.Builder
    interface Builder {
        fun build(): LoggedInComponent
    }
}

@Module
class LoggedInModule {
    @LoggedInScope
    @Provides
    fun provideUser(userRepository: UserRepository) = userRepository.getUserName()
}


interface LoggedInComponentProvider {
    fun provideLoggedInComponent(): LoggedInComponent
}

fun Context.loggedInComponent() =
        (this.applicationContext as LoggedInComponentProvider).provideLoggedInComponent()