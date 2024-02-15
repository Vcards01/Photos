package br.edu.ifsp.scl.photos.model

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.net.HttpURLConnection

class JsonPlaceHolderAPI(context: Context) {

    companion object{

        const val PRODUCTS_ENDPOINT = "https://jsonplaceholder.typicode.com/photos/"

        @Volatile
        private var INSTANCE: JsonPlaceHolderAPI? =null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this){
            INSTANCE ?: JsonPlaceHolderAPI(context).also {
                INSTANCE=it
            }
        }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue( request: Request<T>){
        requestQueue.add(request)
    }

    class PhotoRequest(
        private val responseListener: Response.Listener<Photo>,
        errorListener: Response.ErrorListener
    ): Request<Photo>(Method.GET, PRODUCTS_ENDPOINT, errorListener){
        override fun parseNetworkResponse(response: NetworkResponse?): Response<Photo> {
            return if(response?.statusCode == HttpURLConnection.HTTP_OK || response?.statusCode == HttpURLConnection.HTTP_NOT_MODIFIED){
                String(response.data).run {
                    Response.success(
                        Gson().fromJson(this, Photo::class.java),
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }else{
                Response.error((VolleyError()))
            }
        }

        override fun deliverResponse(response: Photo?) {
            responseListener.onResponse(response)
        }
    }


}