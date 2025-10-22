package com.billioapp.features.auth.presentation.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.billioapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
actual fun rememberSocialSignInLauncher(
    onGoogleSuccess: (String) -> Unit,
    onGoogleError: (String?) -> Unit,
    onAppleSuccess: (idToken: String, nonce: String) -> Unit,
    onAppleError: (String?) -> Unit
): SocialSignInLauncher {
    val context = LocalContext.current
    val activity = context.findActivity()

    val googleClient = rememberGoogleClient(context)

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            onGoogleError("Google ile giriş iptal edildi")
            return@rememberLauncherForActivityResult
        }
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (!idToken.isNullOrBlank()) {
                onGoogleSuccess(idToken)
            } else {
                onGoogleError("Google kimlik belirteci alınamadı")
            }
        } catch (e: Exception) {
            Log.e("SocialSignIn", "Google sign-in failed", e)
            onGoogleError(e.message)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            googleClient.signOut()
        }
    }

    return remember {
        object : SocialSignInLauncher {
            override fun launchGoogleSignIn() {
                val hostActivity = activity
                if (hostActivity == null) {
                    onGoogleError("Google ile giriş başlatılamadı")
                    return
                }
                googleClient.signOut()
                googleLauncher.launch(googleClient.signInIntent)
            }

            override fun launchAppleSignIn() {
                onAppleError("Apple ile giriş Android üzerinde desteklenmiyor.")
            }
        }
    }
}

@Composable
private fun rememberGoogleClient(context: Context): GoogleSignInClient {
    return remember {
        val webClientId = runCatching { context.getString(R.string.default_web_client_id) }
            .getOrElse {
                Log.w("SocialSignIn", "default_web_client_id not found: ${it.message}")
                ""
            }

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .apply {
                if (webClientId.isNotBlank()) {
                    requestIdToken(webClientId)
                }
            }
            .build()

        GoogleSignIn.getClient(context, options)
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
