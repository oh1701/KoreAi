package devgyu.koreAi.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import devgyu.koreAi.domain.entity.FluxImageRes

object IntentUtil {
    fun sendReportImageData(context: Context, fluxImageRes: FluxImageRes){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("rbtjd2290@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Report KoreAI Image")
            putExtra(Intent.EXTRA_TEXT, "prompt = ${fluxImageRes.prompt}\n\nseed =${fluxImageRes.seed}")
        }

        context.startActivity(intent)
    }
}