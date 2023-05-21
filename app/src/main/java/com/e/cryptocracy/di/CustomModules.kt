package com.e.cryptocracy.di

import com.e.cryptocracy.coinDetail.repo.CoinDetailRepo
import com.e.cryptocracy.coinDetail.repo.CoinDetailRepoImpl
import com.e.cryptocracy.currency.repo.CurrencyRepo
import com.e.cryptocracy.currency.repo.CurrencyRepoImpl
import com.e.cryptocracy.globalStats.repo.GlobalStatsRepo
import com.e.cryptocracy.globalStats.repo.GlobalStatsRepoImpl
import com.e.cryptocracy.home.repo.HomeRepo
import com.e.cryptocracy.home.repo.HomeRepoImpl
import com.e.cryptocracy.home.repo.search.SearchRepo
import com.e.cryptocracy.home.repo.search.SearchRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class CustomModules {

    @Binds
    @Singleton
    abstract fun bindsCoinHomeImpl(userRepo: HomeRepoImpl): HomeRepo


    @Binds
    @Singleton
    abstract fun bindsCurrencyImpl(userRepo: CurrencyRepoImpl): CurrencyRepo

    @Binds
    @Singleton
    abstract fun bindsSearchImpl(userRepo: SearchRepoImpl): SearchRepo

    @Binds
    @Singleton
    abstract fun bindsCoinDetailImpl(userRepo: CoinDetailRepoImpl): CoinDetailRepo

    @Binds
    @Singleton
    abstract fun bindsStatsRepoImpl(userRepo: GlobalStatsRepoImpl): GlobalStatsRepo
}