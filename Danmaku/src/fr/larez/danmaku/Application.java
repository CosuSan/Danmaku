package fr.larez.danmaku;

import java.util.Iterator;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Main class.
 *
 * The application's main class, containing the main execution logic and the
 * main() method.
 */
public class Application {

    private final LinkedList<Entity> m_Entities = new LinkedList<Entity>();

    /**
     * The fixed timestep of the simulation.
     *
     * 20ms = 50 frames per second.
     */
    public static final long SIMULATION_STEP = 20;

    /**
     * High-resolution timer.
     *
     * @return The system time in milliseconds
     */
    public long getTime()
    {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public void start()
    {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // FPS counter
        Display.setTitle("Danmaku");
        long lastFPSUpdate = getTime();
        long frames = 0;

        // Simulation timing
        long simuTime = getTime();

        // Initialize OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        while (!Display.isCloseRequested())
        {
            long now = getTime();

            Keyboard.poll();

            // Simulation
            while(now > simuTime + SIMULATION_STEP)
            {
                simuTime += SIMULATION_STEP;

                for(Iterator<Entity> it = m_Entities.iterator(); it.hasNext();)
                {
                    Entity entity = it.next();
                    if(!entity.update())
                        it.remove();
                }
            }

            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            // Draw a quad
            /*
            GL11.glColor3f(0.5f,0.5f,1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(100,100);
            GL11.glVertex2f(100+200,100);
            GL11.glVertex2f(100+200,100+200);
            GL11.glVertex2f(100,100+200);
            GL11.glEnd();
            */

            for(Entity entity : m_Entities)
                entity.render();

            Display.update();

            // FPS calculation
            frames++;
            if(now > lastFPSUpdate + 1000)
            {
                Display.setTitle("Danmaku - FPS: " + frames);
                lastFPSUpdate = now;
                frames = 0;
            }
        }

        Display.destroy();
    }

    public static void main(String[] argv)
    {
        Application displayExample = new Application();
        displayExample.start();
    }
}
