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

    static final float HALFWIDTH = 32.f;
    static final float HALFHEIGHT = 32.f;

    // Relative to the center
    private static final Rectangle2D COLLISION = new Rectangle2D.Float(-3.f, 1.f, 6.f, 6.f);

    private long m_LastStraitBullets = 0;
    private long m_LastHomingBullets = 0;

    Ship()
    {
        reset();
    }

    void reset()
    {
        m_PosX = Application.FIELD_WIDTH*.5f;
        m_PosY = 550.f;
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
                Application.addEntity(new StraightBullet(m_PosX - 3.f, m_PosY - 30.f, -0.3f, -15.f));
                Application.addEntity(new StraightBullet(m_PosX + 3.f, m_PosY - 30.f, +0.3f, -15.f));
                m_LastStraitBullets = simuTime;
                Application.gainPoints(2);
                // Yeah, we gain points for shooting, because WHY NOT!?
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
                    {
                        best = target;
                        sqDist = sq;
                    }
                }
                Application.addEntity(new HomingBullet(m_PosX - 19.f, m_PosY - 6.f, -4.f, -10.f, best));
                Application.addEntity(new HomingBullet(m_PosX + 19.f, m_PosY - 6.f, +4.f, -10.f, best));
                m_LastHomingBullets = simuTime;
            }
        }

        // Collision
        Entity other = Application.collide(this, Entity.ENEMY_BULLET);
        if(other != null)
            hit(simuTime);
    }

    private void hit(long simuTime)
    {
        if(Application.lastHit() + Application.INVULN_ON_HIT < simuTime)
            Application.shipDies();
    }

    @Override
    public void render()
    {
    	// Blinking (period=200ms) when invulnerable
    	if( (Application.lastHit() + Application.INVULN_ON_HIT < Application.simulatedTime())
    	 || ( (Application.simulatedTime()/200) % 2 == 0) )
    	{
	        TextureManager.ship.bind();
	        DrawingUtils.drawRect(m_PosX - HALFWIDTH, m_PosY - HALFHEIGHT,
	                m_PosX + HALFWIDTH, m_PosY + HALFHEIGHT);
    	}
    }

    @Override
    public Rectangle2D boundingBox()
    {
        return new Rectangle2D.Float(
        		m_PosX + (float)COLLISION.getX(),
        		m_PosY + (float)COLLISION.getY(),
        		(float)COLLISION.getWidth(),
        		(float)COLLISION.getHeight());
    }

    @Override
    public long type()
    {
        return Entity.SHIP;
    }

}
