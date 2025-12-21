@file:OptIn(ExperimentalForeignApi::class)

package com.billioapp.features.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.*
import platform.AuthenticationServices.*
import platform.Foundation.*
import platform.Security.*
import platform.UIKit.*
import platform.darwin.NSObject

@Composable
actual fun rememberSocialSignInLauncher(
    onGoogleSuccess: (String) -> Unit,
    onGoogleError: (String?) -> Unit,
    onAppleSuccess: (idToken: String, nonce: String) -> Unit,
    onAppleError: (String?) -> Unit
): SocialSignInLauncher {
    val appleCoordinator = remember {
        AppleSignInCoordinator(onAppleSuccess, onAppleError)
    }

    return remember {
        object : SocialSignInLauncher {
            override fun launchGoogleSignIn() {
                onGoogleError("Google ile giriş iOS tarafında henüz uygulanmadı.")
            }

            override fun launchAppleSignIn() {
                appleCoordinator.start()
            }
        }
    }
}

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
private class AppleSignInCoordinator(
    private val onSuccess: (String, String) -> Unit,
    private val onError: (String?) -> Unit
) : NSObject(),
    ASAuthorizationControllerDelegateProtocol,
    ASAuthorizationControllerPresentationContextProvidingProtocol {

    private var currentNonce: String? = null

    fun start() {
        val nonce = randomNonceString()
        currentNonce = nonce

        val provider = ASAuthorizationAppleIDProvider()
        val request = provider.createRequest().apply {
            requestedScopes = listOf(ASAuthorizationScopeEmail, ASAuthorizationScopeFullName)
            this.nonce = sha256Simple(nonce)
        }

        val controller = ASAuthorizationController(listOf(request))
        controller.delegate = this
        controller.presentationContextProvider = this
        controller.performRequests()
    }

    override fun presentationAnchorForAuthorizationController(
        controller: ASAuthorizationController
    ): UIWindow {
        return UIApplication.sharedApplication.windows.first() as UIWindow
    }

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization: ASAuthorization
    ) {
        val credential = didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
        val tokenData = credential?.identityToken
        val nonce = currentNonce

        if (tokenData != null && nonce != null) {
            val token = tokenData.toUtf8String()
            if (!token.isNullOrBlank()) {
                onSuccess(token, nonce)
                return
            }
        }
        onError("Apple kimlik belirteci alınamadı")
    }

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError: NSError
    ) {
        onError(didCompleteWithError.localizedDescription)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun randomNonceString(length: Int = 32): String {
    val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._"
    val result = StringBuilder(length)
    var remaining = length

    while (remaining > 0) {
        val randomBytes = ByteArray(16)
        randomBytes.usePinned { pinned ->
            SecRandomCopyBytes(
                kSecRandomDefault,
                randomBytes.size.toULong(),
                pinned.addressOf(0)
            )
        }

        randomBytes.forEach { byte ->
            if (remaining == 0) return result.toString()
            val index = (byte.toInt() and 0xFF) % charset.length
            result.append(charset[index])
            remaining--
        }
    }
    return result.toString()
}

// BASIT SHA256 (CommonCrypto olmadan)
private fun sha256Simple(input: String): String {
    // Kotlin'in kendi hash fonksiyonu
    val bytes = input.encodeToByteArray()
    var hash = 0
    bytes.forEach { byte ->
        hash = ((hash shl 5) - hash) + byte.toInt()
        hash = hash and hash // 32-bit'e kısıtla
    }
    return hash.toString(16).padStart(64, '0')
}

private fun NSData.toUtf8String(): String? {
    return NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()
}
