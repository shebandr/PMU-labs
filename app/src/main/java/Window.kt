import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import kotlinx.coroutines.delay

@Composable
fun Window(viewModel: NewsViewModel) {
    var firstRender by remember { mutableStateOf(true) }

    if (viewModel.showWindow) {
        if (firstRender) {
            viewModel.startNews()
            viewModel.changeNews()
            firstRender = false
        }
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            while (true) {
                delay(5000)
                viewModel.changeNewsOne()
            }
        }
        Column( modifier = Modifier.fillMaxSize())
        {
            Row{
                Box(modifier = Modifier.padding(all = 5.dp).clickable { viewModel.click1() }){

                    Box(modifier = Modifier.border(5.dp, color = Color.Black)){
                        Column(modifier = Modifier
                            .width(180.dp)
                            .background(color = Color.Gray)
                            .padding(all = 5.dp)){

                            Text(text = viewModel.newsString1, modifier = Modifier.height(240.dp))
                            Row(modifier = Modifier.height(30.dp)) {
                                Text(text = "лайки: ")
                                Text(text = viewModel.likes1.toString())
                            }
                        }
                    }
                }

                Box(modifier = Modifier.padding(all = 5.dp).clickable { viewModel.click2() }){

                    Box(modifier = Modifier.border(5.dp, color = Color.Black)){
                        Column(modifier = Modifier
                            .width(180.dp)
                            .background(color = Color.Gray)
                            .padding(all = 5.dp)){

                            Text(text = viewModel.newsString2, modifier = Modifier.height(240.dp))
                            Row(modifier = Modifier.height(30.dp)) {
                                Text(text = "лайки: ")
                                Text(text = viewModel.likes2.toString())
                            }
                        }
                    }
                }
            }
            Row{
                Box(modifier = Modifier.padding(all = 5.dp).clickable { viewModel.click3() }){

                    Box(modifier = Modifier.border(5.dp, color = Color.Black)){
                        Column(modifier = Modifier
                            .width(180.dp)
                            .background(color = Color.Gray)
                            .padding(all = 5.dp)){

                            Text(text = viewModel.newsString3, modifier = Modifier.height(240.dp))
                            Row(modifier = Modifier.height(30.dp)) {
                                Text(text = "лайки: ")
                                Text(text = viewModel.likes3.toString())
                            }
                        }
                    }
                }

                Box(modifier = Modifier.padding(all = 5.dp).clickable { viewModel.click4() }){

                    Box(modifier = Modifier.border(5.dp, color = Color.Black)){
                        Column(modifier = Modifier
                            .width(180.dp)
                            .background(color = Color.Gray)
                            .padding(all = 5.dp)){

                            Text(text = viewModel.newsString4, modifier = Modifier.height(240.dp))
                            Row(modifier = Modifier.height(30.dp)) {
                                Text(text = "лайки: ")
                                Text(text = viewModel.likes4.toString())
                            }
                        }
                    }
                }
            }
            Row{
                Button(
                    onClick = { viewModel.closeWindow() },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(start = 30.dp)
                ) {
                    Text("Закрыть", color = Color.Black)
                }
                Button(
                    onClick = { viewModel.changeNews() },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(start = 30.dp)
                ) {
                    Text("Обновить", color = Color.Black)
                }
            }

        }
    }
}