package fr.larez.danmaku;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import fr.larez.danmaku.utils.DrawingUtils;

public class HomingBullet implements Entity {

    static final float HALFWIDTH = 8.f;
    static final float HALFHEIGHT = 8.f;

    private float m_PosX, m_PosY;
    private float m_VelX, m_VelY;
    private Entity m_Target;

    public HomingBullet(float x, float y, float vx, float vy, Entity target)
    {
        m_PosX = x;
        m_PosY = y;
        m_VelX = vx;
        m_VelY = vy;
        m_Target = target;
    }

    @Override
    public boolean update(long simuTime)
    {
        // Homing!
        if(m_Target != null)
        {
            Point2D targetPos = m_Target.position();

            float x = (float)targetPos.getX() - m_PosX;
            float y = (float)targetPos.getY() - m_PosY;

            float d = x*x + y*y;
            if(d > 100.f)
            {
                d = (float)Math.sqrt(d);
                x *= 10.f/d;
                y *= 10.f/d;
            }

            m_VelX = 0.8f*m_VelX + 0.2f*x;
            m_VelY = 0.8f*m_VelY + 0.2f*y;
        }

        // Movement
        m_PosX += m_VelX;
        m_PosY += m_VelY;

        // Death
        if(m_PosX < 0 || m_PosX >= Application.FIELD_WIDTH
        || m_PosY < 0 || m_PosY >= Application.FIELD_HEIGHT)
            return false; // Remove this entity
        return true;
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
        return new Rectangle2D.Float(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT, m_PosX + HALFWIDTH, m_PosY + HALFHEIGHT);
    }

    @Override
    public Point2D position()
    {
        return new Point2D.Float(m_PosX, m_PosY);
    }

    @Override
    public EType type()
    {
        return Entity.EType.OWN_BULLET;
    }

}
