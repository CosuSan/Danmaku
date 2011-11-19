package fr.larez.danmaku;

import org.lwjgl.util.vector.Vector2f;

import fr.larez.danmaku.utils.Rectanglef;

/**
 * An Entity, i.e. any kind of object.
 */
public abstract class Entity {

    public static final long SHIP = 1; // Hit by enemy stuff
    public static final long ENEMY = 2; // Hit by our stuff
    public static final long OWN_BULLET = 4; // Hits the enemies
    public static final long ENEMY_BULLET = 8; // Hits us

    protected boolean m_Alive = true;
    protected float m_PosX, m_PosY;

    /**
     * Execute one simulation step for this Entity.
     * @return false if the Entity is destroyed.
     */
    public abstract void update(long simuTime);

    /**
     * Displays this Entity on the screen.
     */
    public abstract void render();

    /**
     * Whether this Entity is still alive or has/should be removed.
     */
    public final boolean alive()
    {
        return m_Alive;
    }

    /**
     * @return The bounding rectangle of this Entity, used for collision
     * detection.
     */
    public abstract Rectanglef boundingBox();

    /**
     * @return The position of this Entity (for instance, the center of the
     * bounding rectangle).
     */
    public final Vector2f position()
    {
        return new Vector2f(m_PosX, m_PosY);
    }

    /**
     * @return The type of this entity (with respect to the player).
     */
    public abstract long type();

}
