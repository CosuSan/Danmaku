package fr.larez.danmaku;

import java.awt.geom.Rectangle2D;

import fr.larez.danmaku.utils.DrawingUtils;

/**
 * Bullets that are fired from the ship and fly in a straight line.
 */
public class StraightBullet extends Entity {

    private static final float HALFWIDTH = 4.0f;
    private static final float HALFHEIGHT = 4.0f;

    private float m_VelX, m_VelY;

    public StraightBullet(float x, float y, float vx, float vy)
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
        if(m_PosX < 0.0f || m_PosX >= Application.FIELD_WIDTH
        || m_PosY < 0.0f || m_PosY >= Application.FIELD_HEIGHT)
        {
            m_Alive = false; // Remove this entity
            return ;
        }

        // Collision
        Entity other = Application.collide(this, Entity.ENEMY);
        if(other != null)
        {
            ((Enemy)other).harm(10.0f);
            m_Alive = false;
            for(int i = 0; i < 10; ++i)
                Application.addEntity(new Particle(TextureManager.smallParticle,
                        m_PosX, m_PosY, 4.0f * ((float)Math.random()-0.5f), 4.0f * ((float)Math.random()-0.5f), 20));
            return ;
        }
    }

    @Override
    public void render()
    {
        TextureManager.straightBullet.bind();
        DrawingUtils.drawRect(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT, m_PosX + HALFWIDTH, m_PosY + HALFHEIGHT);
    }

    @Override
    public Rectangle2D boundingBox()
    {
        return new Rectangle2D.Float(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT,
                2.0f*HALFWIDTH, 2.0f*HALFHEIGHT);
    }

    @Override
    public long type()
    {
        return Entity.OWN_BULLET;
    }

}
