package com.billioapp.core.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val error: NetworkError) : NetworkResult<Nothing>()
}

sealed class NetworkError(open val message: String? = null, open val cause: Throwable? = null) {
    data class Unauthorized(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class Forbidden(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class NotFound(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class Conflict(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class BadRequest(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class ServerError(val statusCode: Int, override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class Timeout(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class NoInternet(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
    data class Unknown(override val message: String? = null, override val cause: Throwable? = null) : NetworkError(message, cause)
}

fun NetworkError.readableMessage(): String = when (this) {
    is NetworkError.Unauthorized -> "Yetkilendirme başarısız. Lütfen tekrar giriş yapın."
    is NetworkError.Forbidden -> "Bu işlemi gerçekleştirme yetkiniz bulunmuyor."
    is NetworkError.NotFound -> "İstenen kaynak bulunamadı."
    is NetworkError.Conflict -> "Çakışma hatası oluştu."
    is NetworkError.BadRequest -> "Geçersiz istek. Lütfen alanları kontrol edin."
    is NetworkError.ServerError -> "Sunucu hatası (${statusCode}). Lütfen daha sonra tekrar deneyin."
    is NetworkError.Timeout -> "İstek zaman aşımına uğradı. Ağ bağlantınızı kontrol edin."
    is NetworkError.NoInternet -> "İnternet bağlantısı bulunamadı."
    is NetworkError.Unknown -> "Beklenmeyen bir hata oluştu."
}