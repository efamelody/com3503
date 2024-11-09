public class LeftWall {
    // public static final float[] vertices = {  
        
    //     // Window vertices (positioned at the center of the wall)
    //     -0.2f,  0.2f, 0.0f, 1.0f, 0.0f,  0.0f, 1.0f,    // Top-left corner of window
    //     -0.2f, -0.2f, 0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // Bottom-left corner of window
    //      0.2f, -0.2f, 0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // Bottom-right corner of window
    //      0.2f,  0.2f, 0.0f,  1.0f, 0.0f, 1.0f, 1.0f,  // Top-right corner of window
    // };
    public static final float[] vertices = {      // position, colour, tex coords
        -0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 1.0f,  // Top-left corner of window
        -0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 0.0f,  // Bottom-left corner of window
         0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 0.0f,  // Bottom-right corner of window
         0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f   // Top-right corner of window
      };

    // public static final float[] vertices = {      // position, colour, tex coords
    //     -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
    //     -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
    //      0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
    //      0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right
    //   };

    public static final int[] indices = {  
        //the wall
        0, 1, 2,
        0, 2, 3,
        
    };
}
