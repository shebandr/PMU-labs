import android.content.Context
import android.opengl.GLSurfaceView
import android.opengl.GLU
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class Renderer(var context: Context) : GLSurfaceView.Renderer {
    private val mSquare: Square
    private val mCube: Cube
    private var mTransY = 0f
    private var mAngle = 0f

    init {
        mSquare = Square(this.context)
        mCube = Cube()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        gl.glClearDepthf(1.0f)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glDepthFunc(GL10.GL_LEQUAL)
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
        gl.glShadeModel(GL10.GL_SMOOTH)
        gl.glDisable(GL10.GL_DITHER)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        var height = height
        if (height == 0) height = 1
        val aspect = width.toFloat() / height
        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        GLU.gluPerspective(gl, 45f, aspect, 0.1f, 100f)
        gl.glMatrixMode(GL10.GL_MODELVIEW) // Select model-view matrix
        gl.glLoadIdentity()
    }

    override fun onDrawFrame(gl: GL10) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()
        gl.glTranslatef(0.0f, 0.0f, -5.0f)
        mSquare.draw(gl)
        gl.glLoadIdentity()
        gl.glTranslatef(0.0f, 0.0f, -4.0f)
        gl.glScalef(0.2f, 0.2f, 0.2f)
        gl.glRotatef(angleCube, 1.0f, -1.0f, 1.0f)
        mCube.draw(gl)
        gl.glLoadIdentity()
        gl.glTranslatef(
            cos(mTransY.toDouble()).toFloat(), sin(mTransY.toDouble())
                .toFloat() + 0.0f, -4.5f
        )
        gl.glScalef(0.05f, 0.05f, 0.05f)
        gl.glRotatef(mAngle, -1f, -1f, 0f)
        angleCube += speedCube
        mTransY += .05f
        mAngle += 1.8.toFloat()
    }

    companion object {
        private var angleCube = 0f
        private const val speedCube = -1.5f
    }
}