public class WallWithWindow {
    // Define vertices for the wall with a hole (window)
    public static final float[] vertices = {
        // Outer wall (counter-clockwise)
        // -0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 1.0f,  // Top-left corner of window
        // -0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 0.0f,  // Bottom-left corner of window
        //  0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 0.0f,  // Bottom-right corner of window
        //  0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f   // Top-right corner of window

        -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
        -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f,  // bottom left
        0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
        0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f   // top right
        
        
        // Inner window (counter-clockwise)
        -0.4f, 0.0f, -0.4f,  0.0f, 1.0f, 0.0f,  0.3f, 0.7f,  // top left
        -0.4f, 0.0f,  0.4f,  0.0f, 1.0f, 0.0f,  0.7f, 0.7f,  // bottom left
        0.4f, 0.0f,  0.4f,  0.0f, 1.0f, 0.0f,  0.7f, 0.3f,  // bottom right
        0.4f, 0.0f, -0.4f,  0.0f, 1.0f, 0.0f,  0.3f, 0.3f   // top right
        // -0.1f,  0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 1.0f,  // Top-left corner of window
        // -0.1f, -0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 0.0f,  // Bottom-left corner of window
        //  0.1f, -0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 0.0f,  // Bottom-right corner of window
        //  0.1f,  0.1f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f   // Top-right corner of window
    };

    // Define indices for the wall with a hole
    public static final int[] indices = {
            // Outer wall (using the first 4 vertices)
        0, 1, 4,  // Top-left triangle
        1, 5, 4,  // Bottom-left triangle
        1, 2, 5,  // Bottom-right triangle
        2, 6, 5,  // Bottom-right triangle
        2, 3, 6,  // Top-right triangle
        3, 7, 6,  // Top-right triangle
        3, 0, 7,  // Top-left triangle
        0, 4, 7,  // Left triangle

    };

    
}
