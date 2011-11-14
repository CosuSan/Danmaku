package fr.larez.danmaku.level1;

import java.awt.geom.Rectangle2D;

import fr.larez.danmaku.Application;
import fr.larez.danmaku.Enemy;
import fr.larez.danmaku.Entity;
import fr.larez.danmaku.TextureManager;
import fr.larez.danmaku.utils.DrawingUtils;

/**
 * A simple enemy used in the first level.
 *
 * This enemy doesn't shoot at the player but can still collision with him.
 */
public class FiringEnemy extends Enemy {

    static final float HALFWIDTH = 16.f;
    static final float HALFHEIGHT = 16.f;

    private long m_LastFired = -99999;

    public FiringEnemy()
    {
        this(HALFWIDTH + (float)Math.random() * (Application.FIELD_WIDTH-2*HALFWIDTH));
    }

    public FiringEnemy(float x)
    {
        super(198.f);
        m_PosX = x;
        m_PosY = -HALFHEIGHT - 2.f;
    }

    @Override
    public void update(long simuTime)
    {
    	if(m_PosY <= 100.f)
    		m_PosY += 2.f;
    	else if(m_PosY <= 450.f)
    		m_PosY += 0.5f;
    	else
    		m_PosY += 4.f;

        if(m_PosY >= 100.f && m_PosY <= 400.f && simuTime >= m_LastFired + 2500)
        {
        	for(float a = 0.f; a <= 1.99f*Math.PI; a += 0.125f*Math.PI)
        	{
        		final float COS = (float)Math.cos(a);
        		final float SIN = (float)Math.sin(a);
        		Application.addEntity(new Projectile(m_PosX + 10.f*COS, m_PosY + 10.f*SIN, 2.f*COS, 2.f*SIN));
        	}
        	m_LastFired = simuTime;
        }
        if(dying())
        {
            // TODO : Spawn some shiny particles
            Application.gainPoints(30);
            m_Alive = false;
        }
        else if(m_PosY > Application.FIELD_HEIGHT + HALFHEIGHT)
            m_Alive = false;
    }

    @Override
    public void render()
    {
        TextureManager.enemy1.bind();
        DrawingUtils.drawRect(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT, m_PosX + HALFWIDTH, m_PosY + HALFHEIGHT);
        TextureManager.smallParticle.bind();
        for(float a = 0.f; a <= 1.99f*Math.PI; a += 0.5f*Math.PI)
        {
        	final float aa = a + Application.simulatedTime()*(float)Math.PI*1.E-3f;
        	float x = m_PosX + 20.f*(float)Math.cos(aa);
        	float y = m_PosY + 20.f*(float)Math.sin(aa);
        	DrawingUtils.drawRect(x - 2.f, y - 2.f, x + 2.f, y + 2.f);
        }
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
        return Entity.ENEMY // We can hit the player
                | Entity.ENEMY_BULLET; // We can be shot
    }

}
