package com.github.mydogtom.userdetails

import com.github.mydogtom.baseapp.FeatureScope
import com.github.mydogtom.baseapp.LoggedInComponent
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = arrayOf(LoggedInComponent::class))
@FeatureScope
interface UserDetailsComponent {
    fun inject(activity: UserDetailsActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun userDetailsView(view: UserDetailsView): Builder

        fun loggedInComponent(component: LoggedInComponent): Builder
        fun build(): UserDetailsComponent
    }
}