package devgyu.koreAi.app

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp
import devgyu.koreAi.domain.AppBuildConfig
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)

        with(AppBuildConfig) {
            DEBUG = BuildConfig.DEBUG
            VERSION_CODE = BuildConfig.VERSION_CODE
            VERSION_NAME = BuildConfig.VERSION_NAME
            DEVICE_MODEL = android.os.Build.MODEL
            OS_VERSION = android.os.Build.VERSION.SDK_INT
        }

        Timber.plant(TimberTree())
        MobileAds.initialize(this)
    }
}