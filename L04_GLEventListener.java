import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

  
public class L04_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
    
  /* The constructor is not used to initialise anything */
  public L04_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,6f,15f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW

    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    cube.dispose(gl);
    tt1.dispose(gl);
    light.dispose(gl);
    textures.destroy(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Model cube, tt1, tt2, tt3, tt4, tt5, tt6;
  private Light light;
  private Mat4[] roomTransforms;
  
  public void initialise(GL3 gl) {
    textures = new TextureLibrary();
    textures.add(gl, "diffuse", "assets/textures/container2.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "specular", "assets/textures/container2_specular.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "floor_texture", "assets/textures/chequerboard.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "ceiling_texture", "assets/textures/cloud.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "wall_texture", "assets/textures/wattBook.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "cloud", "assets/textures/cloud.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "star", "assets/textures/star.png", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "rightWall", "assets/textures/rightWall.png", GL3.GL_REPEAT, GL3.GL_REPEAT);
    // textures = new TextureLibrary();
    // textures.add(gl, "diffuse", "assets/textures/container2.jpg");
    // textures.add(gl, "specular", "assets/textures/container2_specular.jpg");
    // textures.add(gl, "floor_texture", "assets/textures/chequerboard.jpg");
    // textures.add(gl, "ceiling_texture", "assets/textures/cloud.jpg");
    // textures.add(gl, "wall_texture", "assets/textures/wattBook.jpg");
    // textures.add(gl, "cloud", "assets/textures/cloud.jpg");
    // textures.add(gl, "rightWall", "assets/textures/rightWall.png");


    
    light = new Light(gl);
    light.setCamera(camera);
    
    String name = "flat plane";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt1 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("floor_texture"));

    name = "back wall";
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    // material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // diffuse texture only for this model
    tt2 = new Model(name, mesh, new Mat4(1), shader, material, light, camera, textures.get("cloud"));
    
    name = "window";
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt5 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("cloud"));
   
    name = "sidewall";
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt3 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("cloud"));

    name = "stars";
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt6 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("star"));
    
    name = "Right wall";
    mesh = new Mesh(gl, RightWall.vertices.clone(), RightWall.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt4 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("rightWall"));

    name = "cube";
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    cube = new Model(name, mesh, new Mat4(1), shader, material, light, camera, textures.get("diffuse"), textures.get("specular"));

    roomTransforms = setupRoomTransforms();
  }
 
  // Transforms may be altered each frame for objects so they are set in the render method. 
  // If the transforms do not change each frame, then the model matrix could be set in initialise() and then only retrieved here,
  // although if the same object is being used in multiple positions, then
  // the transforms would need updating for each use of the object.
  // For more efficiency, if the object is static, its vertices could be defined once in the correct world positions.
  
  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    //updateLightColour();
    // light.setPosition(getLightPosition());  // changing light position each frame
    // light.render(gl);
    
    // cube.setModelMatrix(getMforCube());     // change transform
    // cube.render(gl);
    tt1.setModelMatrix(getMforTT1());       // change transform
    tt1.render(gl);
    // tt1.setModelMatrix(getMforTT2());       // change transform
    // tt1.render(gl);
    // tt1.setModelMatrix(getMforTT3());       // change transform
    // tt1.render(gl);
    // tt1.setModelMatrix(getMforTT4());       // change transform
    // tt1.render(gl);
    tt1.setModelMatrix(getMforTT5());       // change transform
    tt1.render(gl);
    tt2.setModelMatrix(getMforTT2());       // change transform
    tt2.render(gl);
    tt3.setModelMatrix(getMforTT3());       // change transform
    tt3.render(gl);
    tt4.setModelMatrix(getMforTT4());       // change transform
    tt4.render(gl);
    tt5.setModelMatrix(getMforTT6());       // change transform
    tt5.render(gl);
    tt6.setModelMatrix(getMforTT7());       // change transform
    tt6.render(gl);

  
  }
  
  // Method to alter light colour over time
  
  private void updateLightColour() {
    double elapsedTime = getSeconds()-startTime;
    Vec3 lightColour = new Vec3();
    lightColour.x = (float)Math.sin(elapsedTime * 2.0f);
    lightColour.y = (float)Math.sin(elapsedTime * 0.7f);
    lightColour.z = (float)Math.sin(elapsedTime * 1.3f);
    Material m = light.getMaterial();
    m.setDiffuse(Vec3.multiply(lightColour,0.5f));
    m.setAmbient(Vec3.multiply(m.getDiffuse(),0.2f));
    light.setMaterial(m);
  }
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 3.4f;
    float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);
  }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforCube() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,0.5f,0f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4f,4f,4f), modelMatrix);
    return modelMatrix;
  }
  
  private Mat4[] setupRoomTransforms() {
    Mat4[] t = new Mat4[5];
    t[0] = getMforTT1(); // floor
    t[1] = getMforTT2(); // back wall
    t[2] = getMforTT3(); // left wall
    t[3] = getMforTT4(); // right wall
    t[4] = getMforTT5(); //ceiling
    return t;
  }
  
  //Scales the plane to a large size to reperesent the floor
  private Mat4 getMforTT1() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    return modelMatrix;
  }
  
  //Rotatets the plane 90 degrees around x-axis then translates it upward and backward. This position th eplane vertically
  //BackWall
  private Mat4 getMforTT2() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
    return modelMatrix;
  }

  //Rotates the plpane around both the y-axis and z-axis, translates it left and upward. 
  //Left Wall
  private Mat4 getMforTT3() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }
  //Outside the window
  private Mat4 getMforTT7() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size*2f,1f,size*2f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f-2f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }
  // window
  private Mat4 getMforTT6() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(8f,1f,8f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f+0.3f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  //RIGHT WALL
  private Mat4 getMforTT4() {
    float size = 16f; // Use the same size as the other walls
    Mat4 modelMatrix = new Mat4(1);

    // Scale the plane to the right size
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);

    // Rotate around y-axis by 90 degrees and then around z-axis by 90 degrees
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);

    // Translate it to the right position
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, 0), modelMatrix);

    return modelMatrix;
  }
  // private Mat4 getMforTT4() {
  //   float size = 16f; // Wall size, can be adjusted
  //   float textureRepeatX = 4f; // Increase this to repeat texture more on X axis
  //   float textureRepeatZ = 3f; // Increase this to repeat texture more on Z axis
    
  //   Mat4 modelMatrix = new Mat4(1);

  //   // Scale the wall to the right size, adjusting texture repeat
  //   modelMatrix = Mat4.multiply(Mat4Transform.scale(size * textureRepeatX, 1f, size * textureRepeatZ), modelMatrix);

  //   // Rotate around the Y-axis and Z-axis to orient the wall correctly
  //   modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(-90), modelMatrix);
  //   modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);

  //   // Translate it to the right position
  //   modelMatrix = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, 0), modelMatrix);

  //   return modelMatrix;
  // }


  private Mat4 getMforTT5() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(180), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size, 0), modelMatrix); // Move up to form ceiling
    return modelMatrix;
  }


  
    // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
}
