package com.github.jing332.filepicker

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> { error("No nav controller") }

object Contants {
    const val ROUTE_PAGE = "page"
    const val ARG_URI = "uri"
    const val DEFAULT_ROOT_URI = "/storage/emulated/0"

}