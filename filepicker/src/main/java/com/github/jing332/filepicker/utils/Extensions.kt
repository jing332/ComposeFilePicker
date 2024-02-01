package com.github.jing332.filepicker.utils

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun View.performLongPress() {
    this.isHapticFeedbackEnabled = true
    this.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
}

@SuppressLint("RestrictedApi")
fun NavController.navigate(
    route: String,
    argsBuilder: Bundle.() -> Unit = {},
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    navigate(route, Bundle().apply(argsBuilder), navOptions, navigatorExtras)
}

/*
* 可传递 Bundle 到 Navigation
* */
@SuppressLint("RestrictedApi")
fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        navigate(id, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}