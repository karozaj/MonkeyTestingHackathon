package com.example.monkeytestinghackathon.di

import com.example.monkeytestinghackathon.viewmodels.AddEventViewModel
import com.example.monkeytestinghackathon.viewmodels.EventDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.example.monkeytestinghackathon.viewmodels.LoginViewModel
import com.example.monkeytestinghackathon.viewmodels.EventListViewModel

val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::EventListViewModel)
    viewModelOf(::AddEventViewModel)
    viewModelOf(::EventDetailViewModel)
}