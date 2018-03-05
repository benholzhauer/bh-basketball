// Virtual Basketball (InitGL.java)
// Author: Ben Holzhauer
// Date: 12/12/2016
// Class: CS 335, Section 1

package Virtual_Basketball; // Define package

//Import Java components
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Import JogAmp components
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

// Define public class for initializing OpenGL
public class InitGL extends Frame
{
	// Define static animator
	static Animator anim = null;
	
	// Initialize JOGL
	private void InitJOGL()
	{
		// Create set of GL capabilities
	    GLCapabilities caps = new GLCapabilities(null);
	    caps.setDoubleBuffered(true);
	    caps.setHardwareAccelerated(true);
	    
	    // Create canvas for OpenGL rendering
	    GLCanvas canvas = new GLCanvas(caps); 
        add(canvas);
        
        // Create event listener for GL events, key typing, and mouse clicking
        JoglEventListener jgl = new JoglEventListener();
        canvas.addGLEventListener(jgl); 
        canvas.addKeyListener(jgl);
        canvas.addMouseListener(jgl);
        canvas.addMouseMotionListener(jgl);
        
        // Begin animation process
        anim = new Animator(canvas);
        anim.start();
	}
	
	// Initialize OpenGL function
    public InitGL()
    {
    	// Set window title
        super("Virtual Basketball");

        // Set layout manager
        setLayout(new BorderLayout());
        
        // Add window listener
        addWindowListener(new WindowAdapter()
        {
        	// Close window with exit button
            public void windowClosing(WindowEvent e)
            {
            	// Exit the program
                System.exit(0);
            }
        });
        
        // Set window size
        setSize(800, 600);
        
        // Set window position
        setLocation(283, 84);

        // Show window
        setVisible(true);

        // Initialize JOGL
        InitJOGL();
    }

    // Main function
    public static void main(String[] args)
    {
    	// Create and display OpenGL window
    	InitGL window = new InitGL();
        window.setVisible(true);
    }
}

// Class for window closing
class myWindow extends WindowAdapter
{
	public void windowClosing(WindowEvent e)
	{
		System.exit(0);
	}
}