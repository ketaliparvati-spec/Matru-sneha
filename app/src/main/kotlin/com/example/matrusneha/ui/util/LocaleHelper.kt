package com.example.matrusneha.ui.util

import android.content.Context
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, languageTag: String) {
        val locale = Locale(languageTag)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
