package com.e.cryptocracy.utils

import android.util.Log

import okhttp3.*
import okio.Buffer
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Invocation


class CustomInterceptor(
    val appPrefs: AppPrefs,
) : Interceptor {
    private val vendorId = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val currencyId = AppConstant.getCurrencyId(appPrefs)
        val invocation = chain.request().tag(Invocation::class.java)
            ?: return chain.proceed(chain.request())
        val shouldAttachAuthHeader = invocation
            .method()
            .annotations
            .any {
                it.annotationClass == Authentication::class
            }

        var requestBody: RequestBody? = chain.request().body

        val runWithDefaultParams = invocation
            .method()
            .annotations
            .any {
                it.annotationClass == WithDefaultParam::class
            }


        val runWithDefaultCurrencyQueryParams = invocation
            .method()
            .annotations
            .any {
                it.annotationClass == WithDefaultCurrencyQueryParam::class
            }


        if (runWithDefaultCurrencyQueryParams) {
            val originalRequest = chain.request()
            val http = originalRequest.url

            val newHttpUrl = http.newBuilder()
                .addQueryParameter(REFERENCE_CURRENCY, currencyId)
                .build()

            val requestBuilder: Request.Builder = originalRequest.newBuilder().url(newHttpUrl)
            /*return chain.proceed(
                requestBuilder.build()
            )*/
            return chain.proceed(
                requestBuilder.addHeader(
                    x_access_token,
                    x_access_token_value
                ).build()
            )

        }





        return if (shouldAttachAuthHeader) {
            val ch = chain.request().newBuilder()
            chain.proceed(
                ch.addHeader(
                    x_access_token,
                    x_access_token_value
                ).build()
            )
        } else chain.proceed(chain.request())

    }

    private fun processQueryDataRequestBody(requestBody: Request): RequestBody {
        val url: HttpUrl = requestBody.url
            .newBuilder()
            .addQueryParameter(VendorID, vendorId)
            .addQueryParameter(
                x_access_token,
                x_access_token_value
            )
            .build()

        requestBody
            .newBuilder()
            .url(url)
            .build()
        return RequestBody.create(requestBody.body?.contentType(), url.toString())
    }

    private fun processApplicationJsonRequestBody(
        requestBody: RequestBody,
    ): RequestBody? {
        val customReq = bodyToString(requestBody)
        try {
            val data = JSONObject()
            val obj = JSONObject(customReq)
            obj.put(VendorID, vendorId)
            data.put("data", obj)
            Log.d("TAG", "processApplicationJsonRequestBody: $data")
            return RequestBody.create(requestBody.contentType(), data.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }


    private fun processFormDataRequestBody(requestBody: RequestBody): RequestBody {
        val formBody: RequestBody = FormBody.Builder()
            .add(VendorID, vendorId)
            .build()
        var postBodyString = bodyToString(requestBody)
        postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(formBody)
        return RequestBody.create(requestBody.contentType(), postBodyString)
    }

    private fun bodyToString(request: RequestBody): String {
        return try {
            val buffer = Buffer()
            request.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

    companion object {
        const val x_access_token = "x-access-token"
        const val x_access_token_value =
            "coinranking9447a114ad26bf096f2ce7a402d17b180ca58e122667bc9a"
        const val VendorID = "vendor_id"
        const val HASH_KEY = "hashkey"
        const val REFERENCE_CURRENCY = "reference-currencies"
    }
}

/*   if (runWithDefaultParams) {
            val subtype: String = chain.request().body?.contentType()?.subtype!!
            Log.d("TAG", "intercept: $subtype")

            when {
                subtype.contains("json") -> {
                }
                subtype.contains("form") ->
                subtype.contains("query") ->
            }
        }*/