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
import com.example.monkeytestinghackathon.ui.views.AddEventView
import kotlinx.serialization.Serializable
import com.example.monkeytestinghackathon.ui.views.LoginView
import com.example.monkeytestinghackathon.ui.views.EventListView

object NavigationKeys{
    @Serializable
    data object LoginScreen: NavKey
    @Serializable
    data object EventListScreen: NavKey
    @Serializable
    data class EventDetailScreen(val eventId: String): NavKey
    @Serializable
    data object AddEventScreen: NavKey
}

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(NavigationKeys.LoginScreen)
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
                            onButtonClick = { backStack.add(NavigationKeys.EventListScreen) }
                        )
                    }
                }
                is NavigationKeys.EventListScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        EventListView(
                            onAddEventButtonClicked = { backStack.add(NavigationKeys.AddEventScreen) }
                        )
                    }
                }
                is NavigationKeys.AddEventScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                         AddEventView()
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        },
    )
}