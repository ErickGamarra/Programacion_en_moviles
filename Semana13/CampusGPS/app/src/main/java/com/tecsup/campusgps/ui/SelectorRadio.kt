package com.tecsup.campusgps.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorRadio(
    radios: List<Int>,
    seleccionado: Int,
    onCambiar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        radios.forEachIndexed { index, metros ->
            SegmentedButton(
                selected = metros == seleccionado,
                onClick = { onCambiar(metros) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = radios.size)
            ) {
                Text("$metros m")
            }
        }
    }
}
