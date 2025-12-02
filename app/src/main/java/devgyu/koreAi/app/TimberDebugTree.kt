package devgyu.koreAi.app

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import devgyu.koreAi.domain.AppBuildConfig
import timber.log.Timber

class TimberTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "${element.fileName}:${element.lineNumber}#${element.methodName}"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        Firebase.crashlytics.log(message)

        if (!AppBuildConfig.DEBUG && t != null && priority == Log.ERROR) {
            Firebase.crashlytics.recordException(t)
        }
    }
}