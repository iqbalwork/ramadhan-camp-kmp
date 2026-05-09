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
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Ayat
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.AudioPlayerBar
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.AyatCard
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.AyatOptionsSheet
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailState
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.time.Clock
import org.koin.core.parameter.parametersOf
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.ui.audio.AudioPlayer
import chaintech.videoplayer.host.MediaPlayerEvent
import com.iqbalwork.ramadhancamp.shared.common.ui.components.loading.Loader
import com.iqbalwork.ramadhancamp.shared.common.ui.components.snackbar.RamadhanSnackBarHost

@Composable
fun QuranDetailScreen(params: QuranDetailScreenParameters) {

    val viewModel: QuranDetailViewModel = rememberViewModel { parametersOf(params) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val action = viewModel.rememberDispatch()

    RamadhanSnackBarHost(
        modifier = Modifier.fillMaxSize(),
        snackbarFlow = viewModel.snackBarEvents
    ) {
        QuranDetailContent(state = state, action = action, scrollToAyat = params.scrollToAyat)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranDetailContent(state: QuranDetailState, action: (QuranDetailEvent) -> Unit, scrollToAyat: Int? = null) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography
    val listState = rememberLazyListState()

    var isPlaying by remember { mutableStateOf(false) }
    var currentTimeMs by remember { mutableStateOf(0f) }
    var totalTimeMs by remember { mutableStateOf(0f) }

    var smoothProgressMs by remember { mutableFloatStateOf(0f) }
    var isBuffering by remember { mutableStateOf(false) }

    val mediaPlayerHost = remember {
        MediaPlayerHost(mediaUrl = "", autoPlay = false, isLooping = false)
    }
    val preBufferHost = remember {
        MediaPlayerHost(mediaUrl = "", autoPlay = false, isLooping = false)
    }

    // FIRST: Set onEvent before anything else starts playback
    LaunchedEffect(mediaPlayerHost) {
        mediaPlayerHost.onEvent = { event ->
            when (event) {
                is MediaPlayerEvent.PauseChange -> {
                    isPlaying = !event.isPaused
                }
                is MediaPlayerEvent.CurrentTimeChange -> {
                    currentTimeMs = event.currentTime * 1000f  // Convert seconds to milliseconds
                }
                is MediaPlayerEvent.TotalTimeChange -> {
                    totalTimeMs = event.totalTime * 1000f  // Convert seconds to milliseconds
                }
                is MediaPlayerEvent.BufferChange -> {
                    isBuffering = event.isBuffering
                }
                is MediaPlayerEvent.MediaEnd -> {
                    mediaPlayerHost.pause()
                    action(QuranDetailEvent.StopAudio)
                }
                else -> { }
            }
        }
        // ADD THIS:
        mediaPlayerHost.onError = { error ->
            action(QuranDetailEvent.AudioError(error.message))
        }
    }

    // SECOND: Load audio only after onEvent is set
    LaunchedEffect(state.playingAyat) {
        if (state.playingAyat != null) {
            // Reset ticker on ayat change
            smoothProgressMs = 0f

            mediaPlayerHost.pause()
            yield()
            mediaPlayerHost.loadUrl(state.playingAyat.audioUrl)
            delay(100)
            mediaPlayerHost.play()
        } else {
            mediaPlayerHost.pause()
        }
    }

    // Pre-load next ayat audio to warm the network cache (no host swap)
    LaunchedEffect(state.nextAyatAudioUrl) {
        val nextUrl = state.nextAyatAudioUrl
        if (nextUrl != null) {
            preBufferHost.loadUrl(nextUrl)
            preBufferHost.pause()
        }
    }
    // Scroll to specific ayat when navigated from search results
    val scrollTarget = scrollToAyat
    LaunchedEffect(scrollTarget, state.surahDetail) {
        if (scrollTarget != null && state.surahDetail != null) {
            val index = state.surahDetail!!.ayat.indexOfFirst { it.nomorAyat == scrollTarget }
            if (index >= 0) {
                delay(300) // wait for layout
                listState.animateScrollToItem(index)
            }
        }
    }

    // Fully independent progress ticker at ~60fps
    // Starts from current smoothProgressMs, advances by real elapsed time
    LaunchedEffect(isPlaying, isBuffering) {
        if (!isPlaying || isBuffering) return@LaunchedEffect
        val startWallClock = Clock.System.now().toEpochMilliseconds()
        val startProgress = smoothProgressMs
        while (true) {
            delay(16L)
            val elapsed = Clock.System.now().toEpochMilliseconds() - startWallClock
            val rawProgress = startProgress + elapsed
            smoothProgressMs = if (totalTimeMs > 0f) rawProgress.coerceAtMost(totalTimeMs) else rawProgress
        }
    }

    Box(modifier = Modifier.size(0.dp)) {
        AudioPlayer(playerHost = mediaPlayerHost)
    }

    DisposableEffect(mediaPlayerHost) {
        onDispose {
            mediaPlayerHost.pause()
        }
    }

    DisposableEffect(preBufferHost) {
        onDispose {
            preBufferHost.pause()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.surahDetail?.namaLatin ?: "Loading...",
                        style = typography.headlineSmall,
                        color = colors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { action(QuranDetailEvent.Back) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.bgPrimary
                )
            )
        },
        containerColor = colors.bgPrimary
    ) { innerPadding ->
        if (state.isLoading) {
            Loader(modifier = Modifier.fillMaxSize())
        } else if (state.isError) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Gagal memuat surah",
                    style = typography.bodyLarge,
                    color = colors.textPrimary
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = if (state.playingAyat != null) 160.dp else 16.dp)
                ) {
                    val ayatList = state.surahDetail?.ayat ?: emptyList()
                    itemsIndexed(ayatList) { index, ayat ->
                        AyatCard(
                            ayat = ayat,

                            onOptionsClick = { action(QuranDetailEvent.OnAyatClicked(ayat)) }
                        )
                    }
                }

                if (state.selectedAyatForOptions != null) {
                    AyatOptionsSheet(
                        ayat = state.selectedAyatForOptions,
                        onDismissRequest = { action(QuranDetailEvent.OnCloseOptionsSheet) },
                        onPlayAudio = {
                            action(QuranDetailEvent.PlayAudio(state.selectedAyatForOptions))
                            action(QuranDetailEvent.OnCloseOptionsSheet)
                        },
                        onBookmark = { action(QuranDetailEvent.OnBookmarkClicked(state.selectedAyatForOptions)) },
                        onShare = { action(QuranDetailEvent.OnShareClicked(state.selectedAyatForOptions)) },
                        onCopy = { action(QuranDetailEvent.OnCopyClicked(state.selectedAyatForOptions)) }
                    )
                }

                if (state.playingAyat != null) {
                    AudioPlayerBar(
                        surahName = state.surahDetail?.namaLatin ?: "",
                        reciterName = "Misyari Rasyid",
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                        elapsedMs = smoothProgressMs.toLong(),
                        totalDurationMs = totalTimeMs.toLong(),
                        onPlayPause = {
                            if (isPlaying) {
                                mediaPlayerHost.pause()
                            } else {
                                mediaPlayerHost.play()
                            }
                        },
                        onNext = { action(QuranDetailEvent.PlayNextAyat) },
                        onPrev = { action(QuranDetailEvent.PlayPrevAyat) },
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}






