import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.ui.tooling.preview.Preview
import radios.Switcher01

@Composable
@Preview
fun App() {
    MaterialTheme(
        colors = darkColors()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Navigator(screen = HomeScreen)
        }
    }
}

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        Experiments()
    }
}

@Composable
private fun Experiments(
    navigator: Navigator = LocalNavigator.currentOrThrow
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        experimentItem(
            key = "Switcher01",
            onClick = {
                navigator.push(Switcher01())
            }
        ) {
            Text("Switcher 01")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun LazyListScope.experimentItem(
    key: Any,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    item(
        key = key
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}