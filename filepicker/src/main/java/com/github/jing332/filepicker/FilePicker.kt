package com.github.jing332.filepicker

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.jing332.filepicker.Contants.ARG_URI
import com.github.jing332.filepicker.Contants.ROUTE_PAGE
import com.github.jing332.filepicker.model.NormalFile
import com.github.jing332.filepicker.utils.navigate
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File

private const val TAG = "FilePicker"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionGrant() {
    val readExtPermission =
        rememberMultiplePermissionsState(
            mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // A13
                    add(Manifest.permission.READ_MEDIA_AUDIO)
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                    add(Manifest.permission.READ_MEDIA_VIDEO)
                }
            }
        )

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        if (!readExtPermission.allPermissionsGranted)
            readExtPermission.launchMultiplePermissionRequest()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {// A11
            if (!Environment.isExternalStorageManager()) {
                kotlin.runCatching {
                    context.startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.parse("package:${context.packageName}")
                    })
                }.onFailure {
                    Log.e(TAG, "ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION", it)
                }
            }
        }
    }
}


@Composable
fun FilePicker(modifier: Modifier = Modifier, config: FilePickerConfig = FilePickerConfig()) {
    val vm: FilePickerViewModel = viewModel()
    val navController = rememberNavController()
    val navBarItems = remember { mutableStateListOf<NavBarItem>() }

    fun popBack() {
        navBarItems.remove(navBarItems.last())
        navController.popBackStack()
    }

    fun update() {
        navController.popBackStack()
        navController.navigate(ROUTE_PAGE, Bundle().apply {
            putString(ARG_URI, config.rootPath)
        })
    }

    PermissionGrant()

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalFilePickerConfig provides config,
    ) {
        var selectedCount by remember { mutableIntStateOf(0) }
        Column(modifier) {
            var sortConfig by remember { mutableStateOf(config.sortConfig) }
            FilePickerToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = navBarItems.lastOrNull()?.name ?: "",
                sortConfig = sortConfig,
                onSortConfigChange = {
                    sortConfig = it
                    config.sortConfig = it
                    update()
                },
                selectedCount = selectedCount,
                onCancelSelect = { selectedCount = 0},
                onConfirmSelect = {  }
            )

            FileNavBar(
                list = navBarItems,
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = { item ->
                    while (true) {
                        val uri =
                            navController.currentBackStackEntry?.arguments?.getString(ARG_URI)
                                ?: break
                        if (uri == item.path) break
                        else popBack()
                    }
                })

            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = ROUTE_PAGE
            ) {
                composable(ROUTE_PAGE) { entry ->
                    LaunchedEffect(key1 = Unit) {

                    }

                    navController.enableOnBackPressed(false)
                    val uri = entry.arguments?.getString(ARG_URI) ?: config.rootPath
                    BackHandler(uri != config.rootPath) {
                        popBack()
                    }

                    val file = File(uri)
                    if (navBarItems.isEmpty())
                        navBarItems.add(NavBarItem(name = file.name, path = file.path))

                    FileListPage(
                        file = NormalFile(file),
                        onBack = { popBack() },
                        onEnter = { enterFile ->
                            navBarItems += NavBarItem(name = enterFile.name, path = enterFile.path)
                            navController.navigate(ROUTE_PAGE, Bundle().apply {
                                putString(ARG_URI, enterFile.path)
                            })
                        },
                        selectedCount = selectedCount,
                        onSelectedCountChange = { selectedCount = it }
                    )

                }
            }
        }
    }
}