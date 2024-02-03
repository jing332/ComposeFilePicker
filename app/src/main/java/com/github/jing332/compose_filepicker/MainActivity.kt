package com.github.jing332.compose_filepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jing332.compose_filepicker.ui.theme.ComposefilepickerTheme
import com.github.jing332.filepicker.FilePicker
import com.github.jing332.filepicker.FilePickerConfiguration
import com.github.jing332.filepicker.model.IFileModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposefilepickerTheme {
                var showSelectedList by remember { mutableStateOf<List<IFileModel>?>(null) }
                if (showSelectedList != null) {
                    val list = showSelectedList!!
                    ModalBottomSheet(onDismissRequest = { showSelectedList = null }) {
                        LazyColumn {
                            items(list) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(text = it.name)
                                    Text(text = it.path)
                                }
                            }
                        }
                    }
                }

                Scaffold { paddingValues ->
                    Column(Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
                        FilePicker(
                            config = FilePickerConfiguration(
//                            fileFilter = { it.name.startsWith("a", ignoreCase = true) },
//                                fileSelector =
                            ),
                            onConfirmSelect = {
                                showSelectedList = it
                            }
                        )
                    }
                }
            }
        }
    }
}