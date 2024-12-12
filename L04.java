import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class L04 extends JFrame {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private Robot robot;
    private Light light;
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

        // Create the main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Row 1: Buttons
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Add gaps for spacing
        JButton danceButton = new JButton("Dance Button");
        JButton stopButton = new JButton("Stop Dancing");
        JButton pauseButton = new JButton("Start/Stop Robot 2");

        // Add action listeners for the buttons
        danceButton.addActionListener(e -> {
            Robot robot = glEventListener.getRobot();
            if (robot != null) {
                robot.setButtonClicked(true);
                robot.setNearRobot1(true);
                robot.setStopButtonClicked(false);
                System.out.println("Button clicked: nearRobot1 is now TRUE");
            } else {
                System.out.println("Robot is not initialized!");
            }
        });

        stopButton.addActionListener(e -> {
            Robot robot = glEventListener.getRobot();
            if (robot != null) {
                robot.setButtonClicked(false);
                robot.setStopButtonClicked(true);
                robot.setNearRobot1(false);
                System.out.println("Stop button clicked: Robot stopped.");
            } else {
                System.out.println("Robot is not initialized!");
            }
        });

        pauseButton.addActionListener(e -> {
            glEventListener.setPauseButton(!glEventListener.isPaused());
            if (glEventListener.isPaused()) {
                System.out.println("Pause button clicked: Robot 2 is now paused.");
            } else {
                System.out.println("Pause button clicked: Robot 2 resumes moving.");
            }
        });

        // Add buttons to the button row
        buttonRow.add(danceButton);
        buttonRow.add(stopButton);
        buttonRow.add(pauseButton);

        // Add button row to the main panel
        mainPanel.add(buttonRow);

        // Row 2: Sliders
        JPanel sliderRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Horizontal layout for sliders

        // Spotlight slider
        JLabel spotlightIntensityLabel = new JLabel("Spotlight Intensity: 100%");
        JSlider spotlightIntensitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        spotlightIntensitySlider.setMajorTickSpacing(20);
        spotlightIntensitySlider.setMinorTickSpacing(5);
        spotlightIntensitySlider.setPaintTicks(true);
        spotlightIntensitySlider.setPaintLabels(true);
        spotlightIntensitySlider.addChangeListener(e -> {
            int sliderValue = spotlightIntensitySlider.getValue();
            spotlightIntensityLabel.setText("Spotlight Intensity: " + sliderValue + "%");
            float intensity = sliderValue / 100.0f;
            Light[] lights = glEventListener.getLights();
            if (lights != null && lights.length > 1) {
                lights[1].setIntensity(intensity); // Adjust spotlight intensity
            }
        });

        // General light slider
        JLabel generalLightIntensityLabel = new JLabel("General Light Intensity: 70%");
        JSlider generalLightIntensitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 70);
        generalLightIntensitySlider.setMajorTickSpacing(20);
        generalLightIntensitySlider.setMinorTickSpacing(5);
        generalLightIntensitySlider.setPaintTicks(true);
        generalLightIntensitySlider.setPaintLabels(true);
        generalLightIntensitySlider.addChangeListener(e -> {
            int sliderValue = generalLightIntensitySlider.getValue();
            generalLightIntensityLabel.setText("General Light Intensity: " + sliderValue + "%");
            float intensity = sliderValue / 100.0f;
            Light[] lights = glEventListener.getLights();
            if (lights != null && lights.length > 0) {
                lights[0].setIntensity(intensity); // Adjust general light intensity
            }
        });

        // Add sliders and labels to the slider row
        sliderRow.add(spotlightIntensityLabel);
        sliderRow.add(spotlightIntensitySlider);
        sliderRow.add(generalLightIntensityLabel);
        sliderRow.add(generalLightIntensitySlider);

        // Add slider row to the main panel
        mainPanel.add(sliderRow);

        // Add the main panel to the bottom of the frame
        this.add(mainPanel, BorderLayout.SOUTH);

        // Window close behavior
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
}

class MyKeyboardInput extends KeyAdapter {
    private Camera camera;

    public MyKeyboardInput(Camera camera) {
        this.camera = camera;
    }

    public void keyPressed(KeyEvent e) {
        Camera.Movement m = Camera.Movement.NO_MOVEMENT;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                m = Camera.Movement.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                m = Camera.Movement.RIGHT;
                break;
            case KeyEvent.VK_UP:
                m = Camera.Movement.UP;
                break;
            case KeyEvent.VK_DOWN:
                m = Camera.Movement.DOWN;
                break;
            case KeyEvent.VK_A:
                m = Camera.Movement.FORWARD;
                break;
            case KeyEvent.VK_Z:
                m = Camera.Movement.BACK;
                break;
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

    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();
        float sensitivity = 0.001f;
        float dx = (float) (ms.x - lastpoint.x) * sensitivity;
        float dy = (float) (ms.y - lastpoint.y) * sensitivity;
        if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK)
            camera.updateYawPitch(dx, -dy);
        lastpoint = ms;
    }

    public void mouseMoved(MouseEvent e) {
        lastpoint = e.getPoint();
    }
}
