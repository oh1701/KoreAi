package devgyu.koreAi.presentation.model.state

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


sealed class NetworkStatus {
    data object Available : NetworkStatus()
    data object Unavailable : NetworkStatus()
}

class NetworkStatusTracker(coroutineScope: CoroutineScope, context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkStatus = callbackFlow {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable)
            }

            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkStatusCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }
        .distinctUntilChanged() // 중복된 네트워크 상태 호출은 제거한다.
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    companion object {
        /** 기존 MainViewModel -> API 별로 네트워크 상태를 확인해야하기 때문에 Singleton 처리하여 공용 처리 하였음
         * @see true -> 네트워크 미연결 상태
         * @see false -> 네트워크 연결 상태
         *  */
        val networkConnected = MutableStateFlow<Boolean?>(null)
    }
}

inline fun <Result> Flow<NetworkStatus?>.statusMap(
    crossinline onUnavailable: suspend () -> Result,
    crossinline onAvailable: suspend () -> Result,
): Flow<Result?> = map { status ->
    when (status) {
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
        else -> null
    }
}