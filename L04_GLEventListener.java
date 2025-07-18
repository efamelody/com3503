import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;

  
public class L04_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
  
    
  /* The constructor is not used to initialise anything */
  public L04_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(10f,10f,15f));
    // this.camera.setPosition(new Vec3(4f,10f,10f));
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
    sphere.dispose(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Model cube, tt1, tt2, tt3, tt4, tt5, tt6, globe;
  private Mat4 perspective;
  private Light light;
  private Robot robot;
  private Mat4[] roomTransforms;
  private SGNode robotRoot;
  private Model sphere, sphereBase, sphereBody, sphereArm, sphereHead;
  private SGNode twoBranchRoot;
  private boolean buttonClicked2 = false;
  private boolean paused = false;

  private TransformNode translateX, rotateAll, rotateUpper1, rotateUpper2, rotateHead, headBalloon;
  private float xPosition = 0.5f;
  
  private float rotateAllAngleStart = 25, rotateAllAngle = rotateAllAngleStart;
  private float rotateHeadStart = 30, rotateHeadAngle = rotateHeadStart;
  private float rotateUpper1AngleStart = 120, rotateUpper1Angle = rotateUpper1AngleStart;
  private float rotateUpper2AngleStart = 90, rotateUpper2Angle = rotateUpper2AngleStart;
  private float rotationAngle = 0.0f;
  private float previousTime = 0f;
  private float headScaleFactor;
  

  public L04_GLEventListener(Robot robot) {
    this.robot = robot;  // Initialize the robot object through constructor or setter
  }

  
  public void initialise(GL3 gl) {
    textures = new TextureLibrary();
    startTime = System.currentTimeMillis();
    textures.add(gl, "diffuse", "assets/textures/container2.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "specular", "assets/textures/container2_specular.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "diffuse_nur", "assets/textures/diffuse_nur.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "specular_nur", "assets/textures/specular_nur.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "floor_texture", "assets/textures/chequerboard.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "ceiling_texture", "assets/textures/cloud.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "wall_texture", "assets/textures/wattBook.jpg", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "cloud", "assets/textures/cloud.jpg", GL3.GL_LINEAR, GL3.GL_LINEAR);
    textures.add(gl, "star", "assets/textures/star.png", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "earth", "assets/textures/earth.png", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "clown", "assets/textures/clown.png", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "rightWall", "assets/textures/rightWall.png", GL3.GL_REPEAT, GL3.GL_REPEAT);   
    textures.add(gl, "base", "assets/textures/base.png", GL3.GL_REPEAT, GL3.GL_REPEAT);
    textures.add(gl, "body", "assets/textures/body.png", GL3.GL_REPEAT, GL3.GL_REPEAT);  
    textures.add(gl, "arm", "assets/textures/arm.png", GL3.GL_REPEAT, GL3.GL_REPEAT); 
    textures.add(gl, "face", "assets/textures/face.png", GL3.GL_REPEAT, GL3.GL_REPEAT);
    textures.add(gl, "face2", "assets/textures/face2.png", GL3.GL_REPEAT, GL3.GL_REPEAT);  
    textures.add(gl, "cheetah", "assets/textures/cheetah.png", GL3.GL_REPEAT, GL3.GL_REPEAT); 
    textures.add(gl, "bear", "assets/textures/bear.png", GL3.GL_REPEAT, GL3.GL_REPEAT); 
    textures.add(gl, "zebra", "assets/textures/zebra.png", GL3.GL_REPEAT, GL3.GL_REPEAT); 
    textures.add(gl, "wall", "assets/textures/wall.png", GL3.GL_REPEAT, GL3.GL_REPEAT); 
    textures.add(gl, "pedestal", "assets/textures/pedestal.png", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
    textures.add(gl, "circus", "assets/textures/circus.png", GL3.GL_CLAMP_TO_EDGE, GL3.GL_CLAMP_TO_EDGE);
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
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    // material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // diffuse texture only for this model
    tt2 = new Model(name, mesh, new Mat4(1), shader, material, light, camera, textures.get("diffuse_nur"), textures.get("specular_nur"));
    
    name = "window";
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt5 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("diffuse_jade"), textures.get("specular_jade"));
   
    name = "sidewall";
    mesh = new Mesh(gl, WallWithWindow.vertices.clone(), WallWithWindow.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
    material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // no textures for this model
    tt3 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("wall"));

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
    tt4 = new Model(name, mesh, new Mat4(1), shader, material, light, camera , textures.get("bear"));

    name = "cube";
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    cube = new Model(name, mesh, new Mat4(1), shader, material, light, camera, textures.get("pedestal"), textures.get("pedestal"));

   float cubeHeight =1.0F;
   float globeRadius = 3.0f;
    name = "globe";
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    // Mat4 modelMatrix = Mat4Transform.translate(0,cubeHeight/2 + globeRadius,0);
    // Mat4 modelMatrix =  Mat4Transform.scale(3,3,3);
    // modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.translate(0,0.0f,0));
    double elapsedTime = getSeconds()-startTime;
    float angle = (float)(-115*Math.sin(Math.toRadians(elapsedTime*50)));
    System.err.println(angle);
    System.out.println(startTime);
    System.out.println(elapsedTime);
    // Mat4 modelMatrix =  Mat4Transform.rotateAroundY(angle);
    // globe = new Model(name, mesh, modelMatrix, shader, material, light, camera, textures.get("diffuse_jade"), textures.get("specular_jade"));
    globe = new Model(name, mesh, new Mat4(1), shader, material, light, camera, textures.get("earth"));

    //Robot 2
    robot = new Robot(gl, camera, light, 
                      textures.get("face2"), textures.get("cheetah"),
                      textures.get("clown"), textures.get("zebra"),
                      textures.get("base"), textures.get("base")); 

    
    
    //ROBOT 1

  

  // CODE EFA BISMILLAH
    sphere = makeSphere(gl, textures.get("diffuse"), textures.get("specular"));
    sphereBase = makeSphere(gl, textures.get("base"), textures.get("specular"));
    sphereBody = makeSphere(gl, textures.get("body"), textures.get("specular"));
    sphereArm = makeSphere(gl, textures.get("arm"), textures.get("specular"));
    sphereHead = makeSphere(gl, textures.get("face"), textures.get("specular"));
      
    twoBranchRoot = new NameNode("two-branch structure");

    float lowerBranchHeight = 4.0f;
    float baseHeight = 2.5f;
    // SGNode base = makeBase(sphere, 4f, 4f, 4f);
    SGNode base = makeUpperBranch(sphereBase, 2.5f, 2.5f, 2.5f);
    SGNode lowerBranch = makeLowerBranch(sphereBody, 1.5f,lowerBranchHeight,1.5f);
    SGNode upperBranch1 = makeUpperBranch(sphereArm, 0.5f,3.1f,1.0f);
    SGNode upperBranch2 = makeUpperBranch(sphereArm, 0.5f,3.1f,1.0f);
    SGNode head = makeUpperBranch(sphereHead, 2.0f,2.0f,2.0f);

    TransformNode translateToTop1 = new TransformNode("translate(0,"+lowerBranchHeight+",0)",Mat4Transform.translate(0,lowerBranchHeight,0));
    TransformNode translateToTop2 = new TransformNode("translate(0,"+lowerBranchHeight+",0)",Mat4Transform.translate(0,lowerBranchHeight,0));
    TransformNode translateLowerBranchToTop = new TransformNode("translate(0," + baseHeight + ",0)", Mat4Transform.translate(0, baseHeight, 0));
    TransformNode translateBaseToTop = new TransformNode("translate(0," + lowerBranchHeight + ",0)", Mat4Transform.translate(0, lowerBranchHeight, 0));
    // The next few are global variables so they can be updated in other methods
    translateX = new TransformNode("translate("+xPosition+",0,0)", Mat4Transform.translate(-3.0f,0f,-3.0f));  //Translating it to the corner of the room
    rotateAll = new TransformNode("rotateAroundZ("+rotateAllAngle+")", Mat4Transform.rotateAroundZ(rotateAllAngle));
    rotateUpper1 = new TransformNode("rotateAroundZ("+rotateUpper1Angle+")",Mat4Transform.rotateAroundZ(rotateUpper1Angle));
    rotateUpper2 = new TransformNode("rotateAroundZ("+rotateUpper2Angle+")",Mat4Transform.rotateAroundZ(rotateUpper2Angle));
    rotateHead = new TransformNode("rotateAroundZ("+rotateHead+")",Mat4Transform.rotateAroundY(rotateHeadAngle));
    headBalloon = new TransformNode("scaleHead", Mat4Transform.scale(headScaleFactor,headScaleFactor,headScaleFactor));  //Translating it to the corner of the room
    headBalloon.setTransform(Mat4Transform.scale(1.0f, 1.0f, 1.0f));
    // TransformNode sceneTranslation = new TransformNode("translate(" + 5.0f + "," + 0.0f + "," + -10f + ")",Mat4Transform.translate(15.0f, 0.0f, -10f));

    twoBranchRoot.addChild(translateX);
      translateX.addChild(rotateAll);
      rotateAll.addChild(base);
      base.addChild(translateLowerBranchToTop);
        // rotateAll.addChild(lowerBranch);
        translateLowerBranchToTop.addChild(lowerBranch);
          lowerBranch.addChild(translateToTop1);
            translateToTop1.addChild(rotateUpper1);
              rotateUpper1.addChild(upperBranch1);
          lowerBranch.addChild(translateToTop2);     // translateToTop1 could be used here as this is not an animated value
            translateToTop2.addChild(rotateUpper2);  // and here
              rotateUpper2.addChild(upperBranch2);
              // upperBranch2.addChild(head);
              // head.addChild(rotateHead);
          lowerBranch.addChild(translateBaseToTop);
            translateBaseToTop.addChild(rotateHead);
            rotateHead.addChild(headBalloon);
              headBalloon.addChild(head);
          //   base.addChild(rotateHead);
              // sceneTranslation.addChild(translateX);
                // upperBranch2.addChild(sceneTranslation);
    twoBranchRoot.update();  // IMPORTANT – must be done every time any part of the scene graph changes
  }

  public void dance() {
    updateBranches();
  }

  // Getter method for Robot
  public Robot getRobot() {
    if (robot == null) {
      System.out.println("Robot is not initialized!");
    }
    return robot;
  }

  public Light getLight() {
    return light;
  }

  




  // the following two methods are quite similar and could be replaced with one method with suitable parameterisation
  private SGNode makeLowerBranch(Model sphere, float sx, float sy, float sz) {
    NameNode lowerBranchName = new NameNode("lower branch");
    Mat4 m = Mat4Transform.scale(sx,sy,sx);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode lowerBranch = new TransformNode("scale("+sx+","+sy+","+sz+"); translate(0,0.5,0)", m);
    ModelNode sphereNode = new ModelNode("Sphere(0)", sphere);
    lowerBranchName.addChild(lowerBranch);
      lowerBranch.addChild(sphereNode);
    return lowerBranchName;
  }

  private SGNode makeUpperBranch(Model sphere, float sx, float sy, float sz) {
    NameNode upperBranchName = new NameNode("upper branch");
    Mat4 m = Mat4Transform.scale(sx,sy,sz);
    // m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode upperBranch = new TransformNode("scale("+sx+","+sy+","+sz+");translate(0,0.5,0)", m);
    ModelNode sphereNode = new ModelNode("Sphere(1)", sphere);
    upperBranchName.addChild(upperBranch);
      upperBranch.addChild(sphereNode);
    return upperBranchName;
  }

  private SGNode makeBase(Model sphere, float sx, float sy, float sz) {
    NameNode upperBranchName = new NameNode("upper branch");
    Mat4 m = Mat4Transform.scale(sx,sy,sz);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode upperBranch = new TransformNode("scale("+sx+","+sy+","+sz+");translate(0,0.5,0)", m);
    ModelNode sphereNode = new ModelNode("Sphere(1)", sphere);
    upperBranchName.addChild(upperBranch);
      upperBranch.addChild(sphereNode);
    return upperBranchName;
  }

  //Creates 3D model returns a model Object, defines what object Looks like
  private Model makeSphere(GL3 gl, Texture t1, Texture t2) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    Model sphere = new Model(name, mesh, modelMatrix, shader, material, light, camera, t1, t2);
    return sphere;
  } 

  private void updateBranches() {
    double elapsedTime = getSeconds()-startTime;
    rotateAllAngle = rotateAllAngleStart*(float)Math.sin(elapsedTime);
    rotateUpper1Angle = rotateUpper1AngleStart*(float)Math.sin(elapsedTime*0.4f);
    rotateUpper2Angle = rotateUpper2AngleStart*(float)Math.sin(elapsedTime*0.4f);
    rotateHeadAngle = rotateHeadStart*(float)Math.sin(elapsedTime*0.7f);
    rotateHead.setTransform(Mat4Transform.rotateAroundZ(rotateHeadAngle));
    rotateAll.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
    rotateUpper1.setTransform(Mat4Transform.rotateAroundZ(rotateUpper1Angle));
    rotateUpper2.setTransform(Mat4Transform.rotateAroundZ(rotateUpper2Angle));
    headScaleFactor = 2.0f + 0.5f *(float)Math.sin(elapsedTime*0.7f);
    // headBalloon.setTransform(Mat4Transform.scale(1.0f, 1.0f, 1.0f));
    headBalloon.setTransform(Mat4Transform.scale(headScaleFactor, headScaleFactor, headScaleFactor));
    twoBranchRoot.update(); // IMPORTANT – the scene graph has changed
  }

  public void setPauseButton(boolean paused) {
    this.paused = paused;
  }

  public boolean isPaused() {
      return paused;
  }
  
  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // updateLightColour();

    Vec3 robotPos = robot.getPosition();
    float offset = 5f;
    System.out.println("Robot Position: X=" + robotPos.x + ", Y=" + robotPos.y + ", Z=" + robotPos.z);
    light.setPosition(robotPos);  // changing light position each frame
    // light.setPosition(getLightPosition());
    light.render(gl);
    // updateBranches();

    //IF XPOSITION IS A CERTAIN DISTANCE, STOP DANCING
    
    if (robot.isNearRobot1()) {
      updateBranches();  // Only update branches during these steps
   } 
   
    robot.nearRobot1();
    twoBranchRoot.draw(gl);
    cube.setModelMatrix(getMforCube());     // change transform
    cube.render(gl);
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
    // tt5.setModelMatrix(getMforTT6());       // change transform
    // tt5.render(gl);
    // tt6.setModelMatrix(getMforTT7(elapsedTime));       // change transform
    // tt6.render(gl);
    double elapsedTime = getSeconds() - startTime;
    float rotationSpeed = 5.0f; // Degrees per second
    rotationAngle += rotationSpeed ;// Continuous rotation
    rotationAngle = rotationAngle % 360; 

    // Construct the transformation matrix
    Mat4 baseModelMatrix = getMforGlobe(); // Includes scaling and translation
    Mat4 modelMatrix = Mat4.multiply(baseModelMatrix, Mat4Transform.rotateAroundY(rotationAngle));
    // Update and render the globe
    globe.setModelMatrix(modelMatrix);
    globe.render(gl);
    // double elapsedTime = getSeconds(); // Or however you retrieve the elapsed time
    float deltaTime = (float) (elapsedTime - previousTime); // Calculate deltaTime (time difference between frames)
    System.out.println("Elapsed Time: " + elapsedTime);
    System.out.println("Delta Time: " + deltaTime);
    previousTime = (float) elapsedTime; // Update previousTime for the next frame
    if (!isPaused()){  
      robot.updateAnimation(deltaTime*500f);
      // robot.nearRobot1();
    } else{
      // robot.nearRobot1();
      System.out.println("Robot 2 is paused.");
    }
    // robot.updateAnimation(deltaTime*500f);
    robot.render(gl);
    tt6.setModelMatrix(getMforTT7(elapsedTime));       // change transform
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
    modelMatrix = Mat4.multiply(Mat4Transform.translate(6.0f,0.5f,4.0f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(1f,4f,1f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforGlobe() {
    float cubeHeight = 4.0f; // Adjust this based on the cube's scale
    float globeRadius = 3.0f;
    
    Mat4 modelMatrix = new Mat4(1);  // Start with the identity matrix
    modelMatrix = Mat4.multiply(Mat4Transform.scale(3, 3, 3), modelMatrix);

    // Apply rotation based on elapsed time
    double elapsedTime = getSeconds() - startTime;
    float angle = (float)(-115 * Math.sin(Math.toRadians(elapsedTime * 50)));
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(angle), modelMatrix);

    // Position the globe 4 units up along the Y-axis
    modelMatrix = Mat4.multiply(Mat4Transform.translate(6.0f, cubeHeight/2 + globeRadius+ 0.5f, 4.0f), modelMatrix);

   
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
  private Mat4 getMforTT7(double elapsedTime) {
      float size = 16f;
      Mat4 modelMatrix = new Mat4(1);
      modelMatrix = Mat4.multiply(Mat4Transform.scale(size*2f,1f,size*2f), modelMatrix);
      float rotationY = 90 + (float)elapsedTime * 30; // 30 degrees per second
      modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(rotationY), modelMatrix);
      // modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
      modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
      float translationX = -size * 0.5f - 2f + (float)Math.sin(elapsedTime) * 2f;
      float translationY = size * 0.5f + (float)Math.cos(elapsedTime) * 1f;
      modelMatrix = Mat4.multiply(Mat4Transform.translate(translationX, translationY,0), modelMatrix);
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