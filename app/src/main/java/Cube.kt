import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Cube {
    private val vertexBuffer: FloatBuffer
    private val edgeBuffer: FloatBuffer
    private val numFaces = 6
    private val colors = arrayOf(
        floatArrayOf(1.0f, 0.5f, 0.0f, 1.0f),
        floatArrayOf(1.0f, 0.0f, 1.0f, 1.0f),
        floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f),
        floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f),
        floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f),
        floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)
    )
    private val vertices = floatArrayOf(
        // FRONT
        -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
        1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
        -1.0f, 1.0f, 1.0f,  // 2. left-top-front
        1.0f, 1.0f, 1.0f,  // 3. right-top-front
        // BACK
        1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
        -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
        1.0f, 1.0f, -1.0f,  // 7. right-top-back
        -1.0f, 1.0f, -1.0f,  // 5. left-top-back
        // LEFT
        -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
        -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
        -1.0f, 1.0f, -1.0f,  // 5. left-top-back
        -1.0f, 1.0f, 1.0f,  // 2. left-top-front
        // RIGHT
        1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
        1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
        1.0f, 1.0f, 1.0f,  // 3. right-top-front
        1.0f, 1.0f, -1.0f,  // 7. right-top-back
        // TOP
        -1.0f, 1.0f, 1.0f,  // 2. left-top-front
        1.0f, 1.0f, 1.0f,  // 3. right-top-front
        -1.0f, 1.0f, -1.0f,  // 5. left-top-back
        1.0f, 1.0f, -1.0f,  // 7. right-top-back
        // BOTTOM
        -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
        1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
        -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
        1.0f, -1.0f, 1.0f // 1. right-bottom-front
    )

    private val edges = floatArrayOf(
        // FRONT
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        // BACK
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        // LEFT
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        // RIGHT
        1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        // TOP
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        // BOTTOM
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f
    )

    init {
        val byteBuf = ByteBuffer.allocateDirect(vertices.size * 4)
        byteBuf.order(ByteOrder.nativeOrder())
        vertexBuffer = byteBuf.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)

        val edgeByteBuf = ByteBuffer.allocateDirect(edges.size * 4)
        edgeByteBuf.order(ByteOrder.nativeOrder())
        edgeBuffer = edgeByteBuf.asFloatBuffer()
        edgeBuffer.put(edges)
        edgeBuffer.position(0)
    }

    fun draw(gl: GL10) {
        gl.glFrontFace(GL10.GL_CCW)
        gl.glEnable(GL10.GL_CULL_FACE)
        gl.glCullFace(GL10.GL_BACK)
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)


        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)
        for (face in 0 until numFaces) {
            gl.glColor4f(
                colors[face][0],
                colors[face][1], colors[face][2], 0.5f
            )
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face * 4, 4)
        }
        gl.glDisable(GL10.GL_BLEND)

        // Draw edges
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f) // Opaque
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, edgeBuffer)
        gl.glDrawArrays(GL10.GL_LINES, 0, edges.size / 3)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisable(GL10.GL_CULL_FACE)
    }
}