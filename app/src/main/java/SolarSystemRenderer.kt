import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.GLU
import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES10
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import com.andreyeyeye.pmu.R
import android.opengl.GLSurfaceView.Renderer
import android.view.MotionEvent
import android.view.View
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties



class SolarSystemRenderer(private val context: Context) : Renderer {
    private lateinit var square: Square
    private val textures = IntArray(6)
    private val planetTextures = intArrayOf(
        R.drawable.sun,
        R.drawable.earth,
        R.drawable.moon,
        R.drawable.mars,
        R.drawable.jupiter,
        R.drawable.neptune
    )
    var showDialog: MutableState<Boolean> = mutableStateOf(false)
    private val planetRadii = floatArrayOf(1.0f, 0.5f, 0.2f, 0.4f, 0.8f, 0.6f)
    private val planetOrbitRadii = floatArrayOf(0.0f, 2.0f, 2.5f, 4.0f, 6.0f, 8.0f)
    private val planetOrbitSpeeds = floatArrayOf(0.0f, 1.0f, 1.2f, 5.8f, 0.6f, 1.6f)
    private val planetRotationSpeeds = floatArrayOf(2.1f, 2.0f, 3.0f, 1.5f, 1.0f, 2.0f)
    private val planetInfoText = arrayOf("Солнце - главная звезда солнечной системы", "Земля - единственная планета с органической жизнью", "Луна - спутник Земли", "Марс - красная планета, названная в честь древнегреческого бога войны", "Юпитер - самый большой газовый гигант солнечной системы", "Нептун - самая дальняя признанная планета солнечной системы")
    private var angle = 0.0f

    private var selectedPlanetIndex = 0
    private lateinit var cube: Cube

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glEnable(GL10.GL_CULL_FACE)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

        loadTextures(gl)
        square = Square(context)
        cube = Cube()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        GLU.gluPerspective(gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f, 100.0f)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    override fun onDrawFrame(gl: GL10) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()

        GLU.gluLookAt(gl, 0.0f, -15.0f, 15.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
        square.draw(gl)

        for (i in 0 until planetTextures.size) {
            if (i != 2) {
                val orbitAngle = angle * planetOrbitSpeeds[i]
                val orbitRadius = planetOrbitRadii[i]
                val x = orbitRadius * Math.cos(Math.toRadians(orbitAngle.toDouble())).toFloat()
                val y = orbitRadius * Math.sin(Math.toRadians(orbitAngle.toDouble())).toFloat()

                val rotationAngle = angle * planetRotationSpeeds[i]
                drawPlanet(gl, i, x, y, 0.0f, orbitAngle, rotationAngle)

                if (i == 1) { // Earth
                    val moonAngle = angle * 2.0f
                    val moonX = x + 0.8f * Math.cos(Math.toRadians(moonAngle.toDouble())).toFloat()
                    val moonY = y + 0.8f * Math.sin(Math.toRadians(moonAngle.toDouble())).toFloat()
                    val moonZ = 0.5f * Math.sin(Math.toRadians(moonAngle.toDouble())).toFloat()

                    drawPlanet(gl, 2, moonX, moonY, moonZ, moonAngle, moonAngle)

                    if (selectedPlanetIndex == 2) {
                        gl.glPushMatrix()
                        gl.glTranslatef(moonX, moonY, moonZ)
                        gl.glRotatef(45.0f, 0.5f, 0.5f, 1.0f)
                        gl.glScalef(
                            planetRadii[selectedPlanetIndex] * 1.2f,
                            planetRadii[selectedPlanetIndex] * 1.2f,
                            planetRadii[selectedPlanetIndex] * 1.2f
                        )
                        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f) // Partially transparent cube
                        cube.draw(gl)
                        gl.glPopMatrix()
                    }
                }
            }
        }

        if (selectedPlanetIndex != 2) { // Not Moon
            val selectedOrbitAngle = angle * planetOrbitSpeeds[selectedPlanetIndex]
            val selectedOrbitRadius = planetOrbitRadii[selectedPlanetIndex]
            val selectedX =
                selectedOrbitRadius * Math.cos(Math.toRadians(selectedOrbitAngle.toDouble()))
                    .toFloat()
            val selectedY =
                selectedOrbitRadius * Math.sin(Math.toRadians(selectedOrbitAngle.toDouble()))
                    .toFloat()

            gl.glPushMatrix()
            gl.glTranslatef(selectedX, selectedY, 0.0f)
            gl.glRotatef(45.0f, 0.5f, 0.5f, 0.0f)
            gl.glScalef(
                planetRadii[selectedPlanetIndex] * 1.2f,
                planetRadii[selectedPlanetIndex] * 1.2f,
                planetRadii[selectedPlanetIndex] * 1.2f
            )
            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f) // Partially transparent cube
            cube.draw(gl)
            gl.glPopMatrix()
        }

        angle += 1.0f
    }

    private fun drawPlanet(
        gl: GL10,
        planetIndex: Int,
        x: Float,
        y: Float,
        z: Float,
        orbitAngle: Float,
        rotationAngle: Float
    ) {
        gl.glPushMatrix()
        gl.glTranslatef(x, y, z)
        gl.glRotatef(rotationAngle, 0.0f, 0.0f, 1.0f)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[planetIndex])
        drawSphere(gl, planetRadii[planetIndex], 100, 100)
        gl.glPopMatrix()
    }

    private fun drawSphere(gl: GL10, radius: Float, numSlices: Int, numStacks: Int) {
        val vertices = FloatArray(numSlices * numStacks * 3)
        val texCoords = FloatArray(numSlices * numStacks * 2)
        val indices = ShortArray(numSlices * numStacks * 6)

        var vertexIndex = 0
        var texCoordIndex = 0
        var indexIndex = 0

        for (stack in 0 until numStacks) {
            val phi = Math.PI * stack.toDouble() / (numStacks - 1)
            for (slice in 0 until numSlices) {
                val theta = 2.0 * Math.PI * slice.toDouble() / numSlices.toDouble()

                val x = radius * Math.sin(phi) * Math.cos(theta)
                val y = radius * Math.sin(phi) * Math.sin(theta)
                val z = radius * Math.cos(phi)

                vertices[vertexIndex++] = x.toFloat()
                vertices[vertexIndex++] = y.toFloat()
                vertices[vertexIndex++] = z.toFloat()

                texCoords[texCoordIndex++] = slice.toFloat() / (numSlices - 1)
                texCoords[texCoordIndex++] = stack.toFloat() / (numStacks - 1)

                if (stack < numStacks - 1 && slice < numSlices - 1) {
                    val i0 = (stack * numSlices + slice).toShort()
                    val i1 = (i0 + 1).toShort()
                    val i2 = ((stack + 1) * numSlices + slice).toShort()
                    val i3 = (i2 + 1).toShort()

                    indices[indexIndex++] = i0
                    indices[indexIndex++] = i2
                    indices[indexIndex++] = i1

                    indices[indexIndex++] = i1
                    indices[indexIndex++] = i2
                    indices[indexIndex++] = i3
                }
            }
        }

        val vertexBuffer =
            ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        vertexBuffer.put(vertices).position(0)

        val texCoordBuffer =
            ByteBuffer.allocateDirect(texCoords.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        texCoordBuffer.put(texCoords).position(0)

        val indexBuffer = ByteBuffer.allocateDirect(indices.size * 2).order(ByteOrder.nativeOrder())
            .asShortBuffer()
        indexBuffer.put(indices).position(0)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer)

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.size, GL10.GL_UNSIGNED_SHORT, indexBuffer)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    }

    private fun loadTextures(gl: GL10) {
        gl.glGenTextures(textures.size, textures, 0)

        for (i in textures.indices) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i])
            gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR.toFloat()
            )

            val bitmap = BitmapFactory.decodeResource(context.resources, planetTextures[i])
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }
    }

    fun moveLeft() {
        selectedPlanetIndex = (selectedPlanetIndex - 1 + planetTextures.size) % planetTextures.size
    }

    fun moveRight() {
        selectedPlanetIndex = (selectedPlanetIndex + 1) % planetTextures.size
    }


    @Composable
    fun showInfo() {
        if (showDialog.value) {
            Dialog(
                onDismissRequest = { showDialog.value = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {

                    Column(

                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(planetInfoText[selectedPlanetIndex])
                        Button(
                            onClick = { showDialog.value = false },
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)

                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

class SolarSystemView(context: Context) : GLSurfaceView(context) {
    private val renderer: SolarSystemRenderer

    init {
        setEGLContextClientVersion(1)
        renderer = SolarSystemRenderer(context)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY


    }
}