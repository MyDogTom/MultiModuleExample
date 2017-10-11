package com.github.mydogtom.login

import com.github.mydogtom.baseapp.FeatureScope
import com.github.mydogtom.baseapp.LoggedOutComponent
import com.github.mydogtom.persistence.UserRepository
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
        modules = arrayOf(LoginModule::class),
        dependencies = arrayOf(LoggedOutComponent::class))
@FeatureScope
interface LoginComponent {
    fun inject(activity: LogInActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance fun loginView(view: LogInView): Builder
        fun loggedOutComponent(component: LoggedOutComponent): Builder
        fun build(): LoginComponent
    }
}

@Module
class LoginModule {
    @Provides
    fun provideLoginPresenter(repository: UserRepository, view: LogInView)
            = LoginPresenter(repository, view)
}
