package fr.larez.danmaku;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
    public abstract Rectangle2D boundingBox();

    /**
     * @return The position of this Entity (for instance, the center of the
     * bounding rectangle).
     */
    public final Point2D position()
    {
        return new Point2D.Float(m_PosX, m_PosY);
    }

    /**
     * @return The type of this entity (with respect to the player).
     */
    public abstract long type();

}
