package com.example.dunibazar.model.net

import com.example.dunibazar.model.data.LoginResponse
import com.example.dunibazar.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AutoChecker : Authenticator, KoinComponent {
    private val apiService: ApiService by inject()

    override fun authenticate(route: Route?, response: Response): Request? {

        if (TokenInMemory.token != null && !response.request.url.pathSegments.last()
                .equals("refreshToken", false)
        ) {

            if (refreshToken()) {
                return response.request
            }

        }

        return null
    }


    //behtar bod esme tabe zir mizasht isTokenrefreshed?
    private fun refreshToken(): Boolean {

        val request: retrofit2.Response<LoginResponse> = apiService.refreshToken().execute()

        if (request.body() != null) {

            if (request.body()!!.success) {
                return true
            }
        }

        return false

    }


}