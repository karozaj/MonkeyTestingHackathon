package navigation

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
import ui.views.LoginView
import ui.views.MainView

object NavigationKeys{

}
@Serializable
data object LoginScreen: NavKey
@Serializable
data object MainScreen: NavKey
//@Serializable
//data object NoteListScreen: NavKey
//
//@Serializable
//data class NoteDetailScreen(val id: Int): NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(LoginScreen)
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
                is LoginScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        LoginView(
                            onButtonClick = { backStack.add(MainScreen) }
                        )
                    }
                }
                is MainScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        MainView(
                            onButtonClick = { backStack.add(LoginScreen) }
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        },
    )
}