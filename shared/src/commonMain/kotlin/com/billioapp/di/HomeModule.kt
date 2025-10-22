package com.billioapp.di

import com.billioapp.data.remote.client.ApiConfig
import com.billioapp.data.remote.client.provideHttpClient
import dev.gitlive.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val homeModule = module {
    // Base URL: ortamdan al, yoksa default değer kullan
    single { ApiConfig(baseUrl = (System.getenv("BILLIO_API_URL") ?: "https://api.billioapp.dev").removeSuffix("/")) }

    // HttpClient provider
    single { provideHttpClient(get<ApiConfig>()) }

    // İlerleyen aşamada buraya API, repository ve usecase binding’leri eklenecek
}