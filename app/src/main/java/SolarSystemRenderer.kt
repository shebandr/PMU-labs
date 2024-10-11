import android.content.Context
import android.opengl.GLES10
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class SolarSystemRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private val sunRadius = 0.2f
    private val planetRadius = 0.05f
    private val moonRadius = 0.02f

    private var angle = 0f

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES10.glClearColor(0f, 0f, 0f, 1f)
        GLES10.glEnable(GL10.GL_DEPTH_TEST)
        GLES10.glEnable(GL10.GL_CULL_FACE) // Включаем отсечение невидимых граней
    }

    override fun onDrawFrame(gl: GL10) {
        // Очистка цвета и глубины
        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        // Установка матрицы модели
        gl.glLoadIdentity()

        // Рисуем Солнце
        drawSun(gl)

        // Обновление угла вращения
        angle += 1.0f

        // Рисуем планеты
        drawPlanet(gl, angle, 0.5f) // Пример для первой планеты (например, Меркурий)
        drawPlanet(gl, angle * 0.8f, 0.7f) // Пример для второй планеты (например, Венера)
        drawPlanet(gl, angle, 0.9f) // Пример для третьей планеты (например, Земля)

        // Рисуем Луну
        drawMoon(gl, angle, 0.9f) // Лунам вращается вокруг Земли
    }

    private fun drawSun(gl: GL10) {
        gl.glColor4f(1f, 1f, 0f, 1f) // Цвет Солнца (желтый)
        gl.glPushMatrix()
        gl.glTranslatef(0f, 0f, -1f) // Позиция Солнца
        drawSphere(gl, sunRadius, 30, 30) // Рисуем Солнце
        gl.glPopMatrix()
    }

    private fun drawPlanet(gl: GL10, angle: Float, distance: Float) {
        // Позиционируем планету
        val x = distance * cos(Math.toRadians(angle.toDouble())).toFloat()
        val y = distance * sin(Math.toRadians(angle.toDouble())).toFloat()

        gl.glPushMatrix()
        gl.glTranslatef(x, y, -1f) // Позиция планеты
        gl.glColor4f(0f, 0f, 1f, 1f) // Цвет планеты (синий для примера)
        drawSphere(gl, planetRadius, 20, 20) // Рисуем планету
        gl.glPopMatrix()
    }

    private fun drawMoon(gl: GL10, angle: Float, planetDistance: Float) {
        // Позиционируем Луну относительно Земли
        val moonDistance = 0.1f // Расстояние Луны от Земли
        val x = planetDistance * cos(Math.toRadians(angle.toDouble())).toFloat() + moonDistance * cos(Math.toRadians(angle * 2.0)).toFloat()
        val y = planetDistance * sin(Math.toRadians(angle.toDouble())).toFloat() + moonDistance * sin(Math.toRadians(angle * 2.0)).toFloat()

        gl.glPushMatrix()
        gl.glTranslatef(x, y, -1f) // Позиция Луны
        gl.glColor4f(0.5f, 0.5f, 0.5f, 1f) // Цвет Луны (серый)
        drawSphere(gl, moonRadius, 20, 20) // Рисуем Луну
        gl.glPopMatrix()
    }

    private fun drawSphere(gl: GL10, radius: Float, latitudeBands: Int, longitudeBands: Int) {
        for (latNumber in 0 until latitudeBands) {
            val theta = latNumber * Math.PI / latitudeBands
            val sinTheta = sin(theta).toFloat()
            val cosTheta = cos(theta).toFloat()

            for (longNumber in 0 until longitudeBands) {
                val phi = longNumber * 2.0 * Math.PI / longitudeBands
                val sinPhi = sin(phi).toFloat()
                val cosPhi = cos(phi).toFloat()

                val x = cosPhi * sinTheta
                val y = cosTheta
                val z = sinPhi * sinTheta

                gl.glNormal3f(x, y, z) // Установка нормалей

                // Используйте gl для вызова glVertex3f
                gl.glVertex3f(x * radius, y * radius, z * radius) // Вершина сферы
            }
        }


}

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES10.glViewport(0, 0, width, height)
        GLES10.glMatrixMode(GL10.GL_PROJECTION)
        GLES10.glLoadIdentity()
        GLES10.glFrustumf(-1f, 1f, -1f, 1f, 1f, 10f)
        GLES10.glMatrixMode(GL10.GL_MODELVIEW)
    }
}
