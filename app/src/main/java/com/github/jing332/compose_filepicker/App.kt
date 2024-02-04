package com.github.jing332.compose_filepicker

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class App : Application() {
    @Suppress("DEPRECATION")
    private fun setLocale(context: Context, locale: Locale) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
//        val newLocale = getLocaleFromFile(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.setLocale(locale)
        }
        Locale.setDefault(locale)

        resources.updateConfiguration(configuration, metrics)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base.apply {
//            setLocale(this, Locale("en"))
//        }
//        )
//    }
}