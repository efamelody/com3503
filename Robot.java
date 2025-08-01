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

 /**
 * This class stores the Robot
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (31/08/2022)
 */

public class Robot {

  private Camera camera;
  private Light light;

  private Model sphere, cube, cube2;
  private boolean isMoving = true;
  private boolean isMovingSide = false;
  private boolean isMovingStraight = true;
  private boolean isMovingBack = false;
  private boolean isTurning = false;
  private boolean buttonClicked = false;
  private boolean stopButtonClicked = false;

  private int movementStepCounter =0;
  private float turnAngle = 0f; // Current turn angle
  private float targetTurnAngle = 90f; // Target turn angle in degrees
  private float turnSpeed = 1f; // Degrees per second
  private boolean nearRobot1 =false;


  private SGNode robotRoot;
  private float xPosition = 0;
  private float zPosition = 0;
  private float speed = 0.01f;   // Speed of the robot's movement (can be adjusted)
  private float targetDistance = 9.0f; // Distance the robot should move
  private float distanceTraveled = 0f; // Track how much distance has been traveled
  private TransformNode robotMoveTranslate, leftArmRotate, rightArmRotate, robotPlaced, robotTurn, rotatingAntenna;
   
  public Robot(GL3 gl, Camera cameraIn, Light lightIn, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5, Texture t6) {

    this.camera = cameraIn;
    this.light = lightIn;
    light = new Light(gl);

    sphere = makeSphere(gl, t1,t2);

    cube = makeCube(gl, t3,t4);
    cube2 = makeCube(gl, t5,t6);

    // robot
    
    float bodyHeight = 3f;
    float bodyWidth = 2f;
    float bodyDepth = 2.5f;
    float headScale = 1.5f;
    float armLength = 3.5f;
    float armScale = 0.5f;
    float legLength = 3.5f;
    float legScale = 0.67f;
    float robotHeight = bodyHeight + headScale + legLength;
    // Create the light
    NameNode lightNode = new NameNode("light node");

    // Create the transform to position the light
    TransformNode lightTransform = new TransformNode(
        "light transform", 
        Mat4Transform.translate(0, headScale + 1.0f, 0)
    );
    

    
    robotRoot = new NameNode("root");
    robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(0f,0f,0));
    robotPlaced = new TransformNode("robot transform",Mat4Transform.translate(4f,0,-4f));
    robotTurn = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundY(turnAngle));

    
    TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,2f,0));
    TransformNode robotScale = new TransformNode("robot scale",Mat4Transform.scale(1f,0.75f,1f));
    
    // make pieces
    // Add a light node to the scene graph
    NameNode body = makeBody(gl, bodyWidth,bodyHeight,bodyDepth, cube);
    NameNode head = makeHead(gl, bodyHeight, headScale, sphere);
    NameNode leftArm = makeLeftArm(gl, bodyWidth, bodyHeight, armLength, armScale, cube2);
    NameNode rightArm = makeRightArm(gl, bodyWidth, bodyHeight, armLength, armScale, cube2);
    NameNode leftLeg = makeLeftLeg(gl, bodyWidth, legLength, legScale, cube);
    NameNode rightLeg = makeRightLeg(gl, bodyWidth, legLength, legScale, cube);
    // NameNode casing = makeCasing(gl, bodyWidth, bodyHeight, bodyDepth, cube);
    
    //Once all the pieces are created, then the whole robot can be created.
    robotRoot.addChild(robotPlaced);                     // Root node
    robotPlaced.addChild(robotMoveTranslate);            // Translate the robot for movement
    robotMoveTranslate.addChild(robotTurn);              // Rotate the robot for turning
    robotTurn.addChild(robotTranslate);                  // Translate the robot vertically
    robotTranslate.addChild(robotScale);
    robotScale.addChild(body);                       // Attach body
      body.addChild(head);                               // Attach head
      head.addChild(lightTransform);      // Add the light's transform to the head
      lightTransform.addChild(lightNode); // Add the light node to the transform    
    robotRoot.update();  // IMPORTANT - don't forget this

  }

  private Model makeSphere(GL3 gl, Texture t1, Texture t2) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    Model sphere = new Model(name, mesh, modelMatrix, shader, material, light, camera, t1, t2);
    return sphere;
  }
  
  // public LightNode(Light light){
  //   this.light =light;
  // }

  private Model makeCube(GL3 gl, Texture t1, Texture t2) {
    String name= "cube";
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    Model cube = new Model(name, mesh, modelMatrix, shader, material, light, camera, t1, t2);
    return cube;
  } 

  

  private NameNode makeBody(GL3 gl, float bodyWidth, float bodyHeight, float bodyDepth, Model cube) {
    NameNode body = new NameNode("body");
    Mat4 m = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.0f,0));
    TransformNode bodyTransform = new TransformNode("body transform", m);
    ModelNode bodyShape = new ModelNode("Cube(body)", cube);
    body.addChild(bodyTransform);
    bodyTransform.addChild(bodyShape);
    return body;
  }
    
  private NameNode makeHead(GL3 gl, float bodyHeight, float headScale, Model sphere) {
    NameNode head = new NameNode("head"); 
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0f,1.5f));
    m = Mat4.multiply(m, Mat4Transform.scale(headScale,headScale,headScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode headTransform = new TransformNode("head transform", m);
    ModelNode headShape = new ModelNode("Sphere(head)", sphere);
    head.addChild(headTransform);
    headTransform.addChild(headShape);
    return head;
  }

  private NameNode makeLeftArm(GL3 gl, float bodyWidth, float bodyHeight, float armLength, float armScale, Model cube) {
    NameNode leftArm = new NameNode("left arm");
    TransformNode leftArmTranslate = new TransformNode("leftarm translate", 
                                          Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
    // leftArmRotate is a class attribute with a transform that changes over time
    leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode leftArmScale = new TransformNode("leftarm scale", m);
    ModelNode leftArmShape = new ModelNode("Cube(left arm)", cube);
    leftArm.addChild(leftArmTranslate);
    leftArmTranslate.addChild(leftArmRotate);
    leftArmRotate.addChild(leftArmScale);
    leftArmScale.addChild(leftArmShape);
    return leftArm;
  }

  private NameNode makeRightArm(GL3 gl, float bodyWidth, float bodyHeight, float armLength, float armScale, Model cube) {
    NameNode rightArm = new NameNode("right arm");
    TransformNode rightArmTranslate = new TransformNode("rightarm translate", 
                                          Mat4Transform.translate(-(bodyWidth*0.5f)-(armScale*0.5f),bodyHeight,0));
    rightArmRotate = new TransformNode("rightarm rotate",Mat4Transform.rotateAroundX(180));
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode rightArmScale = new TransformNode("rightarm scale", m);
    ModelNode rightArmShape = new ModelNode("Cube(right arm)", cube);
    rightArm.addChild(rightArmTranslate);
    rightArmTranslate.addChild(rightArmRotate);
    rightArmRotate.addChild(rightArmScale);
    rightArmScale.addChild(rightArmShape);
    return rightArm;
  }

  private NameNode makeLeftLeg(GL3 gl, float bodyWidth, float legLength, float legScale, Model cube) {
    NameNode leftLeg = new NameNode("left leg");
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate((bodyWidth*0.5f)-(legScale*0.5f),0,0));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
    m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode leftLegTransform = new TransformNode("leftleg transform", m);
    ModelNode leftLegShape = new ModelNode("Cube(leftleg)", cube);
    leftLeg.addChild(leftLegTransform);
    leftLegTransform.addChild(leftLegShape);
    return leftLeg;
  }

  // Can add the translation. rotate the leg
  private NameNode makeRightLeg(GL3 gl, float bodyWidth, float legLength, float legScale, Model cube) {
    NameNode rightLeg = new NameNode("right leg");
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(-(bodyWidth*0.5f)+(legScale*0.5f),0,0));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
    m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode rightLegTransform = new TransformNode("rightleg transform", m);
    ModelNode rightLegShape = new ModelNode("Cube(rightleg)", cube);
    rightLeg.addChild(rightLegTransform);
    rightLegTransform.addChild(rightLegShape);
    return rightLeg;
  }

  public void render(GL3 gl) {
    robotRoot.draw(gl);
  }

  // public void incXPosition() {
  //   xPosition += 0.5f;
  //   if (xPosition>5f) xPosition = 5f;
  //   updateMove(); 
  // }
   
  // public void decXPosition() {
  //   xPosition -= 0.5f;
  //   if (xPosition<-5f) xPosition = -5f;
  //   updateMove();
  // }
 
  private void updateMove(double elapsedTime) {
    float robotHeight =4f;
    if (movementStepCounter == 0) {  // Step 1: Move Straight
        float movementZ = (float) (elapsedTime * speed);
        zPosition += movementZ;  // Update Z position
        robotMoveTranslate.setTransform(Mat4Transform.translate(0, 0, zPosition));
        robotMoveTranslate.update();
        light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
        // System.out.println("STRAIGHT: Moving along Z. Current position: Z=" + zPosition);
        distanceTraveled += Math.abs(movementZ);

        if (distanceTraveled >= targetDistance) {
            distanceTraveled = 0f; // Reset distance
            isTurning = true; // Start turning
            movementStepCounter++; // Advance to turn step
        }
    } else if (movementStepCounter == 1 ) {  // Step 2 & Step 4: Turn
        if (isTurning && turnAngle == 0) {
            // System.out.println("Turning started. Target angle: " + targetTurnAngle);
        }

        float angleIncrement = Math.max(turnSpeed * (float) elapsedTime, 0.001f);
        turnAngle += angleIncrement;
        light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
        robotTurn.setTransform(Mat4Transform.rotateAroundY(-turnAngle));
        robotTurn.update();
        
        // System.out.println("Turning... Current angle: " + turnAngle);

        if (turnAngle >= targetTurnAngle) {
            turnAngle = 0f; // Reset angle
            isTurning = false; // Turn complete
            movementStepCounter++; // Advance to next step
            // System.out.println("Turn complete. Advancing to step: " + movementStepCounter);
        }
    } else if (movementStepCounter == 2) {  // Step 3: Move Side
        float movementX = (float) (elapsedTime * speed);
        xPosition -= movementX;  // Update X position
        robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition, 0, zPosition));
        robotMoveTranslate.update();
        light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
        // System.out.println("SIDE: Moving along X. Current position: X=" + xPosition);

        distanceTraveled += Math.abs(movementX);
        if (distanceTraveled >= targetDistance) {
            distanceTraveled = 0f; // Reset distance
            movementStepCounter++; // Advance to turn step
        }
    } else if (movementStepCounter == 3) {  // Step 2 & Step 4: Turn
      if (isTurning && turnAngle == 0) {
          // System.out.println("Turning started. Target angle: " + targetTurnAngle);
      }

      float angleIncrement = Math.max(turnSpeed * (float) elapsedTime, 0.1f);
      turnAngle += angleIncrement;

      robotTurn.setTransform(Mat4Transform.rotateAroundY(-turnAngle - 90f));
      robotTurn.update();
      light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
      // System.out.println("Turning... Current angle: " + turnAngle);

      if (turnAngle >= targetTurnAngle) {
          turnAngle = 0f; // Reset angle
          isTurning = false; // Turn complete
          movementStepCounter++; // Advance to next step
          // System.out.println("Turn complete. Advancing to step: " + movementStepCounter);
      } 
    }else if (movementStepCounter == 4) {  // Step 5: Move Backward
        float movementZ = (float) (elapsedTime * speed);
        zPosition -= movementZ;  // Move back along Z axis
        robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition, 0, zPosition));
        robotMoveTranslate.update();
        light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
        // System.out.println("BACKWARD: Moving along Z. Current position: Z=" + zPosition);

        distanceTraveled += Math.abs(movementZ);
        if (distanceTraveled >= targetDistance) {
            distanceTraveled = 0f; // Reset distance
            movementStepCounter++; // Reset for next cycle
            // robotMoveTranslate.setTransform(Mat4Transform.translate(0, 0, 0));
            // robotMoveTranslate.update();
            // System.out.println("Cycle complete. Resetting counter.");
        }
    }else if (movementStepCounter == 5) {  // Step 2 & Step 4: Turn
      if (isTurning && turnAngle == 0) {
          // System.out.println("Turning started. Target angle: " + targetTurnAngle);
      }

      float angleIncrement = Math.max(turnSpeed * (float) elapsedTime, 0.1f);
      turnAngle += angleIncrement;

      robotTurn.setTransform(Mat4Transform.rotateAroundY(turnAngle));
      robotTurn.update();
      light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
      // System.out.println("Turning... Current angle: " + turnAngle);

      if (turnAngle >= targetTurnAngle) {
          turnAngle = 0f; // Reset angle
          isTurning = false; // Turn complete
          movementStepCounter++; // Advance to next step
          // System.out.println("Turn complete. Advancing to step: " + movementStepCounter);
      }
    }else if (movementStepCounter == 6) {  // Step 3: Move Side
      float movementX = (float) (elapsedTime * speed);
      xPosition += movementX;  // Update X position
      robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition, 0, zPosition));
      robotMoveTranslate.update();
      light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
      // System.out.println("SIDE: Moving along X. Current position: X=" + xPosition);

      distanceTraveled += Math.abs(movementX);
      if (distanceTraveled >= targetDistance) {
          distanceTraveled = 0f; // Reset distance
          movementStepCounter++; // Advance to turn step
      }
    }else if (movementStepCounter == 7) {  // Step 7: Final Turn to Reset Orientation
      if (isTurning && turnAngle == 0) {
          // System.out.println("Turning back to initial orientation. Target angle: " + targetTurnAngle);
      }
  
      float angleIncrement = turnSpeed * (float) elapsedTime;
      turnAngle += angleIncrement;
  
      robotTurn.setTransform(Mat4Transform.rotateAroundY(0));
      robotTurn.update();
      light.setPosition(new Vec3(xPosition, robotHeight, zPosition));
      System.out.println("Turning back... Current angle: " + turnAngle);
  
      if (turnAngle >= targetTurnAngle) {
          turnAngle = 0f; // Reset angle
          isTurning = false; // Turn complete
          movementStepCounter = 0; // Reset for next cycle
          // System.out.println("Final turn complete. Resetting to step: " + movementStepCounter);
      }
   }
  }

  public Light getLight() {
    return this.light;
  }


  public Vec3 getPosition() {
    float offset =4f;
    return new Vec3(xPosition + offset, 4f, zPosition- offset);
  }

  public void setButtonClicked(boolean buttonClicked) {
    this.buttonClicked = buttonClicked;
  }

  public boolean isButtonClicked() {
      return buttonClicked;
  }

  public void setStopButtonClicked(boolean stopButtonClicked) {
    this.stopButtonClicked = stopButtonClicked;
  }

  public boolean isStopButtonClicked() {
      return stopButtonClicked;
  }
  public void nearRobot1() {
    System.out.println("isButtonClicked: " + isButtonClicked());

    // Check if the stop button was clicked (this should be set when the stop button is clicked)
    if (isStopButtonClicked()) {
        nearRobot1 = false;  // Immediately stop when stop button is clicked
        System.out.println("Near robot 1: FALSE (Stop button clicked)");
        return;  // Exit early since stop button condition is met
    }

    // If the button is clicked, keep nearRobot1 as true
    if (isButtonClicked()) {
        nearRobot1 = true;
        System.out.println("Near robot 1: TRUE (Dance button clicked)");
        return;
    }

        // Check movement step conditions if the button was clicked
    if (movementStepCounter == 3 || movementStepCounter == 4 || movementStepCounter == 5 || movementStepCounter == 6) {
        nearRobot1 = true;
        System.out.println("Near robot 1: TRUE (Movement step condition met)");
    } 
    if (movementStepCounter == 0 || movementStepCounter == 1 || movementStepCounter == 2 || movementStepCounter == 7){
      nearRobot1 = false;
    } 
    
  }




  public boolean isNearRobot1() {
    return nearRobot1;
 }

  public void setNearRobot1(boolean state) {
    nearRobot1 = state;
  }









  // Update the animation and movement logic
  public void updateAnimation(double elapsedTime) {
    // Left arm animation (unchanged)
    float rotateAngle = 180f + 90f * (float) Math.sin(elapsedTime);
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    leftArmRotate.update();
  //   if (movementStepCounter == 5) {
  //     System.out.println("Animation stopped. Final position reached.");
  //     return; // Exit to stop further updates
  // }
    // System.out.println("Current Step Counter: " + movementStepCounter);
    updateMove(elapsedTime);
  }



  public void loweredArms() {
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(180));
    leftArmRotate.update();
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(180));
    rightArmRotate.update();
  }

  public void raisedArms() {
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(0));
    leftArmRotate.update();
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(0));
    rightArmRotate.update();
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
  }


}