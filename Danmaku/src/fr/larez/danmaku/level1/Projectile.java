package fr.larez.danmaku.level1;

import java.awt.geom.Rectangle2D;

import fr.larez.danmaku.Application;
import fr.larez.danmaku.Entity;
import fr.larez.danmaku.TextureManager;
import fr.larez.danmaku.utils.DrawingUtils;

/**
 * Bullets that are fired from the ship and fly in a straight line.
 */
public class Projectile extends Entity {

    static final float HALFWIDTH = 8.f;
    static final float HALFHEIGHT = 8.f;

    private float m_VelX, m_VelY;

    public Projectile(float x, float y, float vx, float vy)
    {
        m_PosX = x;
        m_PosY = y;
        m_VelX = vx;
        m_VelY = vy;
    }

    @Override
    public void update(long simuTime)
    {
        // Movement
        m_PosX += m_VelX;
        m_PosY += m_VelY;

        // Death
        if(m_PosX < 0 || m_PosX >= Application.FIELD_WIDTH
        || m_PosY < 0 || m_PosY >= Application.FIELD_HEIGHT)
        {
            m_Alive = false; // Remove this entity
            return ;
        }
    }

    @Override
    public void render()
    {
        TextureManager.homingBullet.bind();
        DrawingUtils.drawRect(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT, m_PosX + HALFWIDTH, m_PosY + HALFHEIGHT);
    }

    @Override
    public Rectangle2D boundingBox()
    {
        return new Rectangle2D.Float(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT,
                2.f*HALFWIDTH, 2.f*HALFHEIGHT);
    }

    @Override
    public long type()
    {
        return Entity.ENEMY_BULLET;
    }

}
