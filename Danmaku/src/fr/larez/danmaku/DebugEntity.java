package fr.larez.danmaku;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.opengl.GL11;

import fr.larez.danmaku.utils.DrawingUtils;

public class DebugEntity implements Entity {

    private float m_PosX, m_PosY;
    private Entity.EType m_Type;

    public DebugEntity(float x, float y, Entity.EType type)
    {
        m_PosX = x;
        m_PosY = y;
        m_Type = type;
    }

    @Override
    public boolean update(long simuTime)
    {
        return true;
    }

    @Override
    public void render()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        DrawingUtils.drawRect(m_PosX - 10.f, m_PosY - 10.f,
                m_PosX + 10.f, m_PosY + 10.f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public Rectangle2D boundingBox()
    {
        return new Rectangle2D.Float(m_PosX - 10.f, m_PosY - 10.f,
                m_PosX + 10.f, m_PosY + 10.f);
    }

    @Override
    public Point2D position()
    {
        return new Point2D.Float(m_PosX, m_PosY);
    }

    @Override
    public EType type()
    {
        return m_Type;
    }

}
