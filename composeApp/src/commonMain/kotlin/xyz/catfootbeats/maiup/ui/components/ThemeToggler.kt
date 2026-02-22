package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import xyz.catfootbeats.maiup.model.ThemeMode
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

@Composable
fun ThemeToggler() {
    val vm: MaiupViewModel = koinViewModel()
    var expanded by remember { mutableStateOf(false) }
    val settings by vm.settingsState.collectAsState()

    Box {
        TextButton(
            onClick = { expanded = true },
        ) {
            Text(
                text = when (settings.themeMode) {
                    ThemeMode.LIGHT -> "浅色模式"
                    ThemeMode.DARK -> "深色模式"
                    ThemeMode.SYSTEM -> "跟随系统"
                }
            )
            /*
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "展开菜单",
                )*/
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp)
        ) {
            DropdownMenuItem(
                text = { Text("跟随系统") },
                onClick = {
                    vm.updateTheme(ThemeMode.SYSTEM)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.BrightnessAuto,
                        contentDescription = "跟随系统"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("浅色模式") },
                onClick = {
                    vm.updateTheme(ThemeMode.LIGHT)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LightMode,
                        contentDescription = "浅色模式"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("深色模式") },
                onClick = {
                    vm.updateTheme(ThemeMode.DARK)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = "深色模式"
                    )
                }
            )
        }
    }
}
