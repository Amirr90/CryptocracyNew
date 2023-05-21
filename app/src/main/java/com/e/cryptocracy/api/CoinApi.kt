package com.e.cryptocracy.api

import com.e.cryptocracy.coinDetail.dto.CoinDetailResponse
import com.e.cryptocracy.coinDetail.dto.coinHistory.CoinHistoryResponse
import com.e.cryptocracy.currency.dto.CurrencyResponse
import com.e.cryptocracy.globalStats.dto.GlobalStatsResponse
import com.e.cryptocracy.home.dtos.CoinResponse
import com.e.cryptocracy.home.dtos.searchDto.SearchResponse
import com.e.cryptocracy.utils.Authentication
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CoinApi {


    @Authentication
    @GET(COINS)
    suspend fun loadCoins(
        @QueryMap map: HashMap<String, Any>,
    ): Response<CoinResponse>

    @Authentication
    @GET(COINS)
    suspend fun loadFavCoins(
        @Query("uuids[]") names: List<String>,
        @QueryMap map: HashMap<String, Any>,
    ): Response<CoinResponse>


    @Authentication

    @GET("$COIN_DETAIL/{coinId}")
    suspend fun loadCoinDetail(
        @Path("coinId") coinId: String,
        @QueryMap map: HashMap<String, String>,
    ): Response<CoinDetailResponse>


    @Authentication

    @GET("$COIN_DETAIL/{coinId}/history")
    suspend fun loadCoinHistory(
        @Path("coinId") coinId: String,
        @QueryMap map: HashMap<String, String>,
    ): Response<CoinHistoryResponse>


    @Authentication

    @GET(CURRENCY)
    suspend fun loadCurrency(
        @Query("search") search: String,
    ): Response<CurrencyResponse>

    @Authentication

    @GET(SEARCH)
    suspend fun search(
        @QueryMap map: HashMap<String, String>,
    ): Response<SearchResponse>


    @Authentication
/*    @WithDefaultCurrencyQueryParam*/
    @GET(STATS)
    suspend fun statsData(
        @Query("referenceCurrencyUuid") map: String,
    ): Response<GlobalStatsResponse>


    companion object {
        const val BASE_URL = "https://api.coinranking.com/v2/"
        const val CURRENCY = "reference-currencies"
        const val COINS = "coins"
        const val COIN_DETAIL = "coin"
        const val SEARCH = "search-suggestions"
        const val STATS = "stats"
    }

}
