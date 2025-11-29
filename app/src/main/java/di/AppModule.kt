package di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import viewmodels.LoginViewModel
import viewmodels.MainViewModel

val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::MainViewModel)

}