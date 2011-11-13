package fr.larez.danmaku;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import fr.larez.danmaku.utils.DrawingUtils;

/**
 * Main class.
 *
 * The application's main class, containing the main execution logic and the
 * main() method.
 */
public class Application {

    /**
     * The fixed timestep of the simulation.
     *
     * 20ms = 50 frames per second.
     */
    public static final long SIMULATION_STEP = 20;

    /**
     * FPS limitation.
     *
     * 100 images per second.
     */
    public static final int RENDER_FPS_LIMIT = 100;

    public static final float FIELD_WIDTH = 500.f;
    public static final float FIELD_HEIGHT = 600.f;

    private final List<Entity> m_Entities = new LinkedList<Entity>();
    private final List<Entity> m_NewEntities = new LinkedList<Entity>();
    private Ship m_Ship;

    private static Application instance = null;

    Application()
    {
        if(instance != null)
            throw new RuntimeException("Second Application created!");
        instance = this;
    }

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

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glClearColor(0.f, 0.f, 0.2f, 1.f);

        // Load the textures
        TextureManager.loadAll();

        // Setup the ship
        m_Entities.add(m_Ship = new Ship(FIELD_WIDTH*.5f, 550.f));

        // FIXME
        // A target for debugging purposes
        m_Entities.add(new DebugEntity(FIELD_WIDTH*0.8f, 100.f, Entity.EType.ENEMY));

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
                    if(!entity.update(simuTime))
                        it.remove();
                }
                for(Entity e : m_NewEntities)
                    m_Entities.add(e);
                m_NewEntities.clear();
            }

            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            DrawingUtils.translate(50.f, 0.f);

            // Black background for the field
            GL11.glColor3f(0.f,0.f,0.f);
            DrawingUtils.drawRect(0.f, 0.f, FIELD_WIDTH, FIELD_HEIGHT);

            GL11.glColor3f(1.f, 1.f, 1.f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            for(Entity entity : m_Entities)
                entity.render();
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            DrawingUtils.reset();

            Display.update();

            // FPS limit
            if(RENDER_FPS_LIMIT != 0)
                Display.sync(RENDER_FPS_LIMIT);

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

    public static Collection<Entity> entities()
    {
        return instance.m_Entities;
    }

    public static void addEntity(Entity entity)
    {
        instance.m_NewEntities.add(entity);
    }

    public static Entity getShip()
    {
        return instance.m_Ship;
    }

    public static void main(String[] argv)
    {
        Application displayExample = new Application();
        displayExample.start();
    }
}
