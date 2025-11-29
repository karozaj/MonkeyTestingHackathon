package com.example.monkeytestinghackathon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Serializable
import com.example.monkeytestinghackathon.ui.views.LoginView
import com.example.monkeytestinghackathon.ui.views.MainView
import com.example.monkeytestinghackathon.ui.views.RegisterUserView

object NavigationKeys{
    @Serializable
    data object LoginScreen: NavKey
    @Serializable
    data object MainScreen: NavKey
    @Serializable
    data object RegisterScreen: NavKey
}

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(NavigationKeys.RegisterScreen) // Should be LoginScreen in final app
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is NavigationKeys.LoginScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        LoginView(
                            onButtonClick = { backStack.add(NavigationKeys.MainScreen) }
                        )
                    }
                }
                is NavigationKeys.MainScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        MainView(
                            onButtonClick = { backStack.add(NavigationKeys.LoginScreen) }
                        )
                    }
                }
                is NavigationKeys.RegisterScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        RegisterUserView()
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        },
    )
}