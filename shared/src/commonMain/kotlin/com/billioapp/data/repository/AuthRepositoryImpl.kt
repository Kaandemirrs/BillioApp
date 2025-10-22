package com.billioapp.data.repository

import com.billioapp.data.mapper.UserMapper
import com.billioapp.domain.model.AuthResult
import com.billioapp.domain.repository.AuthRepository
import com.billioapp.domain.util.Result
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val userMapper: UserMapper
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResult> =
        execute {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            val user = firebaseAuth.currentUser
                ?: error("Kullanıcı bilgisi alınamadı")
            AuthResult(userMapper.map(user))
        }

    override suspend fun register(email: String, password: String): Result<AuthResult> =
        execute {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
            val user = firebaseAuth.currentUser
                ?: error("Kullanıcı oluşturuldu ancak bilgiler alınamadı")
            AuthResult(userMapper.map(user))
        }

    override suspend fun logout(): Result<Unit> =
        execute {
            firebaseAuth.signOut()
        }

    override suspend fun getCurrentUser() = execute {
        userMapper.mapOrNull(firebaseAuth.currentUser)
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        execute {
            firebaseAuth.sendPasswordResetEmail(email)
        }

    override suspend fun sendEmailVerification(): Result<Unit> =
        execute {
            firebaseAuth.currentUser?.sendEmailVerification()
                ?: error("Oturum açık kullanıcı bulunamadı")
        }

    override suspend fun isEmailVerified(): Result<Boolean> =
        execute {
            firebaseAuth.currentUser?.isEmailVerified ?: false
        }

    override suspend fun reloadUser(): Result<Unit> =
        execute {
            firebaseAuth.currentUser?.reload()
                ?: error("Oturum açık kullanıcı bulunamadı")
        }

    override suspend fun signInWithGoogle(idToken: String): Result<AuthResult> =
        execute {
            firebaseAuth.signInWithCredential(GoogleAuthProvider.credential(idToken, null))
            val user = firebaseAuth.currentUser
                ?: error("Google ile giriş sonrası kullanıcı bilgisi alınamadı")
            AuthResult(userMapper.map(user))
        }

    override suspend fun signInWithApple(idToken: String, nonce: String): Result<AuthResult> =
        execute {
            val credential = OAuthProvider.credential(
                providerId = "apple.com",
                idToken = idToken,
                rawNonce = nonce
            )
            firebaseAuth.signInWithCredential(credential)
            val user = firebaseAuth.currentUser
                ?: error("Apple ile giriş sonrası kullanıcı bilgisi alınamadı")
            AuthResult(userMapper.map(user))
        }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> =
        withContext(Dispatchers.Default) {
            try {
                Result.Success(block())
            } catch (throwable: Throwable) {
                Result.Error(
                    message = throwable.message ?: "Beklenmeyen bir hata oluştu",
                    throwable = throwable
                )
            }
        }
}
