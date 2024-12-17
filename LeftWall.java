/*
 * I declare that this code is my own work.
 * Author: Nur Binti Mohd Talib
 * This class defines the geometry for the "Left Wall". 
 * The code follows the tutorial, but I made the following modification:
 * 
 *  **Changed texture coordinates**: The `tex coords` (texture coordinates) were updated
 *   to ensure the texture repeats itself correctly across the surface.
 */
public class LeftWall {
   
    public static final float[] vertices = {      // position, colour, tex coords
        -0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 1.0f,  // Top-left corner of window
        -0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  0.0f, 0.0f,  // Bottom-left corner of window
         0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 0.0f,  // Bottom-right corner of window
         0.2f,  0.2f, 0.0f, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f   // Top-right corner of window
      };

    public static final int[] indices = {  
        //the wall
        0, 1, 2,
        0, 2, 3,
        
    };
}
