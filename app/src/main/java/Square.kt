import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLUtils
import com.andreyeyeye.pmu.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Square(private val context: Context) {
    private val vertexBuffer: FloatBuffer
    private val textureBuffer: FloatBuffer
    private var textureId: Int = 0

    private val vertices = floatArrayOf(
        -3.0f, -3.0f, 0.0f,  // 0. left-bottom
        3.0f, -3.0f, 0.0f,   // 1. right-bottom
        -3.0f, 3.0f, 0.0f,   // 2. left-top
        3.0f, 3.0f, 0.0f     // 3. right-top
    )

    private val textureCoords = floatArrayOf(
        0.0f, 1.0f,  // bottom left
        1.0f, 1.0f,  // bottom right
        0.0f, 0.0f,  // top left
        1.0f, 0.0f   // top right
    )

    init {
        // Vertex buffer
        val byteBuf = ByteBuffer.allocateDirect(vertices.size * 4)
        byteBuf.order(ByteOrder.nativeOrder())
        vertexBuffer = byteBuf.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)

        // Texture buffer
        val textureByteBuf = ByteBuffer.allocateDirect(textureCoords.size * 4)
        textureByteBuf.order(ByteOrder.nativeOrder())
        textureBuffer = textureByteBuf.asFloatBuffer()
        textureBuffer.put(textureCoords)
        textureBuffer.position(0)
    }

    fun loadTexture(gl: GL10): Int {
        val textureHandle = IntArray(1)
        gl.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] != 0) {
            // Загрузка изображения в битмап с ресурса
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.stars)
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle[0])

            // Загружаем текстуру в OpenGL
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)

            // Настройка параметров текстуры
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat())
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE.toFloat())
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE.toFloat())

            // Освобождаем ресурсы битмапа
            bitmap.recycle()
        }

        return textureHandle[0]
    }

    fun draw(gl: GL10) {
        if (textureId == 0) {
            textureId = loadTexture(gl)
        }

        // Отключаем смешивание цветов
        gl.glDisable(GL10.GL_BLEND)

        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer)

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.size / 3)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
        gl.glDisable(GL10.GL_TEXTURE_2D)
    }
    fun delete(gl: GL10) {
        if (textureId != 0) {
            gl.glDeleteTextures(1, intArrayOf(textureId), 0)
            textureId = 0
        }
    }
}