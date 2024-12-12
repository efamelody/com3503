import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
  
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
  
  public void render(GL3 gl) { //, Mat4 perspective, Mat4 view) {
    float time = (float) System.currentTimeMillis() / 1000.0f; // Dynamic rotation based on time
    float angle = time * 50.0f; // 50 degrees per second
    Mat4 model = new Mat4(1);
    model = Mat4.multiply(Mat4Transform.scale(0.3f,0.3f,0.3f), model);
    model = Mat4.multiply(Mat4Transform.translate(position), model);
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), model));
    
    shader.use(gl);
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    shader.setVec3(gl, "light.ambient", material.getAmbient());
    shader.setVec3(gl, "light.diffuse", material.getDiffuse());
    shader.setVec3(gl, "light.specular", material.getSpecular());

  
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);

    // Render casing
    // Render casing with a different shader
    Shader casingShader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    casingShader.use(gl); // Switch to the casing shader
    Mat4 casingModel = new Mat4(1);
    // casingModel = Mat4.multiply(Mat4Transform.translate(position),casingModel);
    // casingModel = Mat4.multiply(Mat4Transform.translate(0.0f, 0.0f, -0.5f), casingModel);
    // casingModel = Mat4.multiply(casingModel, Mat4Transform.translate(0.5f,0.0f,0.0f));
    // casingModel = Mat4.multiply(Mat4Transform.rotateAroundY(angle), casingModel);
    // casingModel = Mat4.multiply(casingModel, Mat4Transform.translate(0.5f,0.0f,0.5f));
    casingModel = Mat4.multiply(Mat4Transform.scale(0.3f, 0.3f, 0.3f), casingModel);
    casingModel = Mat4.multiply(Mat4Transform.translate(0.1f,0.0f,0.1f), casingModel);
    casingModel = Mat4.multiply(Mat4Transform.rotateAroundY(angle), casingModel);
    casingModel = Mat4.multiply(Mat4Transform.translate(position),casingModel);
    Mat4 casingMvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), casingModel));
    casingShader.setFloatArray(gl, "mvpMatrix", casingMvpMatrix.toFloatArrayForGLSL());

    gl.glBindVertexArray(casingVertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, casingIndices.length, GL.GL_UNSIGNED_INT, 0);

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

}