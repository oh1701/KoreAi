package devgyu.koreAi.presentation.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import devgyu.koreAi.domain.AppBuildConfig
import devgyu.koreAi.presentation.GoogleAdsUtils
import devgyu.koreAi.presentation.designsystem.GyuDefaultTheme
import devgyu.koreAi.presentation.viewmodel.base.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.checkNetworkStatus()
        viewModel.fetchAdID()

        checkAppUpdate()
        GoogleAdsUtils.loadAds(this)

        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            GyuDefaultTheme {
                CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState){
                    NavigatorScreen(navController)
                }
            }
        }
    }

    private fun checkAppUpdate(){
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val isPlayStoreUpdatable =
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        AppBuildConfig.VERSION_CODE < appUpdateInfo.availableVersionCode()

            if(isPlayStoreUpdatable){
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo,
                    this@MainActivity,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}