package com.billioapp.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.billioapp.features.auth.presentation.forgotpassword.ForgotPasswordResetScreen
import com.billioapp.features.auth.presentation.forgotpassword.ForgotPasswordScreen
import com.billioapp.features.auth.presentation.login.LoginScreen
import com.billioapp.features.auth.presentation.register.RegisterScreen
import com.billioapp.features.auth.presentation.emailverification.EmailVerificationScreen
import com.billioapp.features.auth.presentation.otp.OtpScreen
import com.billioapp.features.home.presentation.HomeScreen
import com.billioapp.features.onboarding.presentation.OnboardingScreen
import com.billioapp.features.onboarding.presentation.currency.CurrencyLanguageSelectionScreen
import com.billioapp.features.onboarding.presentation.legal.PrivacyPolicyScreen
import com.billioapp.features.onboarding.presentation.start.GetStartedScreen
import org.koin.compose.koinInject
import com.billioapp.features.onboarding.presentation.OnboardingViewModel
import com.billioapp.features.profile.presentation.ProfileScreen
import com.billioapp.features.ai.presentation.AiScreen
import com.billioapp.features.ai.presentation.AiAnalysisScreen
import com.billioapp.features.ai.presentation.AiPriceFinderScreen
import com.billioapp.features.notifications.presentation.NotificationListScreen
import com.billioapp.features.notifications.presentation.NotificationListViewModel

sealed interface BillioScreen : Screen

class OnboardingRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: OnboardingViewModel = koinInject()

        OnboardingScreen(
            viewModel = viewModel,
            onNavigateToMain = {
                navigator.replace(LoginRoute())
            }
        )
    }
}

class LoginRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LoginScreen(
            onNavigateToMain = {
                navigator.replace(CurrencyLanguageSelectionRoute())
            },
            onNavigateToSignup = {
                navigator.push(RegisterRoute())
            },
            onNavigateToForgotPassword = {
                navigator.push(ForgotPasswordRoute())
            },
            onNavigateToEmailVerification = {
                navigator.replace(EmailVerificationRoute())
            }
        )
    }
}

class RegisterRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        RegisterScreen(
            onNavigateToEmailVerification = {
                navigator.replace(EmailVerificationRoute())
            },
            onNavigateBackToLogin = {
                navigator.pop()
            }
        )
    }
}

class OtpRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        OtpScreen(
            onVerifyClick = {
                navigator.replaceAll(CurrencyLanguageSelectionRoute())
            },
            onResendOtpClick = {
                // TODO: Hook up resend OTP logic when backend is ready
            },
            onBackToLoginClick = {
                navigator.replaceAll(LoginRoute())
            }
        )
    }
}

class ForgotPasswordRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ForgotPasswordScreen(
            onNavigateBack = { resetStack ->
                if (resetStack) {
                    navigator.replaceAll(LoginRoute())
                } else {
                    runCatching { navigator.pop() }
                }
            }
        )
    }
}

class ForgotPasswordResetRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ForgotPasswordResetScreen(
            onPasswordResetClick = {
                navigator.replace(LoginRoute())
            },
            onBackToLoginClick = {
                navigator.replace(LoginRoute())
            }
        )
    }
}

class CurrencyLanguageSelectionRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        CurrencyLanguageSelectionScreen(
            onSaveClick = {
                navigator.push(PrivacyPolicyRoute())
            },
            onBackClick = {
                navigator.replace(LoginRoute())
            }
        )
    }
}

class PrivacyPolicyRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        PrivacyPolicyScreen(
            onContinueClick = {
                navigator.push(GetStartedRoute())
            },
            onBackClick = {
                navigator.pop()
            }
        )
    }
}

class GetStartedRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        GetStartedScreen(
            onGetStartedClick = {
                navigator.replaceAll(HomeRoute())
            }
        )
    }
}

class HomeRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        HomeScreen(
            onNavigateToLogin = {
                navigator.replaceAll(LoginRoute())
            }
        )
    }
}

class EmailVerificationRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        EmailVerificationScreen(
            onNavigateToHome = {
                navigator.replaceAll(HomeRoute())
            },
            onNavigateToLogin = {
                navigator.replaceAll(LoginRoute())
            }
        )
    }
}

class ProfileRoute : BillioScreen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ProfileScreen(
            onNavigateToLogin = {
                navigator.replaceAll(LoginRoute())
            }
        )
    }
}

class AiRoute : BillioScreen {
    @Composable
    override fun Content() {
        AiScreen()
    }
}

class AiAnalysisRoute : BillioScreen {
    @Composable
    override fun Content() {
        AiAnalysisScreen()
    }
}

class AiPriceFinderRoute : BillioScreen {
    @Composable
    override fun Content() {
        AiPriceFinderScreen()
    }
}

class PaywallRoute : BillioScreen {
    @Composable
    override fun Content() {
        com.billioapp.features.premium.presentation.PaywallScreen()
    }
}

object NotificationList : BillioScreen {
    @Composable
    override fun Content() {
        val viewModel: NotificationListViewModel = koinInject()
        NotificationListScreen(viewModel)
    }
}
