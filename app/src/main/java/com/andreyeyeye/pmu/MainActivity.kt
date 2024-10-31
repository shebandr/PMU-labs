package com.andreyeyeye.pmu

import NewsViewModel
import SolarSystemRenderer
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.andreyeyeye.pmu.ui.theme.PMUTheme
import Window

class MainActivity : ComponentActivity() {
    private val viewModel: NewsViewModel by viewModels()
    private var glSurfaceView: GLSurfaceView? = null
    private lateinit var renderer: SolarSystemRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = GLSurfaceView(this)
        glSurfaceView!!.setEGLConfigChooser(8, 8, 8, 8, 16, 1)
        renderer = SolarSystemRenderer(this)
        glSurfaceView!!.setRenderer(renderer)
        glSurfaceView!!.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)

        setContent {
            PMUTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { glSurfaceView!! }
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        Button(
                            onClick = { renderer.moveLeft() },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Left")
                        }

                        Button(
                            onClick = { renderer.showInfo() },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Info")
                        }

                        Button(
                            onClick = { renderer.moveRight() },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Right")
                        }
                    }

                    // Добавляем Window на самый верхний слой
                    Window(viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PMUTheme {
        val viewModel = NewsViewModel()
        Window(viewModel)
    }
}