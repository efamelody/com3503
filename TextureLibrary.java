import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.*;
//import com.jogamp.opengl.util.texture.spi.JPEGImage;

import com.jogamp.opengl.util.texture.*;

public class TextureLibrary {
  
  private Map<String,Texture> textures;

  public TextureLibrary() {
    textures = new HashMap<String, Texture>();
  }

  public void add(GL3 gl, String name, String filename, int wrapS, int wrapT) {
    Texture texture = loadTexture(gl, filename, wrapS, wrapT);
    textures.put(name, texture);
  }
  // public void add(GL3 gl, String name, String filename) {
  //   Texture texture = loadTexture(gl, filename);
  //   textures.put(name, texture);
  // }
  // public void add(GL3 gl, String name, String filename, int wrapS, int wrapT, float repeatX, float repeatY) {
  //   Texture texture = loadTexture(gl, filename, wrapS, wrapT, repeatX, repeatY);
  //   textures.put(name, texture);
  // }


  public Texture get(String name) {
    return textures.get(name);
  }

  // no mip-mapping (see below for mip-mapping)
  // public Texture loadTexture(GL3 gl3, String filename) {
  //   Texture t = null; 
  //   try {
  //     File f = new File(filename);
  //     t = (Texture)TextureIO.newTexture(f, true);
	//     t.bind(gl3);
  //     t.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
  //     t.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
  //     // t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
  //     // t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE); 
  //     t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
  //     // t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
  //     // t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE); 
  //     t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT); 
  //   }
  //   catch(Exception e) {
  //     System.out.println("Error loading texture " + filename); 
  //   }
  //   return t;
  // }

  public Texture loadTextureForRightWall(GL3 gl3, String filename) {
    Texture texture = null;
    try {
      File file = new File(filename);
      texture = TextureIO.newTexture(file, true);
      texture.bind(gl3);
  
      texture.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
      texture.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
      texture.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
      texture.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
      texture.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
      texture.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);
    } catch(Exception e) {
      System.out.println("Error loading texture: " + e.getMessage());
    }
    return texture;
  }
  


  // mip-mapping is included in the below example
  public static Texture loadTexture(GL3 gl3, String filename, int wrapS, int wrapT) {
    Texture t = null; 
    try {
        File f = new File(filename);
        t = TextureIO.newTexture(f, true);
        t.bind(gl3);
        t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, wrapS);
        t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, wrapT);
        t.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
        t.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
        gl3.glGenerateMipmap(GL3.GL_TEXTURE_2D);
    } catch(Exception e) {
        System.out.println("Error loading texture " + filename); 
    }
    return t;
  }

  // public static Texture loadTexture(GL3 gl3, String filename, int wrapS, int wrapT, float repeatX, float repeatY) {
  //   Texture t = null;
  //   try {
  //       File f = new File(filename);
  //       t = (Texture) TextureIO.newTexture(f, true);
  //       t.bind(gl3);
        
  //       // Set texture wrap mode to repeat
  //       t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, wrapS);
  //       t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, wrapT);
        
  //       // Set min and mag filter for mip-mapping
  //       t.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR_MIPMAP_LINEAR);
  //       t.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
        
  //       // Generate mipmaps
  //       gl3.glGenerateMipmap(GL3.GL_TEXTURE_2D);
        
  //       // Adjust the texture coordinate scaling factors
  //       gl3.glTexParameterf(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_REDUCE_TO_COLOR, repeatX); // Horizontal repetition (S)
  //       gl3.glTexParameterf(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_REDUCE_TO_COLOR, repeatY); // Vertical repetition (T)

  //   } catch (Exception e) {
  //       System.out.println("Error loading texture " + filename);
  //   }
  //   return t;
  // }


  public void destroy(GL3 gl3) {
    for (var entry : textures.entrySet()) {
      entry.getValue().destroy(gl3);
    }
  }
}