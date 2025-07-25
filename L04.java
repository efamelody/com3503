import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class L04 extends JFrame {
  
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private Robot robot;
  private Light light;
  private boolean buttonClicked = false; 
  private boolean stopButtonClicked = false;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private L04_GLEventListener glEventListener;
  private final FPSAnimator animator; 

  public static void main(String[] args) {
    L04 b1 = new L04("L04");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
    b1.canvas.requestFocusInWindow();
  }
  // Getter for buttonClicked
  // public boolean isButtonClicked() {
  //   return buttonClicked;
  // }

  // // Setter for buttonClicked
  // public void setButtonClicked(boolean buttonClicked) {
  //   this.buttonClicked = buttonClicked;
  // }



  public L04(String textForTitleBar) {
    super(textForTitleBar);
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    Camera camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new L04_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);

    // Add a JPanel with a dummy button
    JPanel buttonPanel = new JPanel(); // JPanel to hold the button
    JButton danceButton = new JButton("Dance Button"); // Create the button
    buttonPanel.add(danceButton); // Add the button to the panel

    // Optionally, add an action listener to the button
    danceButton.addActionListener(e -> {
      Robot robot = glEventListener.getRobot();
      if (robot != null) { // Check if robot is initialized
        robot.setButtonClicked(true); 
        robot.setNearRobot1(true); 
        robot.setStopButtonClicked(false);
        System.out.println("Button clicked: nearRobot1 is now TRUE");
      } else {
          System.out.println("Robot is not initialized!");
    }
    });
    buttonPanel.add(danceButton);
    JButton stopButton = new JButton("Stop Dancing");
    stopButton.addActionListener(e -> {
        Robot robot = glEventListener.getRobot();
        if (robot != null) {  // Check if robot is initialized
            robot.setButtonClicked(false);
            robot.setStopButtonClicked(true); // Stop dancing
            robot.setNearRobot1(false);     // Optionally reset nearRobot1 state
            System.out.println("Stop button clicked: Robot stopped.");
        } else {
            System.out.println("Robot is not initialized!");
        }
    });
    buttonPanel.add(stopButton);
    JButton pauseButton = new JButton("Start/Stop Robot 2");
    pauseButton.addActionListener(e -> {
        glEventListener.setPauseButton(!glEventListener.isPaused()); // Toggle pause state
        if (glEventListener.isPaused()) {
            System.out.println("Pause button clicked: Robot 2 is now paused.");
        } else {
            System.out.println("Pause button clicked: Robot 2 resumes moving.");
        }
    });
    buttonPanel.add(pauseButton); 
    // Add a slider for light intensity control
    JLabel lightIntensityLabel = new JLabel("Light Intensity: 70%");
    JSlider lightIntensitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 70); // Min=0, Max=100, Initial=70
    lightIntensitySlider.setMajorTickSpacing(20);
    lightIntensitySlider.setMinorTickSpacing(5);
    lightIntensitySlider.setPaintTicks(true);
    lightIntensitySlider.setPaintLabels(true);
    lightIntensitySlider.addChangeListener(e -> {
        int sliderValue = lightIntensitySlider.getValue();
        lightIntensityLabel.setText("Light Intensity: " + sliderValue + "%");
        float intensity = sliderValue / 100.0f; // Convert to 0.0 to 1.0
        if (glEventListener.getLight() != null) {
            glEventListener.getLight().setIntensity(intensity);
        }
    });
    buttonPanel.add(lightIntensitySlider);
    buttonPanel.add(lightIntensityLabel);

    // Add the panel to the bottom of the frame
    this.add(buttonPanel, BorderLayout.SOUTH);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }
  // public void actionPerformed(ActionEvent e) {
  //   if (e.getActionCommand().equalsIgnoreCase("Dancing")) {
  //     glEventListener.dance();
  //   }
  // }
  
}



class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  
  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }
  
  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }
}