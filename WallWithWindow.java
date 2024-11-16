public class WallWithWindow {
    // Define vertices for the wall with a hole (window)
    public static final float[] wallWithWindowVertices = {
        // Outer wall (counter-clockwise)
        // -0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 1.0f,  // Top-left corner of window
        // -0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 0.0f,  // Bottom-left corner of window
        //  0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 0.0f,  // Bottom-right corner of window
        //  0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f   // Top-right corner of window

        -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
        -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
        0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
        0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right
        
        
        // Inner window (counter-clockwise)
        -0.3f, 0.0f, -0.3f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
        -0.3f, 0.0f,  0.3f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
        0.3f, 0.0f,  0.3f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
        0.3f, 0.0f, -0.3f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right
        // -0.1f,  0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 1.0f,  // Top-left corner of window
        // -0.1f, -0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 0.0f,  // Bottom-left corner of window
        //  0.1f, -0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 0.0f,  // Bottom-right corner of window
        //  0.1f,  0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f   // Top-right corner of window
    };

    // Define indices for the wall with a hole
    public static final int[] wallWithWindowIndices = {
        // Outer wall
        0, 1, 4,  // Top-left triangle
        1, 5, 4,  // Top-right triangle
        1, 2, 5,  // Bottom-right triangle
        2, 6, 5,  // Bottom center triangle
        2, 3, 6,  // Bottom-left triangle
        3, 7, 6,  // Left center triangle
        3, 0, 7,  // Top-left center triangle
        0, 4, 7   // Top-left side triangle
        // 0, 1, 2,
        // 0, 2, 3,
    };

    
}
