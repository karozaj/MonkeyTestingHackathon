package com.example.monkeytestinghackathon.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.example.monkeytestinghackathon.viewmodels.LoginViewModel
import com.example.monkeytestinghackathon.viewmodels.MainViewModel

val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::MainViewModel)

}