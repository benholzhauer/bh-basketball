// Virtual Basketball (JoglEventListener.java)
// Author: Ben Holzhauer
// Date: 12/12/2016
// Class: CS 335, Section 1

package Virtual_Basketball; // Define package

// Import Java components
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import sun.audio.*;

// Import JogAmp components
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.*;

// Define public class for event listener
public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener
{
	private GLU glu = new GLU(); // Define GLU
    private GLUT glut = new GLUT(); // Define GLUT
    
	// Integer variables
 	int windowWidth; // Window width
 	int windowHeight; // Window height
 	
 	// Float variables
 	float aspect; // Aspect ratio
 	float forceLevel = 0.0f; // Force level
 	float tries = 0.0f; // Tries for force gauge usage
 	float x0 = 0.0f; // Initial ball x
	float y0 = 0.0f; // Initial ball y
	float z0 = 128.0f; // Initial ball z
	float tx = 0.0f; // Target x
	float tz = 500.0f; // Target z
	float vx; // Ball x velocity
	float vz; // Ball z velocity
	float speed = 1.0f; // Game speed
	float x = 0.0f; // Ball x
	float y = 0.0f; // Ball y
	float z = 0.0f; // Ball z
	float time = 0.0f; // Current normal time
	float hitTime = 0.0f; // Current time for post-collision movement
	float camx = 0.0f; // Replay camera x
	float camy = -256.0f; // Replay camera y
	float camz = 236.0f; // Replay camera z
	float camxTo = 0.0f; // Replay camera xTo
	float camyTo = 384.0f; // Replay camera yTo
	float camzTo = 236.0f; // Replay camera zTo
	float tempX0 = 0.0f; // Additional x value for skybox placement
 	
 	// Boolean variables
 	boolean showGauge = false; // Flag for gauge visibility
 	boolean canThrow = true; // Flag for ability to throw ball
 	boolean ballMoving = false; // Flag for ball moving
 	boolean hitBoard = false; // Flag for backboard collision
 	boolean hitFloor = false; // Flag for floor collision
 	boolean hitRim = false; // Flag for hoop rim collision
 	boolean canHitBoard = true; // Flag for ability to hit backboard
 	boolean scored = false; // Flag for shot made
 	boolean missed = false; // Flag for shot missed
 	boolean showTarget = true; // Flag for target visibility
 	boolean showBall = true; // Flag for ball visibility
 	boolean replayMode = false; // Flag for replay mode
 	boolean askForReplay = false; // Flag for pending replay mode
 	
 	// Texture variables
 	Texture wallTex = null; // Skybox wall texture
 	Texture ceilingTex = null; // Skybox ceiling texture
 	Texture deepFloorTex = null; // Skybox floor texture
 	Texture floorTex = null; // Court floor texture
 	Texture poleTex = null; // Hoop base and pole texture
 	Texture backboardTex = null; // Hoop backboard texture
 	Texture frameTex = null; // Hoop frame and rim texture
 	Texture ballTex = null; // Basketball texture
 	Texture replayButtonTex = null; // Replay button texture
 	
 	// InputStream and AudioStream variables
 	InputStream winFile = null; // Input stream for winning sound
 	AudioStream winStream = null; // Audio stream for winning sound
 	InputStream missFile = null; // Input stream for missing sound
 	AudioStream missStream = null; // Audio stream for missing sound
 	
 	// Function to draw skybox (walls and ceiling)
 	public void drawSkybox(final GL2 gl)
 	{
 		gl.glPushMatrix();
 		
 		gl.glTranslatef(x0 + tempX0, 0.0f, -800.0f);
 		
 		// Enable and bind skybox wall texture
 		gl.glEnable(GL.GL_TEXTURE_2D);
 		wallTex.bind(gl);
 		
 		// Repetition with wrapping
 		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT); 		
        
 		// Construct textured walls and ceiling...
		// Front side
		gl.glBegin(GL2ES3.GL_QUADS);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.5f*-1088.0f, -448.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 4.0f);
		gl.glVertex3f(1.5f*-1088.0f, -448.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 4.0f);
		gl.glVertex3f(1.5f*1088.0f, -448.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 0.0f);
		gl.glVertex3f(1.5f*1088.0f, -448.0f, 0.0f);
		gl.glEnd();

		// Back side
		gl.glBegin(GL2ES3.GL_QUADS);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.5f*-1088.0f, 1344.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 4.0f);
		gl.glVertex3f(1.5f*-1088.0f, 1344.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 4.0f);
		gl.glVertex3f(1.5f*1088.0f, 1344.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 0.0f);
		gl.glVertex3f(1.5f*1088.0f, 1344.0f, 0.0f);
		gl.glEnd();
		
		// Left side
		gl.glBegin(GL2ES3.GL_QUADS);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.5f*-1088.0f, -448.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 4.0f);
		gl.glVertex3f(1.5f*-1088.0f, -448.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 4.0f);
		gl.glVertex3f(1.5f*-1088.0f, 1344.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 0.0f);
		gl.glVertex3f(1.5f*-1088.0f, 1344.0f, 0.0f);
		gl.glEnd();
		
		// Right side
		gl.glBegin(GL2ES3.GL_QUADS);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1.5f*1088.0f, -448.0f, 0.0f);
		gl.glTexCoord2f(0.0f, 4.0f);
		gl.glVertex3f(1.5f*1088.0f, -448.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 4.0f);
		gl.glVertex3f(1.5f*1088.0f, 1344.0f, 1600.0f);
		gl.glTexCoord2f(4.0f, 0.0f);
		gl.glVertex3f(1.5f*1088.0f, 1344.0f, 0.0f);
		gl.glEnd();
		
		// Ceiling, if not blocking view
		if (camz < 800.0f)
		{
			// Bind skybox ceiling texture
			ceilingTex.bind(gl);
			
			// Repetition with wrapping
	 		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
	        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT); 		
			
			gl.glBegin(GL2ES3.GL_QUADS);
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(1.5f*-1088.0f, -448.0f, 1600.0f);
			gl.glTexCoord2f(0.0f, 4.0f);
			gl.glVertex3f(1.5f*-1088.0f, 1344.0f, 1600.0f);
			gl.glTexCoord2f(4.0f, 4.0f);
			gl.glVertex3f(1.5f*1088.0f, 1344.0f, 1600.0f);
			gl.glTexCoord2f(4.0f, 0.0f);
			gl.glVertex3f(1.5f*1088.0f, -448.0f, 1600.0f);
			gl.glEnd();
		}
    	
		// Disable textures
    	gl.glDisable(GL.GL_TEXTURE_2D);
    	
    	gl.glPopMatrix();
 	}
 	
 	// Function to draw floors (skybox and court)
 	public void drawFloor(final GL2 gl)
 	{
 		gl.glPushMatrix();
 		
 		gl.glTranslatef(x0 + tempX0, 0.0f, 0.0f);
    	
 		// Enable and bind skybox floor texture
 		gl.glEnable(GL.GL_TEXTURE_2D);
 		deepFloorTex.bind(gl);
 		
 		// Repetition with wrapping
 		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT); 
 		
 		// Construct textured skybox floor
    	gl.glBegin(GL2ES3.GL_QUADS);
    	gl.glColor3f(1.0f, 1.0f, 1.0f);
    	gl.glTexCoord2f(0.0f, 0.0f);
    	gl.glVertex3f(1.5f*-1088.0f, -448.0f, -4.0f);
    	gl.glTexCoord2f(0.0f, 2.0f);
    	gl.glVertex3f(1.5f*-1088.0f, 1344.0f, -4.0f);
    	gl.glTexCoord2f(2.0f, 2.0f);
    	gl.glVertex3f(1.5f*1088.0f, 1344.0f, -4.0f);
    	gl.glTexCoord2f(2.0f, 0.0f);
    	gl.glVertex3f(1.5f*1088.0f, -448.0f, -4.0f);
    	gl.glEnd();
    	
    	// Disable textures
    	gl.glDisable(GL.GL_TEXTURE_2D);
    	
    	gl.glPopMatrix();
    	
    	gl.glPushMatrix();
    	
 		// Enable and bind court floor texture
    	gl.glEnable(GL.GL_TEXTURE_2D);
    	floorTex.bind(gl);
    	
    	// Construct textured court floor
    	gl.glBegin(GL2ES3.GL_QUADS);
    	gl.glColor3f(1.0f, 1.0f, 1.0f);
    	gl.glTexCoord2f(0.0f, 0.0f);
    	gl.glVertex3f(-576.0f, -128.0f, 0.0f);
    	gl.glTexCoord2f(0.0f, 1.0f);
    	gl.glVertex3f(-576.0f, 896.0f, 0.0f);
    	gl.glTexCoord2f(1.0f, 1.0f);
    	gl.glVertex3f(576.0f, 896.0f, 0.0f);
    	gl.glTexCoord2f(1.0f, 0.0f);
    	gl.glVertex3f(576.0f, -128.0f, 0.0f);
    	gl.glEnd();
    	
    	// Disable textures
    	gl.glDisable(GL.GL_TEXTURE_2D);
    	
    	gl.glPopMatrix();
 	}
 	
 	// Function to draw basketball hoop
 	public void drawHoop(final GL2 gl)
 	{
 		gl.glPushMatrix();

 		gl.glTranslatef(0.0f, 810.0f, 0.0f);	
 		
 		// Enable and bind base and pole texture
 	 	gl.glEnable(GL.GL_TEXTURE_2D);
 	 	poleTex.bind(gl);
 	 	gl.glColor3f(1.0f, 1.0f, 1.0f);
 		
 	 	// Construct textured hoop base
 		GLUquadric base = glu.gluNewQuadric();
 	 	glu.gluQuadricDrawStyle(base, glu.GLU_FILL);
 	 	glu.gluQuadricTexture(base, true);
 	 	glu.gluQuadricNormals(base, glu.GLU_SMOOTH);
 		glu.gluCylinder(base, 40.0f, 0.0f, 2.0f, 16, 16);
 		
 		// Construct textured hoop floor
 	 	GLUquadric pole = glu.gluNewQuadric();
 	 	glu.gluQuadricDrawStyle(pole, glu.GLU_FILL);
 	 	glu.gluQuadricTexture(pole, true);
 	 	glu.gluQuadricNormals(pole, glu.GLU_SMOOTH);
 	 	glu.gluCylinder(pole, 6.0f, 6.0f, 326.0f, 16, 16);
 	 	
 	 	// Disable textures
 	 	gl.glDisable(GL.GL_TEXTURE_2D);
 		
 		gl.glPopMatrix();
 		
 		gl.glPushMatrix();
 		
 		gl.glTranslatef(0.0f, 802.0f, 308.0f);
 		
 		// Construct textured hoop backboard...
 		// Back side
 		gl.glBegin(gl.GL_QUADS);
 		gl.glColor3f(0.1f, 0.1f, 0.1f);
 		gl.glVertex3f(-106.0f, 8.0f, 0.0f);
 		gl.glVertex3f(-106.0f, 8.0f, 120.0f);
 		gl.glVertex3f(106.0f, 8.0f, 120.0f);
 		gl.glVertex3f(106.0f, 8.0f, 0.0f);
 		gl.glEnd();
 	
 		// Enable and bind backboard texture
 		gl.glEnable(GL.GL_TEXTURE_2D);
 	 	backboardTex.bind(gl);
 		
 	 	// Front side
 		gl.glBegin(gl.GL_QUADS);
 		gl.glColor3f(1.0f, 1.0f, 1.0f);
 		gl.glTexCoord2f(0.0f, 0.0f);
 		gl.glVertex3f(-106.0f, 0.0f, 0.0f);
 		gl.glTexCoord2f(0.0f, 1.0f);
 		gl.glVertex3f(-106.0f, 0.0f, 120.0f);
 		gl.glTexCoord2f(1.0f, 1.0f);
 		gl.glVertex3f(106.0f, 0.0f, 120.0f);
 		gl.glTexCoord2f(1.0f, 0.0f);
 		gl.glVertex3f(106.0f, 0.0f, 0.0f);
 		gl.glEnd();
 		
 		// Disable textures
 		gl.glDisable(GL.GL_TEXTURE_2D);
 		
 		// Right side
 		gl.glBegin(gl.GL_QUADS);
 		gl.glColor3f(0.1f, 0.1f, 0.1f);
 		gl.glVertex3f(106.0f, 0.0f, 0.0f);
 		gl.glVertex3f(106.0f, 0.0f, 120.0f);
 		gl.glVertex3f(106.0f, 8.0f, 120.0f);
 		gl.glVertex3f(106.0f, 8.0f, 0.0f);
 		gl.glEnd();
 		
 		// Left side
 		gl.glBegin(gl.GL_QUADS);
 		gl.glColor3f(0.1f, 0.1f, 0.1f);
 		gl.glVertex3f(-106.0f, 0.0f, 0.0f);
 		gl.glVertex3f(-106.0f, 0.0f, 120.0f);
 		gl.glVertex3f(-106.0f, 8.0f, 120.0f);
 		gl.glVertex3f(-106.0f, 8.0f, 0.0f);
 		gl.glEnd();
 		
 		// Top side
 		gl.glBegin(gl.GL_QUADS);
 		gl.glColor3f(0.1f, 0.1f, 0.1f);
 		gl.glVertex3f(-106.0f, 0.0f, 120.0f);
 		gl.glVertex3f(-106.0f, 8.0f, 120.0f);
 		gl.glVertex3f(106.0f, 8.0f, 120.0f);
 		gl.glVertex3f(106.0f, 0.0f, 120.0f);
 		gl.glEnd();
 		
 		// Bottom side
 		gl.glBegin(gl.GL_QUADS);
 		gl.glColor3f(0.1f, 0.1f, 0.1f);
 		gl.glVertex3f(-106.0f, 0.0f, 0.0f);
 		gl.glVertex3f(-106.0f, 8.0f, 0.0f);
 		gl.glVertex3f(106.0f, 8.0f, 0.0f);
 		gl.glVertex3f(106.0f, 0.0f, 0.0f);
 		gl.glEnd();
 		
 		// Enable and bind frame and rim texture
 		gl.glEnable(GL.GL_TEXTURE_2D);
 	 	frameTex.bind(gl);
    
 		// Construct textured hoop frame
    	gl.glBegin(gl.GL_QUADS);
    	gl.glColor3f(1.0f, 1.0f, 1.0f);
    	gl.glTexCoord2f(0.0f, 0.0f);
 		gl.glVertex3f(-10.0f, 0.0f, 0.0f);
 		gl.glTexCoord2f(0.0f, 1.0f);
 		gl.glVertex3f(-10.0f, 0.0f, 24.0f);
 		gl.glTexCoord2f(1.0f, 1.0f);
 		gl.glVertex3f(10.0f, 0.0f, 24.0f);
 		gl.glTexCoord2f(1.0f, 0.0f);
 		gl.glVertex3f(10.0f, 0.0f, 0.0f);
 		gl.glEnd();
    	
 		gl.glTranslatef(0.0f, -32.0f, 16.0f);
 		
 		// Construct textured hoop rim
 		glut.glutSolidTorus(2.0f, 34.0f, 16, 16);
 		
 		// Disable textures
 		gl.glDisable(GL.GL_TEXTURE_2D);
 		
 		gl.glPopMatrix();
 	}
 	
 	// Function to draw basketball
 	public void drawBall(final GL2 gl)
 	{
 		gl.glPushMatrix();
 		
 		gl.glTranslatef(x0 + x, y0 + y, z0 + z);
 		
 		// x rotation given target z
 		float xOA = (tz - 128.0f) / 800.0f;
		float xTheta = (float) Math.toDegrees(Math.atan(xOA));
		
		// z rotation given initial x and target x
		float zOA = (x0 - tx) / 800.0f;
		float zTheta = (float) Math.toDegrees(Math.atan(zOA));
 		
 		gl.glRotatef(xTheta, 1.0f, 0.0f, 0.0f);
 		gl.glRotatef(zTheta, 0.0f, 0.0f, 1.0f);
 		
 		// Rotate in x direction given time and force level
 		gl.glRotatef(time * forceLevel, -1.0f, 0.0f, 0.0f);

 		// Enable and bind ball texture
 	 	gl.glEnable(GL.GL_TEXTURE_2D);
 	 	ballTex.bind(gl);

 	 	// Construct textured ball
 	 	gl.glColor3f(1.0f, 1.0f, 1.0f);
 	 	GLUquadric sphere = glu.gluNewQuadric();
 	 	glu.gluQuadricDrawStyle(sphere, glu.GLU_FILL);
 	 	glu.gluQuadricTexture(sphere, true);
 	 	glu.gluQuadricNormals(sphere, glu.GLU_SMOOTH);
 	 	glu.gluSphere(sphere, 24.0f, 16, 16);

 	 	// Disable textures
 		gl.glDisable(GL.GL_TEXTURE_2D);
 		
 		gl.glPopMatrix();
 	}
 	
 	// Function to draw target
 	public void drawTarget(final GL2 gl)
 	{
 		gl.glPushMatrix();

    	gl.glTranslatef(tx, 800.0f, tz);
    	
    	gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
    	
    	gl.glColor3f(1.0f, 0.0f, 0.0f);
    	
    	// Construct target circle
    	glut.glutSolidTorus(2.0f, 24.0f, 16, 16);
    	
    	// Construct target lines
    	gl.glLineWidth(2.0f);
    	gl.glBegin(gl.GL_LINES);
    	gl.glVertex3f(0.0f, -24.0f, 0.0f);
    	gl.glVertex3f(0.0f, 24.0f, 0.0f);
    	gl.glVertex3f(-24.0f, 0.0f, 0.0f);
    	gl.glVertex3f(24.0f, 0.0f, 0.0f);
    	gl.glEnd();
    	gl.glLineWidth(1.0f);
    	
    	gl.glPopMatrix();
 	}
 	
 	// Function to draw 2D force gauge
 	public void draw2DGauge(final GL2 gl)
 	{
 		gl.glPushMatrix();
 		
 		// Change matrix mode to draw in orthogonal view
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();
    	glu.gluOrtho2D(0, windowWidth, 0, windowHeight);
		
        // Update matrix mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	
    	gl.glTranslatef(16.0f, 16.0f, 0.0f);
    	
    	// Draw gauge fill from force level
    	gl.glBegin(gl.GL_QUADS);
    	gl.glColor3f(0.0f, 1.0f, 0.0f);
    	gl.glVertex2f(0.0f, 0.0f);
    	gl.glVertex2f(0.0f, forceLevel * 2.0f);
    	gl.glVertex2f(32.0f, forceLevel * 2.0f);
    	gl.glVertex2f(32.0f, 0.0f);
    	gl.glEnd();
    	
    	// Draw gauge outline
    	gl.glLineWidth(2.0f);
    	gl.glBegin(gl.GL_LINE_LOOP);
    	gl.glColor3f(0.0f, 0.5f, 0.0f);
    	gl.glVertex2f(0.0f, 0.0f);
    	gl.glVertex2f(0.0f, 200.0f);
    	gl.glVertex2f(32.0f, 200.0f);
    	gl.glVertex2f(32.0f, 0.0f);
    	gl.glEnd();
    	gl.glLineWidth(1.0f);
    	
    	// Set matrix mode back to projection for camera view
    	gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, aspect, 1, 100000.0);
        
        // Update matrix mode
     	gl.glMatrixMode(GL2.GL_MODELVIEW);
     	gl.glLoadIdentity();
    	
    	gl.glPopMatrix();
 	}
 	
 	// Function to draw 2D replay button
 	public void draw2DReplayButton(final GL2 gl)
 	{
 		gl.glPushMatrix();
 		
 		// Change matrix mode to draw in orthogonal view
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();
        glu.gluOrtho2D(0, windowWidth, 0, windowHeight);
		
        // Update matrix mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	
    	gl.glTranslatef(windowWidth - 208.0f, 16.0f, 0.0f);
    	
    	// Enable and bind replay button texture
    	gl.glEnable(GL.GL_TEXTURE_2D);
    	replayButtonTex.bind(gl);
    	
    	// Construct textured replay button
    	gl.glBegin(gl.GL_QUADS);
    	gl.glColor3f(1.0f, 1.0f, 1.0f);
    	gl.glTexCoord2f(0.0f, 0.0f);
    	gl.glVertex2f(0.0f, 0.0f);
    	gl.glTexCoord2f(0.0f, 1.0f);
    	gl.glVertex2f(0.0f, 72.0f);
    	gl.glTexCoord2f(1.0f, 1.0f);
    	gl.glVertex2f(192.0f, 72.0f);
    	gl.glTexCoord2f(1.0f, 0.0f);
    	gl.glVertex2f(192.0f, 0.0f);
    	gl.glEnd();
    	
    	// Disable textures
    	gl.glDisable(GL.GL_TEXTURE_2D);
 		
    	// Draw replay button outline
 		gl.glLineWidth(2.0f);
    	gl.glBegin(gl.GL_LINE_LOOP);
    	gl.glColor3f(0.0f, 0.5f, 0.0f);
    	gl.glVertex2f(0.0f, 0.0f);
    	gl.glVertex2f(0.0f, 72.0f);
    	gl.glVertex2f(192.0f, 72.0f);
    	gl.glVertex2f(192.0f, 0.0f);
    	gl.glEnd();
    	gl.glLineWidth(1.0f);
    	
    	// Set matrix mode back to projection for camera view
    	gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, aspect, 1, 100000.0);
        
        // Update matrix mode
     	gl.glMatrixMode(GL2.GL_MODELVIEW);
     	gl.glLoadIdentity();
    	
    	gl.glPopMatrix();
 	}
 	
 	// Function to draw 2D replay mode dialog
 	public void draw2DRMDialog(final GL2 gl)
 	{
 		gl.glPushMatrix();
 		
 		// Change matrix mode to draw in orthogonal view
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();
        glu.gluOrtho2D(0, windowWidth, 0, windowHeight);
		
        // Update matrix mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	
    	gl.glColor3f(0.0f, 1.0f, 0.0f);
 		
    	// Draw "REPLAY MODE" text using glut bitmap font
 		String replayBannerText = "REPLAY MODE";
    	gl.glRasterPos2f(0.5f * windowWidth - (replayBannerText.length() * 6.0f), windowHeight - 32.0f);
    	for (int c = 0; c < replayBannerText.length(); c++)
    	{
    		glut.glutBitmapCharacter(glut.BITMAP_HELVETICA_18, replayBannerText.charAt(c));
    	}
    	
    	gl.glColor3f(0.5f, 1.0f, 1.0f);
    	
    	// Draw "Replay speed: [speed]" using glut bitmap font
    	String speedStr = String.valueOf(speed);
    	String replaySpeedText = "Replay speed: " + speedStr + "x";
    	gl.glRasterPos2f(0.5f * windowWidth - (replaySpeedText.length() * 6.0f) + 24.0f, windowHeight - 54.0f);
    	for (int c = 0; c < replaySpeedText.length(); c++)
    	{
    		glut.glutBitmapCharacter(glut.BITMAP_HELVETICA_18, replaySpeedText.charAt(c));
    	}

    	// Set matrix mode back to projection for camera view
    	gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, aspect, 1, 100000.0);
        
        // Update matrix mode
     	gl.glMatrixMode(GL2.GL_MODELVIEW);
     	gl.glLoadIdentity();
    	
    	gl.glPopMatrix();
 	}
 	
 	// Function to load desired audio stream
 	public void loadAudioStream(int num)
 	{
 		// Load winning sound
 		if (num == 0)
 		{
 			try
 		    {	
 		 		winFile = new FileInputStream("win.wav");
 				winStream = new AudioStream(winFile);
 		    }
 			catch (Exception ex)
 		    {
 		    	ex.printStackTrace();
 		    }
 		}
 		// Load missing sound
 		else if (num == 1)
 		{
 			try
 		    {	
 		 		missFile = new FileInputStream("miss.wav");
 				missStream = new AudioStream(missFile);
 		    }
 			catch (Exception ex)
 		    {
 		    	ex.printStackTrace();
 		    }
 		}
 	}
 	
 	// Function to reset variables for new game
 	public void reset()
 	{
 		time = 0.0f;
		hitTime = 0.0f;
		speed = 1.0f;
		vx = 0.0f;
		vz = 0.0f;
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		forceLevel = 0.0f;
		replayMode = false;
		showGauge = false;
		tries = 0.0f;
		hitFloor = false;
		hitBoard = false;
		hitRim = false;
		ballMoving = false;
		canThrow = true;
		canHitBoard = true;
		scored = false;
		missed = false;
		askForReplay = false;
		showTarget = true;
		showBall = true;
 	}
 
	// Function for display changing
    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {};
	
    // Initialization function
    public void init(GLAutoDrawable gLDrawable)
    {
    	GL2 gl = gLDrawable.getGL().getGL2(); // Retrieve GL2
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Set background color
	    gl.glClearDepth(1.0f); // Clear depth buffer
	    
	    // Enable depth testing
	    gl.glEnable(GL.GL_DEPTH_TEST);
	    gl.glDepthFunc(GL.GL_LEQUAL);
	    
	    // Load and enable textures, and set texture parameters
	    try
	    {
	    	// Load textures from files
	    	wallTex = TextureIO.newTexture(new File("wall.png"), false);
	    	ceilingTex = TextureIO.newTexture(new File("ceiling.png"), false);
	    	deepFloorTex = TextureIO.newTexture(new File("deepFloor.png"), false);
	    	floorTex = TextureIO.newTexture(new File("floor.png"), false);
	    	poleTex = TextureIO.newTexture(new File("pole.png"), false);
	    	backboardTex = TextureIO.newTexture(new File("backboard.png"), false);
	    	frameTex = TextureIO.newTexture(new File("frame.png"), false);
	    	ballTex = TextureIO.newTexture(new File("ball.png"), false);
	    	replayButtonTex = TextureIO.newTexture(new File("replayButton.png"), false);
	    	
	    	// Enable textures
	    	gl.glEnable(GL.GL_TEXTURE_2D);
	    	
	    	// Set texture parameters
	    	gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
	 	 	gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
	    }
    }
	
    // Reshaping window function
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height)
    {
    	// Update window width and height
    	windowWidth = width;
    	windowHeight = height;
    	
        final GL2 gl = gLDrawable.getGL().getGL2(); // Retrieve GL2
       
        // Avoid dividing by zero
        if (height == 0){
        	height = 1;
        }
       
        aspect = (float)width / (float)height; // Calculate aspect ratio of window
     
        // Set up viewport and projection
        gl.glViewport(0, 0, width, height); 
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, aspect, 1, 100000.0);
    }
    
    @Override
    // Display function
	public void display(GLAutoDrawable gLDrawable)
    {
	    final GL2 gl = gLDrawable.getGL().getGL2(); // Retrieve GL2
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set background color
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear buffers
		
		// Update matrix mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		// Update camera view and variables given game mode (first-person or replay mode)
		if (replayMode == false)
		{
			replayMode = false;
		
			tempX0 = 0.0f;
			
			glu.gluLookAt(1.25f * x0, -256.0f, z0+108.0f, 0.0f, 896.0f, z0+108.0f, 0.0f, 0.0f, 1.0f); // First-person view
			
			camx = 0.0f;
			camy = -256.0f;
			camz = 236.0f;
			camxTo = 0.0f;
			camyTo = 384.0f;
			camzTo = 236.0f;
		}
		else
		{
			tempX0 = -x0;
			
			glu.gluLookAt(camx, camy, camz, camxTo, camyTo, camzTo, 0.0f, 0.0f, 1.0f); // Replay mode view
			draw2DRMDialog(gl); // Draw replay mode dialog
		}
		
    	// Draw world objects
		drawSkybox(gl);
		drawFloor(gl);
		drawHoop(gl);
		
		if (showBall == true)
		{
			drawBall(gl);
		}
		
		if (showTarget == true)
		{
			drawTarget(gl);
		}
		
		// Draw 2D gauge and control gauge
		if (showGauge == true)
		{
			draw2DGauge(gl);
			if (canThrow == true)
			{
				// Increase force level for two chances
				if (tries == 0.0f || tries == 1.0f)
				{
					if (forceLevel < 100.0f)
					{
						forceLevel += 2.0f;
					}
					else
					{
						tries += 0.5f;
					}
				}
				// Lower force level for first chance
				else if (tries == 0.5f)
				{
					if (forceLevel > 2.0f)
					{
						forceLevel -= 2.0f;
					}
					else
					{
						tries += 0.5f;
					}
				}
				// Lower force level and restart if missed second chance
				else if (tries == 1.5f)
				{
					if (forceLevel > 0.0f)
					{
						forceLevel -= 2.0f;
					}
					else
					{
						forceLevel = 0.0f;
						tries = 0.0f;
						showGauge = false;
					}
				}
			}
		}
		
		// Determine projectile motion pattern of ball given collisions, if moving
		if (ballMoving == true)
		{	
			if (hitFloor == false)
			{
				time += 0.2f * speed;

				vx = ((tx - x0) / 8.0f) * (forceLevel / 100.0f);
				x = vx * time;
			}

			if (hitBoard == false)
			{	
				y = forceLevel * time;
			}
			else if (hitBoard == true && hitFloor == false){
				y -= (forceLevel/30.0f) * (speed);
			}
			
			if (hitRim == true && hitFloor == false)
			{
				hitTime += 0.2f * speed;
				vz = (tz - z0 + 313.6f) / 16.0f;
				z = 220.0f + (vz * hitTime) + (-4.9f * hitTime * hitTime);
			}
			else if (hitRim == false && hitFloor == false)
			{
				vz = (tz - z0 + 313.6f) / 8.0f;
				z = (vz * time) + (-4.9f * time * time);
			}
		}
		
		// Check for floor collision to potentially enter replay mode
		if (hitFloor == true)
		{
			showBall = false;
			if (replayMode == false)
			{
				if (askForReplay == true)
				{
					draw2DReplayButton(gl);
				}
				// Start a new game
				else
				{
					reset();
				}
			}
			// Enter replay mode
			else{
				showBall = true;
				showTarget = false;
				time = 0.0f;
				hitTime = 0.0f;
				vx = 0.0f;
				vz = 0.0f;
				x = 0.0f;
				y = 0.0f;
				z = 0.0f;
				hitFloor = false;
				hitBoard = false;
				hitRim = false;
				canHitBoard = true;
			}
		}
			
		// Collision potential for backboard and pole (before detection)
		if (x > (-124.0f - x0) && x < (124.0f - x0))
		{
			if (z <= 348.0f && z >= 180.0f && y < 778.0f)
			{
				canHitBoard = true;
			}
			else if (z > 348.0f && y < 778.0f)
			{
				canHitBoard = false;
			}
			else if (x >= -27.0f - x0 && x <= 27.0f - x0 && z < 180.0f)
			{
				canHitBoard = true;
			}
		}
		else
		{
			canHitBoard = false;
		}
		
		// Collision detection for backboard and pole
		if (canHitBoard == true && y >= 778.0f)
		{
			if (x >= (-124.0f - x0) && x <= (124.0f - x0) && z <= 324.0f && z >= 180.0f)
			{
				hitBoard = true;
			}
			else if (x >= -27.0f - x0 && x <= 27.0f - x0 && z < 180.0f)
			{
				hitBoard = true;
			}
		}
		
		// Collision detection for rim
		if (z <= 220.0f)
		{
			// Score if basket is made
			if (x >= -16.0f - x0 && x <= 16.0f - x0 && z >= 196.0f && y >= 753.0f
					&& y <= 803.0f && hitRim == false && tz > 348.0f)
			{
				scored = true;
			}
			// Miss if rim side is hit
			else if (((x >= -57.0f - x0 && x < -16.0f - x0) || (x > 16.0f - x0 && x <= 57.0f - x0))
					&& z >= 196.0f && y >= 753.0f && scored == false)
			{
				hitRim = true;
				missed = true;
			}
			// Miss if rim front is hit
			else if (x >= -16.0f - x0 && x <= 16.0f - x0 && z >= 196.0f && y <= 753.0f && y >= 736.0f && scored == false)
			{
				hitRim = true;
				missed = true;
			}
		}
		
		// Allow for checking of floor collision
		if (z <= -96.0f || y >= 872.0f)
		{
			// Miss if did not score or rim wasn't hit
			if (missed == false && scored == false)
			{
				missed = true;
			}
			
			hitFloor = true;
			askForReplay = true;
		}
		
		// Set max bounds for initial ball x
		if (x0 <= -500.0f)
		{
			x0 = -500.0f;
		}
		else if (x0 >= 500.0f)
		{
			x0 = 500.0f;
		}
		
		// Play sounds if not in replay mode
		if (replayMode == false)
		{
			// Load winning sound (so that the sound can play more than once)
			if (scored == false)
			{
				loadAudioStream(0);
			}
			// Play winning sound
			else
			{
				AudioPlayer.player.start(winStream);
			}
			
			// Load missing sound (so that the sound can play more than once)
			if (missed == false)
			{
				loadAudioStream(1);
			}
			// Play missing sound
			else
			{
				AudioPlayer.player.start(missStream);
			}
		}
    	
    	gl.glFlush();
	}
    
	@Override
   	// Dispose function
   	public void dispose(GLAutoDrawable gLDrawable)
   	{
   		// TODO Auto-generated method stub
   	}

	@Override
	// Function checking for key typing
	public void keyTyped(KeyEvent e)
	{	
		// Allow for aim adjustment or player movement if not in replay mode
		if (replayMode == false)
		{
			// Adjust aim or move player if ball is not being thrown
			if (showGauge == false)
			{
				// Shift aim left if 'a' is held
				if (e.getKeyChar() == 'a')
				{
					tx -= 4.0f;
				}
				// Shift aim right if 'd' is held
				else if (e.getKeyChar() == 'd')
				{
					tx += 4.0f;
				}
				
				// Shift aim up if 'w' is held
				if (e.getKeyChar() == 'w')
				{
					tz += 4.0f;
				}
				// Shift aim down if 's' is held
				else if (e.getKeyChar() == 's')
				{
					tz -= 4.0f;
				}
				
				// Move player left if 'o' is held
				if (e.getKeyChar() == 'o')
				{
					x0 -= 4.0f;
				}
				// Move player right if 'p' is held
				else if (e.getKeyChar() == 'p')
				{
					x0 += 4.0f;
				}
			}
		}
	}
	
	@Override
	// Function checking for key presses
	public void keyPressed(KeyEvent e)
	{
		// Check if space bar is pressed
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			// Display force gauge, if force gauge is hidden and floor hasn't been hit
			if (showGauge == false && hitFloor == false)
			{
				showGauge = true;
				forceLevel = 0.0f;
				tries = 0.0f;
			}
			
			// Throw ball if gauge is visible, ball can be thrown, and floor hasn't been hit
			else if (showGauge == true && canThrow == true && hitFloor == false)
			{
				canThrow = false;
				ballMoving = true;
			}
		}
		
		// Decrease game speed in replay mode
		if (e.getKeyCode() == '[' && replayMode == true)
		{
			if (speed == 1.0f)
			{
				speed = 0.25f;
			}
			else if (speed == 0.25f)
			{
				speed = 0.1f;
			}
		}
		// Increase game speed in replay mode
		else if (e.getKeyCode() == ']' && replayMode == true)
		{
			if (speed == 0.1f)
			{
				speed = 0.25f;
			}
			else if (speed == 0.25f)
			{
				speed = 1.0f;
			}
		}
	}
	
	@Override
	// Function checking for key releases
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub	
	}

	@Override
	// Function checking if mouse if dragged
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	// Function checking if mouse if moved
	public void mouseMoved(MouseEvent e)
	{
		// Allow for camera movement with mouse, if in replay mode
		if (replayMode == true)
		{
			float XX = (e.getX() - windowWidth*0.5f)*45.0f/windowWidth;
			float YY = -(e.getY() - windowHeight*0.5f)*45.0f/windowHeight;
			
			float angle = (float) Math.toRadians(XX * 8.0f);

			// Camera x and y determined by mouse x (circles the court)
			camx = 800.0f * (float) Math.sin(angle);
			camy = (-800.0f * (float) Math.cos(angle)) + 544.0f;
			
			// Camera z and zTo determined by mouse y (pans up and down)
			camz = 263.885f * (float) Math.exp(0.0800358f * YY);
			camzTo = 236.0f - (0.466173f * YY * YY);
		}
	}

	@Override
	// Function checking if mouse button is clicked
	public void mouseClicked(MouseEvent e)
	{
		float XX = e.getX();
		float YY = windowHeight - e.getY();

		if (askForReplay == true && replayMode == false)
		{
			if (XX >= windowWidth - 208.0f && XX <= windowWidth - 16.0f && YY >= 16.0f && YY <= 88.0f)
			{
				replayMode = true;
			}
			askForReplay = false;
		}
		else if (replayMode == true)
		{
			reset();
		}
		
	}

	@Override
	// Function checking is mouse button is pressed
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	// Function checking if mouse button is released
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	// Function checking if mouse is entered
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub	
	}

	@Override
	// Function checking if mouse is exited
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub	
	}
}