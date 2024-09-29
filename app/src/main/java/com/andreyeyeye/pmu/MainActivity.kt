package com.andreyeyeye.pmu

import NewsViewModel
import Renderer
import Window
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.andreyeyeye.pmu.ui.theme.PMUTheme
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel

enum class AS{
    L, R
}

class MainActivity : ComponentActivity() {
    private val viewModel: NewsViewModel by viewModels()
    private var g: GLSurfaceView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        g =  GLSurfaceView(this);
        g!!.setEGLConfigChooser(8,8,8,8,16,1);
        g!!.setRenderer( Renderer(this));
        g!!.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);


        setContent {
            PMUTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Добавляем GLSurfaceView
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { g!! }
                    )

                    // Добавляем Window
                    Window(viewModel)
                }
            }

//            PMUTheme {
//                Window(viewModel)
//            }

    }
/*
    override fun onPause() {
        super.onPause()
        g!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        g!!.onResume()
    }
*/

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PMUTheme {
        val viewModel = NewsViewModel()
        Window(viewModel)
    }
}
}

