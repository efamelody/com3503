README.txt  
Author: Nur Binti Mohd Talib  
Email: nimohdtalib1@sheffield.ac.uk

Project Description:  
---------------------  
This project builds on the tutorial code provided but incorporates significant modifications and additions.  
The purpose of the project is to implement a scene containing a **robot** with a spotlight, interactive controls,  
animations, and custom geometry.

The following sections describe:  
1. **Files Modified**  
2. **Files Created by Me**  
3. **Textures Generated with ChatGPT**  
4. **What I Kept from the Tutorial**  

--------------------  
1. Files Modified:  
--------------------  
**1.1 L04.java**  
- Added sliders and buttons to control the spotlight intensity, general light intensity, and robot movements.  
- Buttons include: Dance, Stop Dancing, and Start/Stop Robot 2.  
- Added action listeners for these buttons and sliders to interact dynamically with the lights and animations.  

**1.2 L04_GLEventListener.java**  
- Reorganized and extended scene rendering.  
- Added spotlight functionality with smooth motion and direction control.  
- Integrated robot animations (movement, turning, and spotlight behavior).  
- Added methods for rendering custom models and lights.  
- Spotlight casing offset and animation updates were implemented.  
- Added reusable transformation matrices for dynamic models.  

**1.3 Robot.java**  
- Completely modified robot movement logic.  
- Added:  
    - Path-based movement (straight, turn, side, backward).  
    - Smooth transitions between movement steps.  
    - Spotlight that moves dynamically with the robot.  
- Added spotlight casing animation offset with circular motion.  
- Custom head, arms, legs, and antenna transformations for the robot.  
- Integrated logic to pause/start animations using buttons.  

**1.4 Light.java**  
- Extended light functionality to support:  
    - Spotlight direction, cutoff, and outer cutoff.  
    - Spotlight casing rendering as a sphere.  
- Added buffer creation for rendering the spotlight casing.  

**1.5 ModelMultipleLights.java**  
- Added shader support for spotlights.  
- Integrated dynamic light parameters such as type (general light, spotlight), cutoff, and intensity.  
- Extended the shader bindings for multiple lights in the scene.  

----------------------  
2. Files Created by Me:  
----------------------  
**2.1 WallWithWindow.java**  
- Created custom geometry for a wall with a rectangular hole (window).  
- Manually calculated vertex positions, texture coordinates, and indices for the wall with a hole.  
- Geometry excludes the center to simulate a window.

**2.2 LeftWall.java**  
- Modified texture coordinates to repeat the texture seamlessly.  

**2.3 Custom Robot Scene Logic**  
- Implemented complex transformations for the robot using scene graph nodes.  
- Added logic for synchronized robot movement with dynamic spotlight motion and casing behavior.

------------------------------  
3. Textures Generated with ChatGPT:  
------------------------------  
- Some of the textures used in the project were generated using ChatGPT prompts.  
- These include:  
    - **Pedestal Texture**  
    - **Right Wall Texture**  
    - **Base and Body Textures for the Robot**  
    - **Zebra and Cheetah Textures for Robot Parts**  
- I generated these textures to align with the scene and robot's design requirements.  

-----------------------------  
4. Custom Shader Work:  
-----------------------------  
**Fragment Shader (`fs_standard_m_2t.txt`)**:  
- I wrote the fragment shader for spotlight and point light calculations using Phong lighting.  
- This shader includes:  
    - **Attenuation**: Based on distance (constant, linear, quadratic).  
    - **Spotlight Cutoff**: Smooth light intensity based on inner and outer angles.  
    - **Texture Mapping**: Diffuse and specular textures applied dynamically. 

-----------------------------  
5. What I Kept from the Tutorial:  
-----------------------------  
- Scene Graph Structure:  
    - I kept the hierarchical scene graph from the tutorial but customized the transformations and structure for my robot.  

- Basic Shapes:  
    - The sphere and cube models were reused for simplicity but modified with scaling and transformations.  

- Shaders:  
    - Used the shader programs provided in the tutorial as the foundation.  
    - Added spotlight-specific behavior to the existing fragment and vertex shaders.  

- Textures and Meshes:  
    - Reused the texture loading mechanism and basic mesh structures.  
    - Added my textures for models like the pedestal, walls, and robot parts.  

-----------------------------  
Summary of My Work:  
-----------------------------  
1. Added robot animation with dynamic path movements (straight, side, back, turning).  
2. Implemented a spotlight that follows the robot with a casing that moves dynamically in a circular path.  
3. Added interactive UI elements (buttons and sliders) to control animations and lighting.  
4. Created custom geometry for a wall with a window and modified texture coordinates for seamless repetition.  
5. Extended light functionality to include spotlight cutoff, direction, and intensity adjustments.  
6. Modified shaders and models to support multiple lights in the scene.  
7. Generated textures using ChatGPT to enhance the scene’s visual quality.

Overall, I reused the tutorial’s base structure and extended it with custom logic, geometry, animations, and interactivity.  

END OF FILE  
