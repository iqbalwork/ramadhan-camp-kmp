package com.iqbalwork.ramadhancamp.shared.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.flow.SharedFlow


/**
 * Navigation levels:
 *  - Root back stack  : [RootDestination] — auth, main, full-screen overlays
 *  - Tab back stacks  : [TabDestination]  — screens inside each bottom-tab navigator
 *  - Active dialog    : [DialogDestination]? — bottom-sheet dialogs (driven by Compose state)
 */
class AppNavigationController(
    val rootBackStack: NavBackStack<NavKey>,
    val tabBackStacks: Map<AppTab, SnapshotStateList<TabDestination>>,
    val currentTab: MutableState<AppTab>,
    val activeDialogs: SnapshotStateList<DialogDestination>,
    private val resultRepository: ResultNavigationRepository,
) : AppNavigationAction, ResultNavigationAction {

    // ─── Root navigation ─────────────────────────────────────────────────────

    override fun navigateTo(dest: RootDestination, withReplace: Boolean) {
        if (withReplace) {
            if (rootBackStack.isNotEmpty()) rootBackStack[rootBackStack.lastIndex] = dest
            else rootBackStack.add(dest)
        } else {
            rootBackStack.add(dest)
        }
    }

    // ─── In-tab navigation ───────────────────────────────────────────────────

    override fun navigateToInsideTab(dest: TabDestination, withReplace: Boolean) {
        val stack = tabBackStacks[currentTab.value] ?: return
        if (withReplace) {
            if (stack.isNotEmpty()) stack[stack.lastIndex] = dest
            else stack.add(dest)
        } else {
            stack.add(dest)
        }
    }

    // ─── Back navigation ─────────────────────────────────────────────────────

    override fun back(navigationResult: NavigationResult?) {
        navigationResult?.let { sendResult(it) }
        val tabStack = tabBackStacks[currentTab.value]
        when {
            activeDialogs.isNotEmpty() -> activeDialogs.removeLastOrNull()
            tabStack != null && tabStack.size > 1 -> tabStack.removeLastOrNull()
            rootBackStack.size > 1 -> rootBackStack.removeLastOrNull()
        }
    }

    override fun backToScreen(key: String, navigationResult: NavigationResult?) {
        navigationResult?.let { sendResult(it) }
        val tabStack = tabBackStacks[currentTab.value]
        if (tabStack != null && tabStack.size > 1) {
            while (tabStack.size > 1 && tabStack.last().navKey != key) {
                tabStack.removeLastOrNull()
            }
        } else {
            while (rootBackStack.size > 1 && rootBackStack.last().navKey != key) {
                rootBackStack.removeLastOrNull()
            }
        }
    }

    // ─── Flow / tab navigation ───────────────────────────────────────────────

    override fun startNewFlow(dest: RootDestination, withReplace: Boolean) {
        if (withReplace) {
            rootBackStack.clear()
            rootBackStack.add(dest)
        } else {
            rootBackStack.add(dest)
        }
    }

    override fun switchTab(tab: AppTab) {
        currentTab.value = tab
    }

    // ─── Dialog navigation ───────────────────────────────────────────────────

    override fun showDialog(dialog: DialogDestination) {
        activeDialogs.add(dialog)
    }

    override fun hideDialog() {
        activeDialogs.removeLastOrNull()
    }

    // ─── Navigation results ──────────────────────────────────────────────────

    override fun sendResult(value: NavigationResult) {
        resultRepository.sendResult(value.key, value)
    }

    override fun removeKey(key: String) {
        resultRepository.removeKey(key)
    }

    override fun subscribeToResult(key: String): SharedFlow<NavigationResult> {
        return resultRepository.getResultFlow(key)
    }
}

// ─── Nav key extensions ──────────────────────────────────────────────────────

/** Stable string key derived from the destination's class name + content. */
val NavKey.navKey: String get() = this::class.simpleName + "@" + this.hashCode()

// ─── Interfaces ──────────────────────────────────────────────────────────────

interface AppNavigationAction {
    fun navigateTo(dest: RootDestination, withReplace: Boolean = false)
    fun navigateToInsideTab(dest: TabDestination, withReplace: Boolean = false)
    fun back(navigationResult: NavigationResult? = null)
    fun backToScreen(key: String, navigationResult: NavigationResult? = null)
    fun startNewFlow(dest: RootDestination, withReplace: Boolean = false)
    fun switchTab(tab: AppTab)
    fun showDialog(dialog: DialogDestination)
    fun hideDialog()
}

interface ResultNavigationAction {
    fun sendResult(value: NavigationResult)

    fun removeKey(key: String)

    fun subscribeToResult(key: String): SharedFlow<NavigationResult>
}
// ─── Factory ─────────────────────────────────────────────────────────────────

@Composable
fun rememberAppNavigationController(
    startDestination: RootDestination,
    resultRepository: ResultNavigationRepository,
): AppNavigationController {
    val rootBackStack = rememberNavBackStack(appSavedStateConfig, startDestination)
    val currentTab = rememberSaveable { mutableStateOf(AppTab.Home) }
    val activeDialogs = remember { mutableStateListOf<DialogDestination>() }
    val tabBackStacks = remember {
        mapOf(
            AppTab.Home to mutableStateListOf(TabDestination.HomeMain as TabDestination),
            AppTab.Pray to mutableStateListOf(TabDestination.PrayMain as TabDestination),
            AppTab.Quran to mutableStateListOf(TabDestination.QuranMain as TabDestination),
            AppTab.Qibla to mutableStateListOf(TabDestination.QiblaMain as TabDestination),
            AppTab.Bookmark to mutableStateListOf(TabDestination.BookmarkMain as TabDestination),
        )
    }
    return remember(rootBackStack) {
        AppNavigationController(
            rootBackStack = rootBackStack,
            tabBackStacks = tabBackStacks,
            currentTab = currentTab,
            activeDialogs = activeDialogs,
            resultRepository = resultRepository,
        )
    }
}