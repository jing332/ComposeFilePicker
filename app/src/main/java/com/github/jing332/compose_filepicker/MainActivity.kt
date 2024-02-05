package com.github.jing332.compose_filepicker

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.jing332.compose_filepicker.ui.theme.ComposefilepickerTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    @Suppress("DEPRECATION", "unused")
    private fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        setLocale(Locale("en"))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ComposefilepickerTheme {
                var saveFileMode by remember { mutableStateOf(false) }
                var filename by remember { mutableStateOf("test.txt") }
                var isOnlyDir by remember { mutableStateOf(false) }
                var isSingleSelect by remember { mutableStateOf(false) }
                var selectMode by remember { mutableStateOf(SelectMode.ALL) }

                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) {
                    Dialog(onDismissRequest = { showDialog = false }) {
                        Surface {
                            FilePickerScreen(
                                filename = if (saveFileMode) filename else null,
                                onlyShowDir = isOnlyDir,
                                singleSelect = isSingleSelect,
                                selectMode = selectMode
                            )
                        }
                    }
                }
                Scaffold { paddings ->
                    Column(
                        Modifier
                            .padding(paddings)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CheckBox(text = "Save file", checked = saveFileMode) {
                            saveFileMode = it
                        }
                        AnimatedVisibility(visible = saveFileMode) {
                            TextField(value = filename, onValueChange = { filename = it })
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedCard {
                            Column(Modifier.padding(8.dp)) {

                                Text(text = "Select", style = MaterialTheme.typography.titleMedium)
                                Row {
                                    RadioButton(
                                        text = "All",
                                        checked = selectMode == SelectMode.ALL
                                    ) {
                                        selectMode = SelectMode.ALL
                                    }

                                    RadioButton(
                                        text = "File",
                                        checked = selectMode == SelectMode.FILE
                                    ) {
                                        selectMode = SelectMode.FILE
                                    }

                                    RadioButton(
                                        text = "Folder",
                                        checked = selectMode == SelectMode.FOLDER
                                    ) {
                                        selectMode = SelectMode.FOLDER
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                CheckBox(text = "Single select", checked = isSingleSelect) {
                                    isSingleSelect = it
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { showDialog = true }) {
                            Text("Dialog")
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun RadioButton(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(role = Role.RadioButton) {
            onCheckedChange(true)
        }) {
        androidx.compose.material3.RadioButton(selected = checked, onClick = null)
        Text(
            text = text, modifier = Modifier
                .padding(start = 4.dp)
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun CheckBox(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(role = Role.Checkbox) {
            onCheckedChange(!checked)
        }) {
        Checkbox(checked = checked, onCheckedChange = null)
        Text(
            text = text, modifier = Modifier
                .padding(start = 4.dp)
                .padding(vertical = 8.dp)
        )
    }
}