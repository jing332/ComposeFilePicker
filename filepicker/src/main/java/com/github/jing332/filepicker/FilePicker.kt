package com.github.jing332.filepicker

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.jing332.filepicker.Contants.ARG_PATH
import com.github.jing332.filepicker.Contants.ROUTE_PAGE
import com.github.jing332.filepicker.listpage.FileListPage
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.model.NormalFile
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
fun FilePicker(
    modifier: Modifier = Modifier,
    initialPath: String = Environment.getExternalStorageDirectory().path,
    state: FilePickerState = rememberNavController().run {
        remember { FilePickerState(initialPath, this) }
    },
    config: FilePickerConfiguration = FilePickerConfiguration(),
    onConfirmSelect: (List<IFileModel>) -> Unit
) {
    val navController = state.navController
    val navBarItems = remember { mutableStateListOf<NavBarItem>() }

    fun popBack() {
        navBarItems.remove(navBarItems.last())
        navController.popBackStack()
    }

    PermissionGrant()

    Column(modifier) {
        val selectedCount = state.currentListState?.items?.count { it.isChecked.value } ?: 0
        FilePickerToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = navBarItems.lastOrNull()?.name ?: "",
            sortConfig = config.sortConfig,
            onSortConfigChange = {
                config.sortConfig = it
            },
            viewType = config.viewType,
            onSwitchViewType = {
                config.viewType = it
            },
            selectedCount = selectedCount,
            onCancelSelect = {
                state.currentListState?.uncheckAll()
            },
            onConfirmSelect = {
                onConfirmSelect(state.currentListState?.items?.filter { it.isChecked.value }
                    ?.map { it.model } ?: emptyList())
            }
        )

        FileNavBar(
            list = navBarItems,
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { item ->
                while (true) {
                    val uri =
                        navController.currentBackStackEntry?.arguments?.getString(ARG_PATH)
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
                navController.enableOnBackPressed(false)
                val path = entry.arguments?.getString(ARG_PATH) ?: initialPath
                val fileListState = state.getListState(path)

                LaunchedEffect(key1 = Unit) {
                    state.currentPath = path
                }
                BackHandler(path != initialPath) {
                    popBack()
                }
                BackHandler(selectedCount > 0) {
                    state.currentListState?.uncheckAll()
                }

                val file = File(path)
                if (navBarItems.isEmpty())
                    navBarItems.add(NavBarItem(name = file.name, path = file.path))
                FileListPage(
                    file = NormalFile(file),
                    state = fileListState,
                    config = config,
                    onBack = { popBack() },
                    onEnter = { enterFile ->
                        navBarItems += NavBarItem(name = enterFile.name, path = enterFile.path)
                        state.navigate(enterFile.path)
                    },

                    )

            }
        }
    }
}