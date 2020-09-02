package com.cavistapractical.view

import com.google.gson.JsonObject
import retrofit2.Response

interface SearchView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<JsonObject>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            page: Int,
            q: String
        )

        fun onStop()
    }
}