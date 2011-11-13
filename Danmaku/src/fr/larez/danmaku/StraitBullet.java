package fr.larez.danmaku;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import fr.larez.danmaku.utils.DrawingUtils;

public class StraitBullet extends Object implements Entity {

    static final float HALFWIDTH = 4.f;
    static final float HALFHEIGHT = 4.f;

    private float m_PosX, m_PosY;
    private float m_VelX, m_VelY;

    public StraitBullet(float x, float y, float vx, float vy)
    {
        m_PosX = x;
        m_PosY = y;
        m_VelX = vx;
        m_VelY = vy;
    }

    @Override
    public boolean update(long simuTime)
    {
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
        TextureManager.straitBullet.bind();
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
