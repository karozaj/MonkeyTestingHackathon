package com.example.monkeytestinghackathon.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import com.example.monkeytestinghackathon.models.Firebase_CredentialManager
import com.example.monkeytestinghackathon.ui.views.AddEventView
import com.example.monkeytestinghackathon.ui.views.EventDetailView
import kotlinx.serialization.Serializable
import com.example.monkeytestinghackathon.ui.views.LoginView
import com.example.monkeytestinghackathon.ui.views.EventListView
import com.example.monkeytestinghackathon.ui.views.RegisterUserView
import kotlinx.coroutines.launch

object NavigationKeys{
    @Serializable
    data object LoginScreen: NavKey
    @Serializable
    data class EventListScreen(val userId: String): NavKey
    @Serializable
    data class EventDetailScreen(val eventId: String): NavKey
    @Serializable
    data object AddEventScreen: NavKey
    @Serializable
    data class RegisterScreen(val userId: String): NavKey
}

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(NavigationKeys.LoginScreen)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = Firebase_CredentialManager(context)
    var uid: String = ""

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
                    NavEntry(key = key) {
                        LoginView(
                            onGoogleSignInClick = {
                                scope.launch {
                                    val result = credentialManager.signIn()
                                    if (result.errorMessage == null && result.data != null) {
                                        Toast.makeText(
                                            context,
                                            "UID: ${result.data.userId}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        uid = result.data.userId
                                        backStack.add(NavigationKeys.RegisterScreen(uid))
                                    } else {
                                        Log.i("logowanie",result.errorMessage ?: "Logowanie nieudane")
                                        Toast.makeText(
                                            context,
                                            result.errorMessage ?: "Logowanie nieudane",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                }
                is NavigationKeys.EventListScreen -> {
                    NavEntry(key = key) {
                        EventListView(
                            onAddEventButtonClicked = { backStack.add(NavigationKeys.AddEventScreen) },
                            userId = uid
                        )
                    }
                }
                is NavigationKeys.AddEventScreen -> {
                    NavEntry(
                        key=key
                    ){
                        AddEventView()
                    }
                }
                is NavigationKeys.RegisterScreen -> {
                    NavEntry(key = key) {
                        RegisterUserView(
                            userId = key.userId,
                            onRegistrationComplete = {
                                backStack.add(NavigationKeys.EventListScreen(key.userId))
                            }
                        )
                    }
                }
                is NavigationKeys.EventDetailScreen -> {
                    NavEntry(key = key) {
                        EventDetailView(key.eventId)
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        },
    )
}