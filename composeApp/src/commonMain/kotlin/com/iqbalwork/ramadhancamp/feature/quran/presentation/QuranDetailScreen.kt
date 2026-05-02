package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Ayat
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.AyatCard
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailState
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import org.koin.core.parameter.parametersOf

@Composable
fun QuranDetailScreen(parameters: QuranDetailScreenParameters) {
    val viewModel: QuranDetailViewModel = rememberViewModel(parameters = { parametersOf(parameters) })
    val state by viewModel.state.collectAsStateWithLifecycle()
    QuranDetailContent(
        state = state,
        parameters = parameters,
        action = viewModel.rememberDispatch()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuranDetailContent(
    state: QuranDetailState,
    parameters: QuranDetailScreenParameters,
    action: (QuranDetailEvent) -> Unit
) {
    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current
    var selectedAyat by remember { mutableStateOf<Ayat?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(state.surahDetail, parameters.scrollToAyat) {
        if (state.surahDetail != null && parameters.scrollToAyat != null) {
            val index = parameters.scrollToAyat - 1
            if (index in state.surahDetail.ayat.indices) {
                listState.animateScrollToItem(index)
            }
        }
    }

    Scaffold(
        containerColor = RamadhanTheme.colors.bgPrimary,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = state.surahDetail?.namaLatin ?: "Loading...",
                            style = RamadhanTheme.typography.headlineSmall,
                            color = RamadhanTheme.colors.textPrimary
                        )
                        state.surahDetail?.let {
                            Text(
                                text = "${state.surahDetail.ayat.size} Ayat",
                                style = RamadhanTheme.typography.labelSmall,
                                color = RamadhanTheme.colors.textSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { action(QuranDetailEvent.Back) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = RamadhanTheme.colors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RamadhanTheme.colors.bgPrimary)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(bottom = innerPadding.calculateBottomPadding()))
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (state.isLoading) {
                // You can place a Loader here
            } else {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    state.surahDetail?.ayat?.let { ayatList ->
                        itemsIndexed(ayatList, key = { _, a -> a.nomorAyat }) { index, ayat ->
                            AyatCard(
                                ayat = ayat,
                                onOptionsClick = {
                                    selectedAyat = ayat
                                    showBottomSheet = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showBottomSheet && selectedAyat != null) {
        val ayat = selectedAyat!!
        val isPlaying = state.currentlyPlayingAudioUrl == ayat.audioUrl
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = RamadhanTheme.colors.bgSecondary
        ) {
            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                TextButton(
                    onClick = {
                        if (isPlaying) action(QuranDetailEvent.StopAudio) else action(QuranDetailEvent.PlayAudio(ayat.audioUrl))
                        showBottomSheet = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(if (isPlaying) "Hentikan Audio" else "Putar Audio", color = RamadhanTheme.colors.textPrimary)
                }
                TextButton(
                    onClick = {
                        val text = "\n\n\n\nBuka di aplikasi: ramadhancamp://quran//${parameters.surahId}"
                        action(QuranDetailEvent.ShareAyat(text))
                        showBottomSheet = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("Bagikan", color = RamadhanTheme.colors.textPrimary)
                }
                TextButton(
                    onClick = {
                        val text = "${ayat.teksArab}\n${ayat.teksLatin}\n${ayat.teksIndonesia}"
                        clipboardManager.setText(AnnotatedString(text))
                        showBottomSheet = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("Salin", color = RamadhanTheme.colors.textPrimary)
                }
            }
        }
    }
}
