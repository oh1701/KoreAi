package devgyu.koreAi.presentation

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.NonRestartableComposable
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

object GoogleAdsUtils {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    fun loadAds(context: Context) = RewardedInterstitialAd.load(
        context,
        BuildConfig.AD_UNIT_ID,
        AdRequest.Builder().build(),
        object : RewardedInterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                rewardedInterstitialAd = null
            }

            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                super.onAdLoaded(ad)
                rewardedInterstitialAd = ad
            }
        }
    )

    /**
     * 구글 광고 띄우는 함수
     * @param callBack 파라미터가 false 라면 광고 없음, true 라면 광고 있음
     * */
    @NonRestartableComposable
    fun showAds(
        context: Context,
        callback: (Boolean) -> Unit
    ){
        when {
            rewardedInterstitialAd != null ->
                rewardedInterstitialAd?.show(context as Activity) { callback(true) }
            else -> callback(false)
        }
    }
}