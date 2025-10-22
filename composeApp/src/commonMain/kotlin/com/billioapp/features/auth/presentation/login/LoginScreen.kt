package com.billioapp.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import billioapp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinInject(),
    onNavigateToMain: () -> Unit = {},
    onNavigateToSignup: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToEmailVerification: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val socialLauncher = rememberSocialSignInLauncher(
        onGoogleSuccess = { viewModel.onEvent(LoginEvent.GoogleIdTokenReceived(it)) },
        onGoogleError = { viewModel.onEvent(LoginEvent.GoogleSignInFailed(it)) },
        onAppleSuccess = { idToken, nonce -> viewModel.onEvent(LoginEvent.AppleIdTokenReceived(idToken, nonce)) },
        onAppleError = { viewModel.onEvent(LoginEvent.AppleSignInFailed(it)) }
    )
    
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToMain -> onNavigateToMain()
                LoginEffect.NavigateToSignup -> onNavigateToSignup()
                LoginEffect.NavigateToEmailVerification -> onNavigateToEmailVerification()
                LoginEffect.RequestGoogleSignIn -> socialLauncher.launchGoogleSignIn()
                LoginEffect.RequestAppleSignIn -> socialLauncher.launchAppleSignIn()
                is LoginEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFBDE9DE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Top Image
            Image(
                painter = painterResource(Res.drawable.loginonboard),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(200.dp, 150.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Login Title
            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Welcome back subtitle
            Text(
                text = "Welcome back",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email Input Field
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                label = { Text("Email", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                ),
                isError = !state.isEmailValid && state.email.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input Field
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = { Text("Password", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    TextButton(
                        onClick = { viewModel.onEvent(LoginEvent.TogglePasswordVisibility) }
                    ) {
                        Text(
                            text = if (state.isPasswordVisible) "Gizle" else "Göster",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                ),
                isError = !state.isPasswordValid && state.password.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF2E7D32)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading &&
                    !state.isGoogleLoading &&
                    !state.isAppleLoading &&
                    state.email.isNotBlank() &&
                    state.password.isNotBlank() &&
                    state.isEmailValid &&
                    state.isPasswordValid
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF2E7D32)
                    )
                } else {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OrDivider()

            Spacer(modifier = Modifier.height(24.dp))

            SocialLoginButton(
                text = "Continue with Google",
                painter = painterResource(Res.drawable.google),
                containerColor = Color.White,
                contentColor = Color.Black,
                isLoading = state.isGoogleLoading,
                enabled = !state.isLoading && !state.isGoogleLoading && !state.isAppleLoading,
                onClick = { viewModel.onEvent(LoginEvent.GoogleLoginClicked) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialLoginButton(
                text = "Sign in with Apple",
                painter = painterResource(Res.drawable.apple),
                containerColor = Color.Black,
                contentColor = Color.White,
                isLoading = state.isAppleLoading,
                enabled = !state.isLoading && !state.isGoogleLoading && !state.isAppleLoading,
                onClick = { viewModel.onEvent(LoginEvent.AppleLoginClicked) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Create Account Link
            Text(
                text = "Create Account",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    viewModel.onEvent(LoginEvent.CreateAccountClicked)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password Link
            TextButton(
                onClick = { onNavigateToForgotPassword() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            // Error Message
            state.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = Color.Red.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.White.copy(alpha = 0.4f)
        )
        Text(
            text = "OR",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.White.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun SocialLoginButton(
    text: String,
    painter: Painter,
    containerColor: Color,
    contentColor: Color,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = contentColor
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
            }
        }
    }
}