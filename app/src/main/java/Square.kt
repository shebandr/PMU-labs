import android.content.Context
import android.graphics.BitmapFactory
import android.icu.text.Transliterator.Position
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10
import java.util.Random
class StaticSquare(private val context: Context, private val textureId: Int) {
    private val vertexBuffer: FloatBuffer
    private val textureBuffer: FloatBuffer

    private val vertices = floatArrayOf(
        -12.0f, -10.0f, -25.0f,  // 0. left-bottom
        12.0f, -10.0f, -25.0f,   // 1. right-bottom
        -12.0f, 40.0f, -8.0f,   // 2. left-top
        12.0f, 40.0f, -8.0f     // 3. right-top
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

    fun draw(gl: GL10) {
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
}

class MovingSquare(private val context: Context, private val textureId: Int) {
    private val vertexBuffer: FloatBuffer
    private val textureBuffer: FloatBuffer
    private var positionY: Float = -15.0f // Начальное положение за экраном
    private var startY: Float = -15.0f // Начальная позиция Y
    private var endY: Float = 35.0f // Конечная позиция Y
    private var startX: Float = -15.0f // Начальная позиция Y
    private var endX: Float = 35.0f // Конечная позиция Y
    private var positionX: Float = 35.0f
    private var speedX: Float = 0.5f
    private var speedY: Float = 0.5f
    private var rotationAngle: Float = 0.0f;

    private val vertices = floatArrayOf(
        -1.0f, -1.0f, -4.0f,  // 0. left-bottom
        1.0f, -1.0f, -4.0f,   // 1. right-bottom
        -1.0f, 1.0f, -4.0f,   // 2. left-top
        1.0f, 1.0f, -4.0f     // 3. right-top
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

        // Устанавливаем начальные значения
        resetPosition()
    }

    fun draw(gl: GL10) {
        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        gl.glPushMatrix()
        gl.glTranslatef(positionX, positionY, -4.0f)
        gl.glRotatef(rotationAngle, 0.0f, 0.0f, 1.0f )
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer)

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.size / 3)

        gl.glPopMatrix()


    }

    fun updatePosition() {
        positionY += speedY // Скорость движения
        if (positionY > endY) {
            resetPosition() // Возвращаем прямоугольник за экран и устанавливаем новые начальные и конечные позиции
        }
        positionX += speedX // Скорость движения
        if (positionX > endX) {
            resetPosition() // Возвращаем прямоугольник за экран и устанавливаем новые начальные и конечные позиции
        }
        rotationAngle += 2.5f
    }

    private fun resetPosition() {
        val random = Random(System.currentTimeMillis())
        startY = random.nextFloat() * 30 - 45 // Случайная начальная позиция Y в диапазоне от -15 до 15
        endY = startY + 75 // Случайная конечная позиция Y в диапазоне от startY до startY + 50
        positionY = startY // Устанавливаем начальную позицию
        speedY = random.nextFloat() * 0.2f + 0.05f // Случайная скорость в диапазоне от 0.05 до 0.25
        startX = random.nextFloat() * 30 - 55 // Случайная начальная позиция Y в диапазоне от -15 до 15
        endX = startX + 75 // Случайная конечная позиция Y в диапазоне от startY до startY + 50
        positionX = startX // Устанавливаем начальную позицию
        speedX = random.nextFloat() * 0.2f + 0.05f // Случайная скорость в диапазоне от 0.05 до 0.25
    }
}