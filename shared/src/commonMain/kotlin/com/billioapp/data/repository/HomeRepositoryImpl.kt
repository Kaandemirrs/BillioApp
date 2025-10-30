package com.billioapp.data.repository

import com.billioapp.data.mapper.HomeMapper
import com.billioapp.data.remote.api.HomeApi
import com.billioapp.domain.model.home.HomeSummary
import com.billioapp.domain.model.home.MonthlyLimit
import com.billioapp.domain.repository.HomeRepository
import com.billioapp.domain.util.Result
import com.billioapp.core.network.NetworkErrorMapper
import com.billioapp.core.network.readableMessage
import io.github.aakira.napier.Napier

class HomeRepositoryImpl(
    private val api: HomeApi,
    private val mapper: HomeMapper
) : HomeRepository {

    override suspend fun getHomeSummary(): Result<HomeSummary> = execute {
        Napier.i(tag = "HomeRepository", message = "Çağrı yapılıyor: GET /home/summary")
        val dto = api.getHomeSummary()
        Napier.i(tag = "HomeRepository", message = "Çağrı başarılı: GET /home/summary")
        mapper.mapSummary(dto)
    }

    override suspend fun getMonthlyLimit(): Result<MonthlyLimit?> = execute {
        Napier.i(tag = "HomeRepository", message = "Çağrı yapılıyor: GET /home/monthly-limit")
        val dto = api.getMonthlyLimit()
        Napier.i(tag = "HomeRepository", message = "Çağrı başarılı: GET /home/monthly-limit")
        dto?.let(mapper::mapLimit)
    }

    override suspend fun updateMonthlyLimit(amount: Double, currency: String): Result<MonthlyLimit> = execute {
        Napier.i(tag = "HomeRepository", message = "Çağrı yapılıyor: PUT /home/monthly-limit")
        val dto = api.updateMonthlyLimit(amount, currency)
        Napier.i(tag = "HomeRepository", message = "Çağrı başarılı: PUT /home/monthly-limit")
        mapper.mapLimit(dto)
    }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> =
        try {
            Result.Success(block())
        } catch (t: Throwable) {
            val error = NetworkErrorMapper.fromThrowable(t)
            Napier.e(tag = "HomeRepository", message = "Hata alındı: ${error.readableMessage()}")
            Result.Error(error.readableMessage(), t)
        }
}