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
import org.newdawn.slick.Color;

import fr.larez.danmaku.utils.DrawingUtils;
import fr.larez.danmaku.utils.MathUtils;

/**
 * Main class.
 *
 * The application's main class, containing the main execution logic and the
 * main() method.
 */
public class Application {

    /**
     * The fixed time step of the simulation.
     *
     * 20ms = 50 frames per second.
     */
    public static final long SIMULATION_STEP = 20;

    /**
     * FPS limitation.
     *
     * 100 images per second.
     */
    public static final int RENDER_FPS_LIMIT = 60;

    /**
     * Invulnerability time after a hit.
     */
    public static final int INVULN_ON_HIT = 4000;

    public static final float FIELD_WIDTH = 500.0f;
    public static final float FIELD_HEIGHT = 600.0f;

    private final List<Entity> m_Entities = new LinkedList<Entity>();
    private final List<Entity> m_NewEntities = new LinkedList<Entity>();
    private Ship m_Ship;

    private static Application instance = null;

    private long m_SimuTime;

    private long m_LastHit;
    private boolean m_ShipDies = false;

    private int m_NbLives = 9;
    private int m_Score = 0;

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

    public void initialize()
    {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Initialize OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, 800.0, 600.0, 0.0, 1.0, -1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);

        // Load the textures
        TextureManager.loadAll();

        // Load the font
        DrawingUtils.loadFont();
    }

    void start()
    {
        initialize();

        // TODO : Some kind of menu (or at least title screen,
        // i.e. fixed image and "press enter")

        Level[] levels = {new fr.larez.danmaku.level1.Level1()};
        for(Level level : levels)
        {
            play(level);
            if(m_NbLives <= 0)
                break;
        }

        // Game over screen
        gameover();

        Display.destroy();
    }

    void play(Level level)
    {
        m_Entities.clear();

        // FPS counter
        Display.setTitle("Danmaku");
        long lastFPSUpdate = getTime();
        long frames = 0;

        // Simulation timing
        long lastFrameTime = getTime();
        m_SimuTime = 0;

        m_LastHit = -99999;

        // Setup the ship
        m_Entities.add(m_Ship = new Ship());

        boolean paused = false;

        while (!level.finished() && m_NbLives > 0)
        {
            long now = getTime();

            if(m_ShipDies)
            {
                m_NbLives--;
                m_LastHit = m_SimuTime;
                instance.m_Entities.remove(m_Ship);
                m_Entities.add(m_Ship = new Ship());
                m_ShipDies = false;
            }

            Keyboard.poll();

            // If the computer can't keep up we should pause
            if(!paused && now > lastFrameTime + 20 * SIMULATION_STEP)
            {
                System.err.println("Warning: can't keep up! delta=" + (now - lastFrameTime) + "ms");
                paused = true;
            }

            // Simulation
            while(!paused && now > lastFrameTime + SIMULATION_STEP)
            {
                lastFrameTime += SIMULATION_STEP;
                m_SimuTime += SIMULATION_STEP;

                level.update(m_SimuTime);

                for(Iterator<Entity> it = m_Entities.iterator(); it.hasNext();)
                {
                    Entity entity = it.next();
                    entity.update(m_SimuTime);
                    if(!entity.alive())
                        it.remove();
                }
                m_Entities.addAll(m_NewEntities);
                m_NewEntities.clear();
            }

            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            DrawingUtils.translate(50.0f, 0.0f);

            // Black background for the field
            GL11.glColor3f(0.0f, 0.0f, 0.0f);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            DrawingUtils.drawRect(0.0f, 0.0f, FIELD_WIDTH, FIELD_HEIGHT);

            // Level background
            level.renderBackground(m_SimuTime);

            // Entities
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            for(Entity entity : m_Entities)
                entity.render();
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            // White screen on death
            if(m_LastHit + 1000 > m_SimuTime)
            {
                float alpha = 1.0f - MathUtils.square((m_SimuTime - m_LastHit)*1.0E-3f);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
                DrawingUtils.drawRect(0.0f, 0.0f, FIELD_WIDTH, FIELD_HEIGHT);
            }

            // Level foreground
            level.renderForeground(m_SimuTime);

            // Pause screen
            if(paused)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(0.8f, 0.4f, 0.4f, 0.7f);
                DrawingUtils.drawRect(0.0f, 0.0f, FIELD_WIDTH, FIELD_HEIGHT);
                DrawingUtils.drawText(100.0f, 100.0f, "PAUSED - press enter", new Color(0.6f, 0.0f, 0.8f, 1.0f));
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                if(Keyboard.isKeyDown(Keyboard.KEY_RETURN))
                {
                    paused = false;
                    lastFrameTime = now;
                }
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_PAUSE))
                paused = true;

            DrawingUtils.reset();

            DrawingUtils.drawText(570.0f, 100.0f, "Score: " + m_Score);
            DrawingUtils.drawText(570.0f, 150.0f, "Lives: " + m_NbLives);
            DrawingUtils.drawText(570.0f, 300.0f, "Sim time: ");
            DrawingUtils.drawText(570.0f, 350.0f, String.valueOf(m_SimuTime));

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

            // Emergency exit
            if(Display.isCloseRequested())
            {
                Display.destroy();
                System.exit(0);
            }
        }
    }

    private void gameover()
    {
        while (!Display.isCloseRequested())
        {
            Keyboard.poll();

            if(Keyboard.isKeyDown(Keyboard.KEY_RETURN))
                break;

            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            DrawingUtils.drawText(300.0f, 200.0f, "GAME OVER !");
            DrawingUtils.drawText(250.0f, 350.0f, "Your score: " + m_Score);
            DrawingUtils.drawText(250.0f, 450.0f, "Press enter to exit");

            Display.update();

            // FPS limit
            Display.sync(25);
        }
    }

    public static long simulatedTime()
    {
        return instance.m_SimuTime;
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

    public static Entity collide(Entity entity, long types)
    {
        for(Entity other : instance.m_Entities)
            if((other.type() & types) != 0 && other.boundingBox().intersects(entity.boundingBox()))
                return other;
        return null;
    }

    public static long lastHit()
    {
        return instance.m_LastHit;
    }

    public static void shipDies()
    {
        instance.m_ShipDies = true;
    }

    public static void gainPoints(int points)
    {
        if(points >= 0 || points + instance.m_Score >= 0)
            instance.m_Score += points;
    }

    public static void main(String[] argv)
    {
        Application app = new Application();
        app.start();
    }
}
