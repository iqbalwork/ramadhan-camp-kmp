package com.iqbalwork.ramadhancamp.feature.qibla.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.qibla.domain.repository.QiblaRepository
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.model.QiblaEffect
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.model.QiblaEvent
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.model.QiblaState
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import com.iqbalwork.ramadhancamp.shared.common.utils.goToDeviceSettings
import com.iqbalwork.ramadhancamp.shared.common.utils.isLocationPermissionGranted
import dev.jordond.compass.geolocation.GeolocatorResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QiblaViewModel(
    navController: NavigationManager,
    params: QiblaScreenParameters,
    private val qiblaRepository: QiblaRepository
) : BaseViewModel<QiblaScreenParameters, QiblaState, QiblaEvent, QiblaEffect>(
    params = params,
    initialState = QiblaState(
        isCompassAvailable = qiblaRepository.isCompassAvailable
    ),
    navigationManager = navController,
    resultKeys = arrayOf(),
) {

    init {
        qiblaRepository.startCompass()
        
        qiblaRepository.compassHeading.onEach { heading ->
            updateState { copy(currentHeading = heading) }
        }.launchIn(viewModelScope)
        
        fetchQiblaLocation()
    }
    
    private fun fetchQiblaLocation() {
        updateState { copy(isLoading = true, hasLocationPermission = true) }
        viewModelScope.launch {
            qiblaRepository.getQiblaLocation().fold(
                onSuccess = { location ->
                    updateState {
                        copy(
                            isLoading = false,
                            cityName = location.cityName,
                            bearingToKaaba = location.bearingToKaaba,
                            distanceKm = location.distanceToKaabaKm
                        )
                    }
                },
                onFailure = {
                    updateState { 
                        copy(
                            isLoading = false,
                            hasLocationPermission = false 
                        ) 
                    }
                }
            )
        }
    }

    private fun checkLocationPermission(): Boolean {
        return isLocationPermissionGranted()
    }

    override fun handleEvent(event: QiblaEvent) {
        when (event) {
            is QiblaEvent.RequestLocation -> {
                if (!state.value.hasLocationPermission) {
                    viewModelScope.launch {
                        updateState { copy(isLoading = true) }
                        val isGranted = checkLocationPermission()
                        updateState { copy(isLoading = false) }
                        if (!isGranted) return@launch
                        fetchQiblaLocation()
                    }
                } else {
                    fetchQiblaLocation()
                }
            }
            is QiblaEvent.GoToSettings -> {
                goToDeviceSettings()
            }
        }
    }

    override fun onCleared() {
        qiblaRepository.stopCompass()
        super.onCleared()
    }
}
