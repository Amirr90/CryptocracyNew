package com.e.cryptocracy.home.repo.search

import com.e.cryptocracy.utils.ApiResponse

interface SearchRepo {
    suspend fun search(map: HashMap<String, String>): ApiResponse
}