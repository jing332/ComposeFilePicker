package com.github.jing332.filepicker

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.github.jing332.filepicker.utils.FilePickerConfig

val LocalNavController = compositionLocalOf<NavHostController> { error("No nav controller") }
val LocalFilePickerConfig = compositionLocalOf<FilePickerConfig> { error("No config") }

object Contants {
    const val ROUTE_PAGE = "page"
    const val ARG_URI = "uri"
    const val DEFAULT_ROOT_PATH = "/storage/emulated/0"

}