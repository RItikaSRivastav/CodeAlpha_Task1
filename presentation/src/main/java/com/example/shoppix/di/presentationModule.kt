package com.example.shoppix.di

import org.koin.dsl.module

val presentationModule = module {
 includes(viewModelModule)
}