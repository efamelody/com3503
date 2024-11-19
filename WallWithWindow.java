public class WallWithWindow {
    // Define vertices for the wall with a hole (window)
    public static final float[] vertices = {
        -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // 0
        -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // 1
        0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f,  // 2
        0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,   // 3
        
        
        
        -0.4f, 0.0f, -0.4f,  0.0f, 1.0f, 0.0f,  0.2f, 0.2f,   // 4
        -0.4f, 0.0f,  0.4f,  0.0f, 1.0f, 0.0f,  0.8f, 0.2f,  // 5
        0.4f, 0.0f,  0.4f,  0.0f, 1.0f, 0.0f,  0.8f, 0.8f,  // 6
        0.4f, 0.0f, -0.4f,  0.0f, 1.0f, 0.0f,  0.2f, 0.8f  // 7
    };

    // Define indices for the wall with a hole
    public static final int[] indices = {
        // Outer wall
        0,1,4,
        1,5,4,
        1,2,5,
        2,6,5,
        2,7,6,
        2,3,7,
        3,0,7,
        0,4,7


    };

    
}
