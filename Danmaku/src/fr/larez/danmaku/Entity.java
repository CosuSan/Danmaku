package fr.larez.danmaku;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An Entity, i.e. any kind of object.
 */
public interface Entity {

    /**
     * Execute one simulation step for this Entity.
     * @return false if the Entity is destroyed.
     */
    boolean update(long simuTime);

    /**
     * Displays this Entity on the screen.
     */
    void render();

    /**
     * @return The bounding rectangle of this Entity, used for collision
     * detection.
     */
    Rectangle2D boundingBox();

    /**
     * @return The position of this Entity (for instance, the center of the
     * bounding rectangle).
     */
    Point2D position();

    enum EType {
        SHIP,
        ENEMY,
        OWN_BULLET,
        ENEMY_BULLET
    }

    /**
     * @return The type of this entity (with respect to the player).
     */
    EType type();

}
