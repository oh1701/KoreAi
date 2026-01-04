package devgyu.koreAi.presentation.viewmodel.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import dagger.hilt.android.lifecycle.HiltViewModel
import devgyu.koreAi.core.ioDispatcher
import devgyu.koreAi.domain.usecase.FetchFluxUserDataUseCase
import devgyu.koreAi.presentation.model.state.NetworkStatusTracker
import devgyu.koreAi.presentation.model.state.NetworkStatusTracker.Companion.networkConnected
import devgyu.koreAi.presentation.model.state.statusMap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val fetchFluxUserDataUseCase: FetchFluxUserDataUseCase
): BaseAndroidViewModel(application){
    /**
     * adId.id 를 파이어베이스로 전송
     *
     * true - 수집 거절
     * false - 수집 허용
     * */
    fun fetchAdID(){
        viewModelScope.launch(ioDispatcher) {
            val adId = AdvertisingIdClient.getAdvertisingIdInfo(application)

            if (!adId.isLimitAdTrackingEnabled) {
                fetchFluxUserDataUseCase(adId.id ?: "")
            }
        }
    }

    fun checkNetworkStatus() {
        viewModelScope.launch(ioDispatcher) {
            val connectivityManager =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            NetworkStatusTracker(this, application)
                .networkStatus
                .statusMap(
                    onAvailable = { networkConnected.update { true } },
                    onUnavailable = { networkConnected.update { false } }
                ).collectLatest {
                    if (it == null) {
                        networkConnected.update { networkCapabilities != null }
                    }
                }
        }
    }
}