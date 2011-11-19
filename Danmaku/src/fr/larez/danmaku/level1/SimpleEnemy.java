package fr.larez.danmaku.level1;

import fr.larez.danmaku.Application;
import fr.larez.danmaku.Enemy;
import fr.larez.danmaku.Entity;
import fr.larez.danmaku.TextureManager;
import fr.larez.danmaku.utils.DrawingUtils;
import fr.larez.danmaku.utils.Rectanglef;

/**
 * A simple enemy used in the first level.
 *
 * This enemy doesn't shoot at the player but can still collision with him.
 */
public class SimpleEnemy extends Enemy {

    private static final float HALFWIDTH = 8.0f;
    private static final float HALFHEIGHT = 8.0f;

    public SimpleEnemy()
    {
        this(HALFWIDTH + (float)Math.random() * (Application.FIELD_WIDTH - 2.0f*HALFWIDTH));
    }

    public SimpleEnemy(float x)
    {
        super(18.0f);
        m_PosX = x;
        m_PosY = -HALFHEIGHT - 2.0f;
    }

    @Override
    public void update(long simuTime)
    {
        m_PosY += 2.5;
        if(dying())
        {
            // TODO : Spawn some shiny particles
            Application.gainPoints(10);
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
    }

    @Override
    public Rectanglef boundingBox()
    {
        return new Rectanglef(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT,
                2.0f*HALFWIDTH, 2.0f*HALFHEIGHT);
    }

    @Override
    public long type()
    {
        return Entity.ENEMY // We can hit the player
                | Entity.ENEMY_BULLET; // We can be shot
    }

}
