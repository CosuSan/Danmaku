package fr.larez.danmaku;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import fr.larez.danmaku.utils.DrawingUtils;
import fr.larez.danmaku.utils.Rectanglef;

/**
 * Any kind of particle emitted for graphical purposes.
 *
 * Shiny!
 */
public class Particle extends Entity {

    private Texture m_Texture;
    private float m_VelX, m_VelY;
    private int m_Life;
    private float m_AlphaMultiplier = 0.0f;

    public Particle(Texture texture, float x, float y, float vx, float vy, int lifetime, boolean fade)
    {
        m_Texture = texture;
        m_PosX = x - texture.getImageWidth()*0.5f;
        m_PosY = y - texture.getImageHeight()*0.5f;
        m_VelX = vx;
        m_VelY = vy;
        m_Life = lifetime;
        if(fade)
            m_AlphaMultiplier = 1.0f/lifetime;
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
        if(m_PosX < -m_Texture.getWidth() || m_PosX >= Application.FIELD_WIDTH
        || m_PosY < -m_Texture.getHeight() || m_PosY >= Application.FIELD_HEIGHT)
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
        if(m_AlphaMultiplier != 0.0f)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, m_Life * m_AlphaMultiplier);
        DrawingUtils.drawRect(m_PosX, m_PosY, m_PosX + m_Texture.getImageWidth(), m_PosY + m_Texture.getImageHeight());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public Rectanglef boundingBox()
    {
        return null;
    }

    @Override
    public long type()
    {
        return 0;
    }

}
