import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        colors = darkColors(
            background = Color(0xFF313638),
            surface = Color(0xFF092230)
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background,
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
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 200.dp),
        contentPadding = PaddingValues(16.dp)
    ) {

        item(
            key = "Switches",
            span = {
                GridItemSpan(this.maxCurrentLineSpan)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Switch Lab")
            }
        }

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
private fun LazyGridScope.experimentItem(
    key: Any,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    item(
        key = key,
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