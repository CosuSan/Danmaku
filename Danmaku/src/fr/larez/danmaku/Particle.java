package fr.larez.danmaku;

import java.awt.geom.Rectangle2D;

import org.newdawn.slick.opengl.Texture;

import fr.larez.danmaku.utils.DrawingUtils;

/**
 * Any kind of particle emitted for graphical purposes.
 *
 * Shiny!
 */
public class Particle extends Entity {

    private Texture m_Texture;
    private float m_VelX, m_VelY;
    private int m_Life;

    public Particle(Texture texture, float x, float y, float vx, float vy, int lifetime)
    {
        m_Texture = texture;
        m_PosX = x - texture.getWidth()*0.5f;
        m_PosY = y - texture.getHeight()*0.5f;
        m_VelX = vx;
        m_VelY = vy;
        m_Life = lifetime;
    }

    @Override
    public void update(long simuTime)
    {
        m_Life--;
        if(m_Life <= 0)
        {
            m_Alive = false;
            return ;
        }

        // Death
        if(m_PosX < 0.0f || m_PosX >= Application.FIELD_WIDTH
        || m_PosY < 0.0f || m_PosY >= Application.FIELD_HEIGHT)
        {
            m_Alive = false; // Remove this entity
            return ;
        }

        m_PosX += m_VelX;
        m_PosY += m_VelY;
    }

    @Override
    public void render()
    {
        m_Texture.bind();
        DrawingUtils.drawRect(m_PosX, m_PosY, m_PosX + m_Texture.getWidth(), m_PosY + m_Texture.getHeight());
    }

    @Override
    public Rectangle2D boundingBox()
    {
        return null;
    }

    @Override
    public long type()
    {
        return 0;
    }

}
