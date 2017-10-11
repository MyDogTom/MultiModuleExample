package com.github.mydogtom.login

import com.github.mydogtom.baseapp.FeatureScope
import com.github.mydogtom.baseapp.LoggedOutComponent
import com.github.mydogtom.persistence.UserRepository
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
        modules = arrayOf(LoginModule::class),
        dependencies = arrayOf(LoggedOutComponent::class))
@FeatureScope
interface LoginComponent {
    fun inject(activity: LogInActivity)
}

@Module
class LoginModule {
    @Provides
    fun provideLoginPresenter(repository: UserRepository) = LoginPresenter(repository)
}