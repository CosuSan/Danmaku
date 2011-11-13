package fr.larez.danmaku;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Keyboard;

import fr.larez.danmaku.utils.DrawingUtils;
import fr.larez.danmaku.utils.MathUtils;

/**
 * The ship, i.e. the Entity controlled by the player.
 */
public class Ship extends Entity {

    /**
     * Ship movement speed, in pixels per millisecond.
     */
    static final float MOVE_SPEED = 3.f;

    static final float HALFWIDTH = 16.f;
    static final float HALFHEIGHT = 16.f;

    static final float HALFCOLWIDTH = 3.f;
    static final float HALFCOLHEIGHT = 3.f;

    private long m_LastStraitBullets = 0;
    private long m_LastHomingBullets = 0;

    Ship(float x, float y)
    {
        m_PosX = x;
        m_PosY = y;
    }

    @Override
    public void update(long simuTime)
    {
        // Movement
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            m_PosX -= MOVE_SPEED;
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            m_PosX += MOVE_SPEED;
        if(Keyboard.isKeyDown(Keyboard.KEY_UP))
            m_PosY -= MOVE_SPEED;
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            m_PosY += MOVE_SPEED;

        // Field boundary
        if(m_PosX < HALFWIDTH)
            m_PosX = HALFWIDTH;
        else if(m_PosX > Application.FIELD_WIDTH - HALFWIDTH)
            m_PosX = Application.FIELD_WIDTH - HALFWIDTH;
        if(m_PosY < HALFHEIGHT)
            m_PosY = HALFHEIGHT;
        else if(m_PosY > Application.FIELD_HEIGHT - HALFHEIGHT)
            m_PosY = Application.FIELD_HEIGHT - HALFHEIGHT;

        // Shoot
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            if(simuTime > m_LastStraitBullets + 198)
            {
                Application.addEntity(new StraightBullet(m_PosX - 3.f, m_PosY, -0.3f, -15.f));
                Application.addEntity(new StraightBullet(m_PosX + 3.f, m_PosY, +0.3f, -15.f));
                m_LastStraitBullets = simuTime;
            }
            if(simuTime > m_LastHomingBullets + 498)
            {
                // Find a target for the bullets
                Entity best = null;
                float sqDist = 99999999.f;
                for(Entity target : Application.entities())
                {
                    if((target.type() & Entity.ENEMY) == 0)
                        continue;
                    Point2D t = target.position();
                    float sq = MathUtils.square((float)t.getX() - m_PosX) + MathUtils.square((float)t.getY() - m_PosY);
                    if(sq < sqDist)
                        best = target;
                }
                Application.addEntity(new HomingBullet(m_PosX - 6.f, m_PosY, -4.f, -10.f, best));
                Application.addEntity(new HomingBullet(m_PosX + 6.f, m_PosY, +4.f, -10.f, best));
                m_LastHomingBullets = simuTime;
            }
        }

        // Collision
        Entity other = Application.collide(this, Entity.ENEMY_BULLET);
        if(other != null)
            Application.shipDies();
    }

    @Override
    public void render()
    {
        TextureManager.ship.bind();
        DrawingUtils.drawRect(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT,
                m_PosX + HALFWIDTH, m_PosY + HALFHEIGHT);
    }

    @Override
    public Rectangle2D boundingBox()
    {
        return new Rectangle2D.Float(m_PosX - HALFCOLWIDTH, m_PosY - HALFCOLHEIGHT,
                2.f*HALFCOLWIDTH, 2.f*HALFCOLHEIGHT);
    }

    @Override
    public long type()
    {
        return Entity.SHIP;
    }

}
