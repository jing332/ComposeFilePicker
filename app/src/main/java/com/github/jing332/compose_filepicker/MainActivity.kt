package com.github.jing332.compose_filepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.github.jing332.compose_filepicker.ui.theme.ComposefilepickerTheme
import com.github.jing332.filepicker.FilePicker
import com.github.jing332.filepicker.FilePickerConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposefilepickerTheme {
                Scaffold {
                    Column(Modifier.padding(bottom = it.calculateBottomPadding())) {
                        FilePicker(config = FilePickerConfig(
//                            fileFilter = { it.name.startsWith("a", ignoreCase = true) },
                            fileSelector = { it.name.startsWith("A") }
                        ))
                    }
                }
            }
        }
    }
}