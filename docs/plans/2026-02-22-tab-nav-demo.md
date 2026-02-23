# Tab Nav Demo — Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Refactor nav entry registration to per-tab extension files and build full demo screens covering every AppNavigationController action.

**Architecture:** EntryProviderBuilder extension functions per tab. Each tab's `[Tab]NavEntries.kt` exposes `fun EntryProviderBuilder<NavKey>.[tab]TabEntries(nav: AppNavigationController)`. `MainScreen.kt` dispatches via `when(tab)`. Per-tab `DialogDestination` entries replace the global `SampleDialog`. `AuthScreen` demos root navigation actions.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Navigation3 (1.0.0-alpha05), Koin 4.1.1, `org.koin.core.module.dsl.viewModel`

---

### Task 1: Add TextResult

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/shared/common/navigation/ResultNavigationRepository.kt`

**Step 1:** Append at end of file:

```kotlin
data class TextResult(val text: String) : NavigationResultData
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/shared/common/navigation/ResultNavigationRepository.kt
git commit -m "feat(nav): add TextResult for demo navigation result passing"
```

---

### Task 2: Rewrite AppDestination.kt

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/shared/common/navigation/AppDestination.kt`

**Step 1:** Replace entire file:

```kotlin
package com.iqbalwork.ramadhancamp.shared.common.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

enum class AppTab { Home, Pray, Quran, Qibla, Bookmark }

@Serializable
sealed interface RootDestination : NavKey {
    @Serializable data class Main(val initialTab: AppTab = AppTab.Home) : RootDestination
    @Serializable data object Auth : RootDestination
}

@Serializable
sealed interface TabDestination : NavKey {
    // Home
    @Serializable data object HomeMain       : TabDestination
    @Serializable data object HomeDetail     : TabDestination
    @Serializable data object HomeSubDetail  : TabDestination
    // Pray
    @Serializable data object PrayMain       : TabDestination
    @Serializable data object PrayDetail     : TabDestination
    @Serializable data object PraySubDetail  : TabDestination
    // Quran
    @Serializable data object QuranMain      : TabDestination
    @Serializable data object QuranDetail    : TabDestination
    @Serializable data object QuranSubDetail : TabDestination
    // Qibla
    @Serializable data object QiblaMain      : TabDestination
    @Serializable data object QiblaDetail    : TabDestination
    @Serializable data object QiblaSubDetail : TabDestination
    // Bookmark
    @Serializable data object BookmarkMain      : TabDestination
    @Serializable data object BookmarkDetail    : TabDestination
    @Serializable data object BookmarkSubDetail : TabDestination
}

@Serializable
sealed interface DialogDestination : NavKey {
    @Serializable data object HomeSheet     : DialogDestination
    @Serializable data object PraySheet     : DialogDestination
    @Serializable data object QuranSheet    : DialogDestination
    @Serializable data object QiblaSheet    : DialogDestination
    @Serializable data object BookmarkSheet : DialogDestination
}

val appSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RootDestination.Main::class,          RootDestination.Main.serializer())
            subclass(RootDestination.Auth::class,          RootDestination.Auth.serializer())
            subclass(TabDestination.HomeMain::class,          TabDestination.HomeMain.serializer())
            subclass(TabDestination.HomeDetail::class,        TabDestination.HomeDetail.serializer())
            subclass(TabDestination.HomeSubDetail::class,     TabDestination.HomeSubDetail.serializer())
            subclass(TabDestination.PrayMain::class,          TabDestination.PrayMain.serializer())
            subclass(TabDestination.PrayDetail::class,        TabDestination.PrayDetail.serializer())
            subclass(TabDestination.PraySubDetail::class,     TabDestination.PraySubDetail.serializer())
            subclass(TabDestination.QuranMain::class,         TabDestination.QuranMain.serializer())
            subclass(TabDestination.QuranDetail::class,       TabDestination.QuranDetail.serializer())
            subclass(TabDestination.QuranSubDetail::class,    TabDestination.QuranSubDetail.serializer())
            subclass(TabDestination.QiblaMain::class,         TabDestination.QiblaMain.serializer())
            subclass(TabDestination.QiblaDetail::class,       TabDestination.QiblaDetail.serializer())
            subclass(TabDestination.QiblaSubDetail::class,    TabDestination.QiblaSubDetail.serializer())
            subclass(TabDestination.BookmarkMain::class,      TabDestination.BookmarkMain.serializer())
            subclass(TabDestination.BookmarkDetail::class,    TabDestination.BookmarkDetail.serializer())
            subclass(TabDestination.BookmarkSubDetail::class, TabDestination.BookmarkSubDetail.serializer())
            subclass(DialogDestination.HomeSheet::class,      DialogDestination.HomeSheet.serializer())
            subclass(DialogDestination.PraySheet::class,      DialogDestination.PraySheet.serializer())
            subclass(DialogDestination.QuranSheet::class,     DialogDestination.QuranSheet.serializer())
            subclass(DialogDestination.QiblaSheet::class,     DialogDestination.QiblaSheet.serializer())
            subclass(DialogDestination.BookmarkSheet::class,  DialogDestination.BookmarkSheet.serializer())
        }
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/shared/common/navigation/AppDestination.kt
git commit -m "feat(nav): add per-tab destinations and sheet destinations, remove SampleDialog"
```

---

### Task 3: Create shared DemoComponents

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/shared/common/presentation/DemoComponents.kt`

**Step 1:** Create file:

```kotlin
package com.iqbalwork.ramadhancamp.shared.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        content()
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun DemoButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = label)
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/shared/common/presentation/DemoComponents.kt
git commit -m "feat(ui): add DemoSection and DemoButton shared composables"
```

---

### Task 4: Refactor Home feature

All paths under `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/`.

**Files:**
- Modify: `feature/home/presentation/HomeViewModel.kt`
- Modify: `feature/home/presentation/HomeMainScreen.kt`
- Modify: `feature/home/presentation/HomeDetailScreen.kt`
- Create: `feature/home/presentation/HomeSubDetailScreen.kt`
- Create: `feature/home/presentation/HomeSheetScreen.kt`
- Create: `feature/home/HomeNavEntries.kt`
- Delete: `feature/home/presentation/SampleDialogScreen.kt`

**Step 1: Rewrite HomeViewModel.kt**

```kotlin
package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationControllerHolder
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppTab
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.RootDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TextResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navHolder: AppNavigationControllerHolder,
) : ViewModel() {

    companion object {
        const val RESULT_KEY = "home_result"
    }

    private val _lastResult = MutableStateFlow<String?>(null)
    val lastResult: StateFlow<String?> = _lastResult.asStateFlow()

    init {
        viewModelScope.launch {
            navHolder.get().subscribeToResult(RESULT_KEY).collect { result ->
                _lastResult.value = when (result) {
                    is NavigationResult.Success -> "✓ ${(result.value as? TextResult)?.text}"
                    is NavigationResult.Cancel  -> "✗ Cancelled"
                }
            }
        }
    }

    // ─── HomeMain ────────────────────────────────────────────────────────────
    fun navigateToDetail()      = navHolder.get().navigateToInsideTab(TabDestination.HomeDetail)
    fun replaceWithDetail()     = navHolder.get().navigateToInsideTab(TabDestination.HomeDetail, withReplace = true)
    fun navigateToAuth()        = navHolder.get().navigateTo(RootDestination.Auth)
    fun navigateToAuthReplace() = navHolder.get().navigateTo(RootDestination.Auth, withReplace = true)
    fun startNewFlowToAuth()    = navHolder.get().startNewFlow(RootDestination.Auth)
    fun switchToPray()          = navHolder.get().switchTab(AppTab.Pray)
    fun showHomeSheet()         = navHolder.get().showDialog(DialogDestination.HomeSheet)

    // ─── HomeDetail ──────────────────────────────────────────────────────────
    fun navigateToSubDetail() = navHolder.get().navigateToInsideTab(TabDestination.HomeSubDetail)
    fun back()                = navHolder.get().back()
    fun backWithResult()      = navHolder.get().back(
        NavigationResult.Success(RESULT_KEY, TextResult("From HomeDetail"))
    )

    // ─── HomeSubDetail ───────────────────────────────────────────────────────
    fun backToMain() = navHolder.get().backToScreen(TabDestination.HomeMain)
    fun backToMainWithResult() = navHolder.get().backToScreen(
        key = TabDestination.HomeMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From HomeSubDetail")),
    )
}
```

**Step 2: Rewrite HomeMainScreen.kt**

```kotlin
package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeMainScreen(viewModel: HomeViewModel = koinViewModel()) {
    val lastResult by viewModel.lastResult.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        lastResult?.let {
            Text(
                text = "Last result: $it",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        DemoSection("In-Tab") {
            DemoButton("Push Detail")           { viewModel.navigateToDetail() }
            DemoButton("Replace with Detail")   { viewModel.replaceWithDetail() }
        }
        DemoSection("Root") {
            DemoButton("Navigate to Auth (push)")    { viewModel.navigateToAuth() }
            DemoButton("Navigate to Auth (replace)") { viewModel.navigateToAuthReplace() }
            DemoButton("Start New Flow → Auth")      { viewModel.startNewFlowToAuth() }
        }
        DemoSection("Cross-Tab") {
            DemoButton("Switch to Pray") { viewModel.switchToPray() }
        }
        DemoSection("Sheet") {
            DemoButton("Show Home Sheet") { viewModel.showHomeSheet() }
        }
    }
}
```

**Step 3: Rewrite HomeDetailScreen.kt**

```kotlin
package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeDetailScreen(viewModel: HomeViewModel = koinViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Home Detail", style = MaterialTheme.typography.headlineMedium)
        DemoSection("In-Tab") {
            DemoButton("Push SubDetail") { viewModel.navigateToSubDetail() }
        }
        DemoSection("Back") {
            DemoButton("Back")              { viewModel.back() }
            DemoButton("Back with Result")  { viewModel.backWithResult() }
        }
    }
}
```

**Step 4: Create HomeSubDetailScreen.kt**

```kotlin
package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeSubDetailScreen(viewModel: HomeViewModel = koinViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Home SubDetail", style = MaterialTheme.typography.headlineMedium)
        DemoSection("Back to Root") {
            DemoButton("Back to Home Main")              { viewModel.backToMain() }
            DemoButton("Back to Home Main with Result")  { viewModel.backToMainWithResult() }
        }
        DemoSection("Back") {
            DemoButton("Back one step") { viewModel.back() }
        }
    }
}
```

**Step 5: Create HomeSheetScreen.kt**

```kotlin
package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton

@Composable
fun HomeSheetScreen(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Home Sheet", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "showDialog(DialogDestination.HomeSheet). Swipe down or Close → hideDialog().",
            style = MaterialTheme.typography.bodySmall,
        )
        DemoButton("Close (hideDialog)") { onDismiss() }
        Spacer(Modifier.height(8.dp))
    }
}
```

**Step 6: Create HomeNavEntries.kt**

```kotlin
package com.iqbalwork.ramadhancamp.feature.home

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeDetailScreen
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeMainScreen
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeSheetScreen
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeSubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

fun EntryProviderBuilder<NavKey>.homeTabEntries(nav: AppNavigationController) {
    entry<TabDestination.HomeMain>      { HomeMainScreen() }
    entry<TabDestination.HomeDetail>    { HomeDetailScreen() }
    entry<TabDestination.HomeSubDetail> { HomeSubDetailScreen() }
    entry<DialogDestination.HomeSheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        HomeSheetScreen(onDismiss = { nav.hideDialog() })
    }
}
```

**Step 7: Delete SampleDialogScreen.kt**

```bash
git rm composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/home/presentation/SampleDialogScreen.kt
```

**Step 8: Commit**

```bash
git add -A
git commit -m "feat(home): refactor home feature — full nav demo screens + HomeNavEntries"
```

---

### Task 5: Create Pray feature

All paths under `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/pray/`.

**Step 1: Create `di/PrayModule.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray.di

import com.iqbalwork.ramadhancamp.feature.pray.presentation.PrayViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val prayModule = module {
    viewModel { PrayViewModel(get()) }
}
```

**Step 2: Create `presentation/PrayViewModel.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationControllerHolder
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppTab
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TextResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrayViewModel(
    private val navHolder: AppNavigationControllerHolder,
) : ViewModel() {

    companion object { const val RESULT_KEY = "pray_result" }

    private val _lastResult = MutableStateFlow<String?>(null)
    val lastResult: StateFlow<String?> = _lastResult.asStateFlow()

    init {
        viewModelScope.launch {
            navHolder.get().subscribeToResult(RESULT_KEY).collect { result ->
                _lastResult.value = when (result) {
                    is NavigationResult.Success -> "✓ ${(result.value as? TextResult)?.text}"
                    is NavigationResult.Cancel  -> "✗ Cancelled"
                }
            }
        }
    }

    fun navigateToDetail()  = navHolder.get().navigateToInsideTab(TabDestination.PrayDetail)
    fun replaceWithDetail() = navHolder.get().navigateToInsideTab(TabDestination.PrayDetail, withReplace = true)
    fun switchToQuran()     = navHolder.get().switchTab(AppTab.Quran)
    fun showPraySheet()     = navHolder.get().showDialog(DialogDestination.PraySheet)

    fun navigateToSubDetail() = navHolder.get().navigateToInsideTab(TabDestination.PraySubDetail)
    fun back()                = navHolder.get().back()
    fun backWithResult()      = navHolder.get().back(
        NavigationResult.Success(RESULT_KEY, TextResult("From PrayDetail"))
    )

    fun backToMain() = navHolder.get().backToScreen(TabDestination.PrayMain)
    fun backToMainWithResult() = navHolder.get().backToScreen(
        key = TabDestination.PrayMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From PraySubDetail")),
    )
}
```

**Step 3: Create `presentation/PrayMainScreen.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrayMainScreen(viewModel: PrayViewModel = koinViewModel()) {
    val lastResult by viewModel.lastResult.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pray", style = MaterialTheme.typography.headlineMedium)
        lastResult?.let { Text("Last result: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary) }
        DemoSection("In-Tab") {
            DemoButton("Push Detail")          { viewModel.navigateToDetail() }
            DemoButton("Replace with Detail")  { viewModel.replaceWithDetail() }
        }
        DemoSection("Cross-Tab") {
            DemoButton("Switch to Quran") { viewModel.switchToQuran() }
        }
        DemoSection("Sheet") {
            DemoButton("Show Pray Sheet") { viewModel.showPraySheet() }
        }
    }
}
```

**Step 4: Create `presentation/PrayDetailScreen.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrayDetailScreen(viewModel: PrayViewModel = koinViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pray Detail", style = MaterialTheme.typography.headlineMedium)
        DemoSection("In-Tab") {
            DemoButton("Push SubDetail") { viewModel.navigateToSubDetail() }
        }
        DemoSection("Back") {
            DemoButton("Back")             { viewModel.back() }
            DemoButton("Back with Result") { viewModel.backWithResult() }
        }
    }
}
```

**Step 5: Create `presentation/PraySubDetailScreen.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PraySubDetailScreen(viewModel: PrayViewModel = koinViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pray SubDetail", style = MaterialTheme.typography.headlineMedium)
        DemoSection("Back to Root") {
            DemoButton("Back to Pray Main")             { viewModel.backToMain() }
            DemoButton("Back to Pray Main with Result") { viewModel.backToMainWithResult() }
        }
        DemoSection("Back") {
            DemoButton("Back one step") { viewModel.back() }
        }
    }
}
```

**Step 6: Create `presentation/PraySheetScreen.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton

@Composable
fun PraySheetScreen(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pray Sheet", style = MaterialTheme.typography.titleMedium)
        Text("showDialog(DialogDestination.PraySheet)", style = MaterialTheme.typography.bodySmall)
        DemoButton("Close (hideDialog)") { onDismiss() }
        Spacer(Modifier.height(8.dp))
    }
}
```

**Step 7: Create `PrayNavEntries.kt`**

```kotlin
package com.iqbalwork.ramadhancamp.feature.pray

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.iqbalwork.ramadhancamp.feature.pray.presentation.PrayDetailScreen
import com.iqbalwork.ramadhancamp.feature.pray.presentation.PrayMainScreen
import com.iqbalwork.ramadhancamp.feature.pray.presentation.PraySheetScreen
import com.iqbalwork.ramadhancamp.feature.pray.presentation.PraySubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

fun EntryProviderBuilder<NavKey>.prayTabEntries(nav: AppNavigationController) {
    entry<TabDestination.PrayMain>      { PrayMainScreen() }
    entry<TabDestination.PrayDetail>    { PrayDetailScreen() }
    entry<TabDestination.PraySubDetail> { PraySubDetailScreen() }
    entry<DialogDestination.PraySheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        PraySheetScreen(onDismiss = { nav.hideDialog() })
    }
}
```

**Step 8: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/pray/
git commit -m "feat(pray): add pray feature with nav demo screens and PrayNavEntries"
```

---

### Tasks 6–8: Quran, Qibla, Bookmark features

Apply the exact Pray pattern for each tab. Substitution table:

| Token | Quran | Qibla | Bookmark |
|-------|-------|-------|----------|
| Package `pray` | `quran` | `qibla` | `bookmark` |
| Class prefix `Pray` | `Quran` | `Qibla` | `Bookmark` |
| `PrayMain/Detail/SubDetail` | `Quran…` | `Qibla…` | `Bookmark…` |
| `PraySheet` | `QuranSheet` | `QiblaSheet` | `BookmarkSheet` |
| `RESULT_KEY` | `"quran_result"` | `"qibla_result"` | `"bookmark_result"` |
| `switchTo…()` method | `switchToQibla()` → `AppTab.Qibla` | `switchToBookmark()` → `AppTab.Bookmark` | `switchToHome()` → `AppTab.Home` |
| Switch button label | `"Switch to Qibla"` | `"Switch to Bookmark"` | `"Switch to Home"` |
| Result texts | `"From QuranDetail"` / `"From QuranSubDetail"` | `"From QiblaDetail"` / `"From QiblaSubDetail"` | `"From BookmarkDetail"` / `"From BookmarkSubDetail"` |

Files to create per tab (7 files, same structure as Pray):
```
feature/[tab]/di/[Tab]Module.kt
feature/[tab]/presentation/[Tab]ViewModel.kt
feature/[tab]/presentation/[Tab]MainScreen.kt
feature/[tab]/presentation/[Tab]DetailScreen.kt
feature/[tab]/presentation/[Tab]SubDetailScreen.kt
feature/[tab]/presentation/[Tab]SheetScreen.kt
feature/[tab]/[Tab]NavEntries.kt
```

Commit after each tab:
```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/[tab]/
git commit -m "feat([tab]): add [tab] feature with nav demo screens and [Tab]NavEntries"
```

---

### Task 9: Create AuthScreen

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/auth/presentation/AuthScreen.kt`

No ViewModel — injects `AppNavigationControllerHolder` directly via `koinInject()`.

```kotlin
package com.iqbalwork.ramadhancamp.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationControllerHolder
import com.iqbalwork.ramadhancamp.shared.common.navigation.RootDestination
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoButton
import com.iqbalwork.ramadhancamp.shared.common.presentation.DemoSection
import org.koin.compose.koinInject

@Composable
fun AuthScreen() {
    val navHolder: AppNavigationControllerHolder = koinInject()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Auth", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Arrived via navigateTo, navigateTo(replace), or startNewFlow from HomeMain.",
            style = MaterialTheme.typography.bodySmall,
        )
        DemoSection("Root Navigation") {
            DemoButton("Navigate to Main (push)") {
                navHolder.get().navigateTo(RootDestination.Main())
            }
            DemoButton("Navigate to Main (replace)") {
                navHolder.get().navigateTo(RootDestination.Main(), withReplace = true)
            }
            DemoButton("Start New Flow → Main") {
                navHolder.get().startNewFlow(RootDestination.Main())
            }
        }
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/auth/
git commit -m "feat(auth): add AuthScreen demoing root return navigation"
```

---

### Task 10: Update App.kt — wire AuthScreen

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/App.kt`

**Step 1:** Add import:
```kotlin
import com.iqbalwork.ramadhancamp.feature.auth.presentation.AuthScreen
```

**Step 2:** Replace the placeholder:
```kotlin
// before
entry<RootDestination.Auth> { /* AuthScreen() — placeholder */ }

// after
entry<RootDestination.Auth> { AuthScreen() }
```

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/App.kt
git commit -m "feat(app): wire AuthScreen to RootDestination.Auth entry"
```

---

### Task 11: Refactor MainScreen.kt — dispatch to extension functions

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/main/MainScreen.kt`

**Step 1:** Add imports for all five NavEntries files:
```kotlin
import com.iqbalwork.ramadhancamp.feature.home.homeTabEntries
import com.iqbalwork.ramadhancamp.feature.pray.prayTabEntries
import com.iqbalwork.ramadhancamp.feature.quran.quranTabEntries
import com.iqbalwork.ramadhancamp.feature.qibla.qiblaTabEntries
import com.iqbalwork.ramadhancamp.feature.bookmark.bookmarkTabEntries
```

**Step 2:** Replace the `entryProvider { when(tab) { ... } }` block:

```kotlin
entryProvider = entryProvider {
    when (tab) {
        AppTab.Home     -> homeTabEntries(navController)
        AppTab.Pray     -> prayTabEntries(navController)
        AppTab.Quran    -> quranTabEntries(navController)
        AppTab.Qibla    -> qiblaTabEntries(navController)
        AppTab.Bookmark -> bookmarkTabEntries(navController)
    }
},
```

**Step 3:** Delete the `PlaceholderTabScreen` private function at the bottom of the file — no longer used.

**Step 4:** Remove now-unused imports:
- `HomeMainScreen`, `HomeDetailScreen`, `SampleDialogScreen`
- `DialogDestination`, `TabDestination`
- `Alignment`

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/feature/main/MainScreen.kt
git commit -m "refactor(main): dispatch entryProvider to per-tab extension functions"
```

---

### Task 12: Update initKoin.kt

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/initKoin.kt`

**Step 1:** Replace entire file:

```kotlin
package com.iqbalwork.ramadhancamp

import com.iqbalwork.ramadhancamp.feature.bookmark.di.bookmarkModule
import com.iqbalwork.ramadhancamp.feature.home.di.homeModule
import com.iqbalwork.ramadhancamp.feature.pray.di.prayModule
import com.iqbalwork.ramadhancamp.feature.qibla.di.qiblaModule
import com.iqbalwork.ramadhancamp.feature.quran.di.quranModule
import com.iqbalwork.ramadhancamp.shared.common.navigation.di.navigationModule
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException

fun initKoin() {
    startKoin {
        modules(navigationModule, homeModule, prayModule, quranModule, qiblaModule, bookmarkModule)
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/iqbalwork/ramadhancamp/initKoin.kt
git commit -m "feat(di): register pray, quran, qibla, bookmark Koin modules"
```

---

### Verification checklist

After all tasks complete, build and run. Verify:

- [ ] Home tab: three Root buttons navigate to AuthScreen; from Auth, all three return variants work
- [ ] Every tab: Push Detail → Push SubDetail; SubDetail shows Back to Main + Back to Main with Result
- [ ] Every tab Main: result text appears after returning from SubDetail with result
- [ ] Every tab: Replace with Detail replaces (no back in Detail to previous Main entry)
- [ ] Every tab: sheet opens via Show Sheet button; closes via Close button and swipe-down
- [ ] NavigationBar switch buttons cycle: Home→Pray→Quran→Qibla→Bookmark→Home
- [ ] Tab state preserved on switch (scroll position maintained via Box+requiredSize pattern)
