# Android application structure with multiple modules
 An example how android application can be structured using multiple modules. With the focus on how to configure [Dagger](https://google.github.io/dagger/). 
 
## Benefits from having multiple modules
Benefits come into play when you have a big application with many developers.
  * Faster incremental compilation time. Instead of compiling everything, it's enough to compile the only module affected by the changes and dependent modules.
  * Better isolation. It's much harder to introduce cross feature dependency. And it's much easier to visualize dependencies between features.
  * Easier collaboration, since different developers are responsible for different features.
  
See also [How modularisation affects build time of an android application](https://medium.freecodecamp.org/how-modularisation-affects-build-time-of-an-android-application-43a984ce9968). Note that article was published in January 2017. Since that time Google did a lot of improvements. Especially in plugin version 3.0.

## Requirements for modularization
* Minimize dependencies between modules
* Completely eliminate circular dependencies
* Modules should be as small as possible

## Application structure
### Screens
* MainActivity eg. splash screen. In this example, it shows button and always redirects to LoginActivity. In the real world, it could check if the user is logged in and redirect to the appropriate screen.
* LoginActivity. Accepts username (eg. login) and redirects to UserDetailsActivity.
* UserDetailsActivity - shows username and allows to change address.

### Project Modules 
* persistence - provides persistence functionality. No dependencies on other modules.
* base-app - depends on `persistence` module.
* login - login feature. Depends on `base-app`, `login-navigator` and `user-details-navigator`. 
* login-navigator - helper for navigating to the login screen. No dependencies on other modules.
* user-details - use details feature. Depends on `base-app`, `user-details-navigator`.
* user-details-navigator - helper for navigating to user details screen. No dependencies on other modules.
* app - actual application. This is top level module which composes everything together. Contains `MainActivity` since it's default activity.

## Dagger structure and learnings

The main question is how to organize dependency injection between different application modules. 
Dagger provides two mechanisms for relating components (see [Component relationships](https://google.github.io/dagger/api/latest/dagger/Component.html) form JavaDoc). Subcomponents and component dependencies. 

### Subcomponents
We cannot use subcomponents because they are tightly coupled with parents (parent component should know the complete list of subcomponents). In other words, this is the circular dependency between application modules. That contradicts to our initial requirements. 

Another disadvantage
>the subcomponent implementation to inherit the entire binding graph from its parent

This allows to easily "leak" dependency. I prefer to be more explicit about what dependencies should be visible, even if it means writing more code.

But subcomponents are good choice to be used within one application module.
 
### Component dependencies
 
In that case, parent component knows nothing about dependent components. Also, we have to explicitly list all classes that we want to be available for dependent components. Let's see an example. `base-app` app-module has `LoggedOutComponent` which provides `UserRepository` and know nothing about who will use it.
```kotlin
@Singleton
@Component(modules = arrayOf(PersistenceModule::class, SubComponentsBindigsModule::class))
interface LoggedOutComponent {
    fun userRepository(): UserRepository
}
```

`LoginComponent`, from `login` app-module, depends on `LoggedOutComponent`. This allows to inject `UserRepository` into `LoginPresenter`

```kotlin
@Component(
        modules = arrayOf(LoginModule::class),
        dependencies = arrayOf(LoggedOutComponent::class))
@FeatureScope
interface LoginComponent {
    fun inject(activity: LogInActivity)

    @Component.Builder
    interface Builder {
        fun loggedOutComponent(component: LoggedOutComponent): Builder
        fun build(): LoginComponent
    }
}
```

### How to get reference to base component
`LoggedOutComponent` resides in `base-app` module which is the bottom level module. `login` module depends on it. Actual instance for `LoggedOutComponent` is created in `App` class inside `app` top-level module. We don't want to introduce the dependency from `login` module to `app` module. Otherwise, it would mean circular dependency. 

In order to mitigate that, `base-app` module defines interface `LoggedOutComponentProvider`
 ```kotlin
 interface LoggedOutComponentProvider {
     fun provideLoggedOutComponent(): LoggedOutComponent
 }
```
Application has to implement this interface. That allows to get easily access component by casting `context.getApplicationContext` to `LoggedOutComponentProvider`. That's even easier to do with extension function
 ```kotlin
 @Suppress("UnsafeCast")
 fun Context.loggedOutComponent() =
         (this.applicationContext as LoggedOutComponentProvider).provideLoggedOutComponent()
 ```
 and use it inside `LoginActivity`
 ```kotlin
 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_log_in)

     DaggerLoginComponent.builder()
             .loginView(this)
             .loggedOutComponent(this.loggedOutComponent())
             .build()
             .inject(this)

     logInButton.setOnClickListener {
         presenter.performLogIn(userNameEditText.text.toString())
     }
 }
```

This approach has one disadvantage - absence of compile time verification. Developer can forget to implement `LoggedOutComponentProvider` and notice that only as runtime error. Fortunately, we can mitigate this disadvantage by having instrumented tests. 

Eliminate circular dependency is much more important.  


### Scopes

"Component dependencies" is very powerful technique. It allows composing different components into one. We could write something like that
 ```kotlin
 @Component(
         modules = arrayOf(LoginModule::class),
         dependencies = arrayOf(LoggedOutComponent::class, LoggedInComponent.class))
 ```
 
 Unfortunately, this has limitation. Only one component, from "dependencies array" can have scope. All other should be unscoped. Example above will work, if `LoggedOutComponennt` has `@Singlethon` scope and `LoggedInComponent` is without scope. But won't work, if `LoggedInComponent` has scope (`@Singlethon` or any other).
 
 That's why we in this repo we introduce `LoggedInComponent` which is subcomponent of `LoggedOutComponent`. It provides the same `UserRepository` and `UserName` (which make sense only when user is logged in).
 
 ```kotlin
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
```

### @BindInstance
Another learning, but not related to "multiple modules" theme. Presenter has dependency on View. Instead of creating `setView` method we can inject it via constructor. All you need is to tell Dagger, that View dependency will be provided during component creation.
```kotlin
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
```
See [binding instances documentation](https://google.github.io/dagger/users-guide.html#binding-instances)

### Dagger summary
* Use component dependency for inter app-modules.
* Break circular dependencies by introducing "component provider" contract
* SubComponents can be used only within same app-module, because they force circular dependency

## Performance

Let's see how multi-module project affects build time. I will compare two different android Gradle plugin versions `2.3.3` and `3.0.0-beta7`.

### How you assemble matters

Version 2.3.3
`./gradlew clean assembleDebug --profile` ~28 sec
`./gradlew clean :app:assembleDebug --profile` ~ 18 sec
My assumption is the following. `:app:assembleDebug` compiles `app` module and all dependent modules. In case of clean build - all of them.
Plain `assembleDebug` do same as `:app:assembleDebug` plus `:peristence:assembleDebug`, `:base-app:assembleDebug` etc. This introduces additional actions which blows up build time.

Version 3.0.0-beta7
`./gradlew clean assembleDebug --profile` ~13 sec
`./gradlew clean :app:assembleDebug --profile` ~ 13 sec
 
It seems that latest version not only faster but also "smarter". The difference is visible when we compare profile. `:app:assembleDebug` executes less tasks than `assembleDebug`. And version 3.0.0 `assembleDebug` executes less tasks than version 2.3.3 `assembleDebug`.
 
 ### Flavors and build types
  
  Let's take a close look at the generated profile. Version 2.3.3 `:app:assembleDebug` contains task `:user-details:compileReleaseKotlin`. That's strange since we are compiling debug build. Version 3.0.0 contains `:user-details:compileDebugKotlin`, which is more logical. Same happens with flavors. Versions 3.0.0 can properly propagate compiled flavor to all app-modules. Before all flavors were compiled regardless your choice.
  
  ### Incremental compilation.
  
  Let's see how incremental compilation is influenced by module division. When we change `LoginActivity` from `login` module,  only `login` and `app` modules are recompiled. All other modules are "UP-TO_DATE".  When we change `Storage` from `persistence` module, all dependent modules (`login`, `base-app`, `user-details`) are recompiled. Navigator modules are not recompiled because they do not depend on `persistence` module.
  
  ### Summary
  * Only affected code is recompiled. It's possible to achieve huge build time improvement, by creating multiple small independent modules.
  * Use latest Android Gradle plugin version. Version 3.0.0 has a lot of improvements and bugfixes for multi-module projects.
 
 ## Other learning
 
 * It's not possible to user [ButterKnife](http://jakewharton.github.io/butterknife/) in library modules while using `kapt`. See [details](https://github.com/JakeWharton/butterknife/issues/974#issuecomment-307585969). But it's possible to use ButterKnife in library module when you are using Java (see [details](https://github.com/JakeWharton/butterknife#library-projects)).
 * Layout names should be unique across all modules. Let's say "main_actiity" layout is present in both `login`and `app` modules. In that case `LoginActivity` will use "main_activity" layout from `app` module instead of one from `login` module.
 * `app` , top level module, should include all “screen” modules via compile project(":user-details"). Otherwise screen won’t be included into application at all and produce runtime error when we try to open it.
 * Since resulting `AndroidManifest.xml` is merging result of all `AndroidManifest.xml` it’s possible declare `UserDetailsActivity` in `user-details` module and specify `parentActivityName` later in `AndroidManifest.xml` from app module.
 