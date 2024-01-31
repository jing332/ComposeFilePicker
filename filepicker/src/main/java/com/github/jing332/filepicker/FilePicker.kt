package com.github.jing332.filepicker

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.jing332.filepicker.Contants.ARG_URI
import com.github.jing332.filepicker.Contants.DEFAULT_ROOT_URI
import com.github.jing332.filepicker.Contants.ROUTE_PAGE
import com.github.jing332.filepicker.model.NormalFile
import com.github.jing332.filepicker.utils.Extensions.navigate
import java.io.File

private const val TAG = "FilePicker"


@Composable
fun FilePicker(modifier: Modifier = Modifier, state: FilePickerState) {
    val vm: FilePickerViewModel = viewModel()
    val navController = rememberNavController()
    val navBarItems = remember { mutableStateListOf<NavBarItem>() }
    fun popBack() {
        navBarItems.removeLast()
        navController.popBackStack()
    }

    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {
        Column {
            FileNavBar(list = navBarItems, modifier = Modifier.padding(8.dp), onClick = { item ->
                while (true) {
                    println(navController.currentBackStackEntry)
                    val uri =
                        navController.currentBackStackEntry?.arguments?.getString(ARG_URI) ?: break
                    if (uri == item.path) break
                    else popBack()
                }
            })

            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = ROUTE_PAGE
            ) {
                composable(ROUTE_PAGE) {
                    navController.enableOnBackPressed(false)
                    BackHandler(true) {
                        popBack()
                    }

                    val uri = it.arguments?.getString(ARG_URI) ?: DEFAULT_ROOT_URI
                    val file = File(uri)
                    if (navBarItems.isEmpty())
                        navBarItems += NavBarItem(name = file.name, path = file.path)

                    FileListPage(
                        file = NormalFile(file),
                        onBack = { popBack() },
                        onEnter = { enterFile ->
                            navBarItems += NavBarItem(name = enterFile.name, path = enterFile.path)
                            navController.navigate(ROUTE_PAGE, Bundle().apply {
                                putString(ARG_URI, enterFile.path)
                            })
                        }
                    )

                }
            }
        }
    }
}