import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

/*
 * I declare that this code is my own work.
 * Author: Nur Binti Mohd Talib
 *
 * Description:
 * This class is based on the tutorial `Light.java`. However, I made the following modifications:
 * 
 * 1. **Spotlight Functionality**:
 *    - Added new variables for spotlight properties: `direction`, `cutOff`, `outerCutOff`, and attenuation factors 
 *      (`constant`, `linear`, `quadratic`).
 *    - Implemented getters and setters to control these properties dynamically.
 * 
 * 2. **Light Representation**:
 *    - Rendered the light source as a **sphere** to visualize its position in the scene.
 *    - Added an animated **casing** (around the spotlight) to visually enhance the spotlight effect.
 *    - Implemented the method `renderCasing()` to render the casing with circular motion around the spotlight.
 * 
 * Overall, the light class now supports both general lights and spotlights with added visualization
 * and enhanced functionality.
 */
  
public class Light {
  
  private Material material;
  private float intensity = 1.0f;
  private Vec3 position;
  private Mat4 model;
  private Shader shader;
  private Camera camera;
  //private Mat4 perspective;
  private int[] casingVertexBufferId = new int[1];
  private int[] casingElementBufferId = new int[1];
  private int[] casingVertexArrayId = new int[1];
  public static final float[] vertices =Sphere.vertices.clone();
  public static final int[] indices = Sphere.indices.clone();
  public static final float[] casingVertices = Sphere.vertices.clone();
  public static final int[] casingIndices = Sphere.indices.clone();
  // private float intensity;
  private float cutOff;
  private float outerCutOff;
  private float constant, linear, quadratic;
  private boolean isSpotlight = false;
  private Vec3 direction;
  private int type = 0;
  
    
  public Light(GL3 gl) {
    material = new Material();
    // this.intensity = intensity;
    material.setAmbient(0.3f * intensity, 0.3f * intensity, 0.3f * intensity);
    material.setDiffuse(0.7f * intensity, 0.7f * intensity, 0.7f * intensity);
    material.setSpecular(0.7f * intensity, 0.7f * intensity, 0.7f * intensity);
    position = new Vec3(3f,2f,1f);
    model = new Mat4(1);
    shader = new Shader(gl, "assets/shaders/vs_light_01.txt", "assets/shaders/fs_light_01.txt");
    fillBuffers(gl);
    fillCasingBuffers(gl);
    this.direction = new Vec3(0.0f, 0.0f, -2.0f);
    this.cutOff = 0.9978f;
    this.outerCutOff = 0.953f;
    this.constant = 1.0f;
    this.linear = 0.09f;
    this.quadratic = 0.032f;
    this.intensity = .5f;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  // Getter for material ambient
  public Vec3 getAmbient() {
    return material.getAmbient();
  }


  // Getter for material diffuse
  public Vec3 getDiffuse() {
    return material.getDiffuse();
  }

  // Getter for material specular
  public Vec3 getSpecular() {
    return material.getSpecular();
  }

  public boolean getIsSpotlight() {
    return isSpotlight;
  }

  public void setIsSpotlight(boolean isSpotlight) {
    this.isSpotlight = isSpotlight;
  }
  


  public Vec3 getDirection() {
    return direction;
  }

  public void setDirection(Vec3 direction) {
      this.direction = direction;
  }

  public float getCutOff() {
      return cutOff;
  }

  public void setCutOff(float cutOff) {
      this.cutOff = cutOff;
  }

  public float getOuterCutOff() {
      return outerCutOff;
  }

  public void setOuterCutOff(float outerCutOff) {
      this.outerCutOff = outerCutOff;
  }

  public float getConstant() {
      return constant;
  }

  public void setConstant(float constant) {
      this.constant = constant;
  }

  public float getLinear() {
      return linear;
  }

  public void setLinear(float linear) {
      this.linear = linear;
  }

  public float getQuadratic() {
      return quadratic;
  }

  public void setQuadratic(float quadratic) {
      this.quadratic = quadratic;
  }

  public float getIntensity() {
    return intensity;
  }

  public void setIntensity(float intensity){
    this.intensity=intensity;
    material.setAmbient(0.3f * intensity, 0.3f * intensity, 0.3f * intensity);
    material.setDiffuse(0.7f * intensity, 0.7f * intensity, 0.7f * intensity);
    material.setSpecular(0.7f * intensity, 0.7f * intensity, 0.7f * intensity);
  }


  
  public void setPosition(Vec3 v) {
    position.x = v.x;
    position.y = v.y;
    position.z = v.z;
  }
  
  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }
  
  public Vec3 getPosition() {
    return position;
  }
  
  public void setMaterial(Material m) {
    material = m;
  }
  
  public Material getMaterial() {
    return material;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  /*public void setPerspective(Mat4 perspective) {
    this.perspective = perspective;
  }*/
  
  public void render(GL3 gl) {
    float time = (float) System.currentTimeMillis() / 1000.0f; // Dynamic time-based rotation
    float angle = time * 50.0f; // Rotation speed (degrees per second)

    // Light model matrix
    Mat4 lightModel = new Mat4(1);
    // lightModel = Mat4.multiply(Mat4Transform.scale(0.3f, 0.3f, 0.3f), lightModel);
    lightModel = Mat4.multiply(Mat4Transform.translate(position), lightModel);
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), lightModel));

    // Render light sphere
    shader.use(gl);
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    shader.setVec3(gl, "light.ambient", material.getAmbient());
    shader.setVec3(gl, "light.diffuse", material.getDiffuse());
    shader.setVec3(gl, "light.specular", material.getSpecular());

    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);

    // Render casing
    renderCasing(gl, angle);
  }

  private void renderCasing(GL3 gl, float angle) {
    Shader casingShader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    casingShader.use(gl);
    double elapsedTime = getSeconds() - startTime;

    // Calculate casing rotation to align with spotlight direction
    float angleY = (float) Math.atan2(direction.z, direction.x);
    float angleX = (float) Math.asin(direction.y);

    Mat4 casingModel = new Mat4(1);
    // Calculate circular motion for the casing
    double theta = Math.toRadians(elapsedTime * 50f);  // Adjust rotation speed
    double radius = 0.1; // Distance of casing from spotlight's position
    float translateX = (float) (radius * Math.cos(theta)); // Circular X-position
    float translateZ = (float) (radius * Math.sin(theta)); // Circular Z-position

    // Apply translations to position casing around the light
    casingModel = Mat4.multiply(casingModel, Mat4Transform.translate(position.x - translateX, position.y, position.z - translateZ));
    casingModel = Mat4.multiply(casingModel, Mat4Transform.rotateAroundY((float) Math.toDegrees(theta))); // Rotate casing around Y-axis
    casingModel = Mat4.multiply(casingModel, Mat4Transform.scale(0.2f, 0.2f, 0.2f)); // Scale casing

    Mat4 casingMvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), casingModel));
    casingShader.setFloatArray(gl, "mvpMatrix", casingMvpMatrix.toFloatArrayForGLSL());

    gl.glBindVertexArray(casingVertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, casingIndices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }



  private void fillLightBuffers(GL3 gl) {
    // Light geometry buffers (existing logic)
    gl.glGenVertexArrays(1, vertexArrayId, 0);
    gl.glBindVertexArray(vertexArrayId[0]);

    gl.glGenBuffers(1, vertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
    gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);

    gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3 * Float.BYTES, 0);
    gl.glEnableVertexAttribArray(0);

    gl.glGenBuffers(1, elementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
  }

  private void fillCasingBuffers(GL3 gl) {
    int stride = vertexStride; // Use the same stride as in fillBuffers
    int numXYZFloats = vertexXYZFloats; // Use the same number of floats for position

    gl.glGenVertexArrays(1, casingVertexArrayId, 0);
    gl.glBindVertexArray(casingVertexArrayId[0]);

    // Generate and bind the vertex buffer
    gl.glGenBuffers(1, casingVertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, casingVertexBufferId[0]);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(casingVertices);
    gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * casingVertices.length, fb, GL.GL_STATIC_DRAW);

    // Setup vertex attributes
    int offset = 0;
    gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride * Float.BYTES, offset);
    gl.glEnableVertexAttribArray(0);

    // Generate and bind the element buffer
    gl.glGenBuffers(1, casingElementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(casingIndices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, casingElementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * casingIndices.length, ib, GL.GL_STATIC_DRAW);

    // Unbind the VAO
    gl.glBindVertexArray(0);
  }


  public void dispose(GL3 gl) {
    gl.glDeleteBuffers(1, vertexBufferId, 0);
    gl.glDeleteVertexArrays(1, vertexArrayId, 0);
    gl.glDeleteBuffers(1, elementBufferId, 0);

    gl.glDeleteBuffers(1, casingVertexBufferId, 0);
    gl.glDeleteVertexArrays(1, casingVertexArrayId, 0);
    gl.glDeleteBuffers(1, casingElementBufferId, 0);
  }

    // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering



    
  

    
  private int vertexStride = 8;
  private int vertexXYZFloats = 3;
  
  // ***************************************************
  /* THE LIGHT BUFFERS
   */

  private int[] vertexBufferId = new int[1];
  public int[] vertexArrayId = new int[1];
  private int[] elementBufferId = new int[1];
    
  private void fillBuffers(GL3 gl) {
    gl.glGenVertexArrays(1, vertexArrayId, 0);
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glGenBuffers(1, vertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
    
    gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);
    
    int stride = vertexStride;
    int numXYZFloats = vertexXYZFloats;
    int offset = 0;
    gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(0);
     
    gl.glGenBuffers(1, elementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
    //gl.glBindVertexArray(0);
  } 

  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

}