package com.github.mydogtom.baseapp

import dagger.Module

@Module(subcomponents = arrayOf(LoggedInComponent::class))
class SubComponentsBindigsModule
