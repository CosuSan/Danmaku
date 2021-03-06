package fr.larez.danmaku;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import fr.larez.danmaku.utils.DrawingUtils;
import fr.larez.danmaku.utils.MathUtils;
import fr.larez.danmaku.utils.Rectanglef;

/**
 * The ship, i.e. the Entity controlled by the player.
 */
public class Ship extends Entity {

    /**
     * Ship movement speed, in pixels per millisecond.
     */
    private static final float MOVE_SPEED = 4.0f;

    private static final float HALFWIDTH = 32.0f;
    private static final float HALFHEIGHT = 32.0f;

    // Relative to the center
    private static final Rectanglef COLLISION = new Rectanglef(-3.0f, 6.0f, 6.0f, 6.0f);

    private long m_LastStraitBullets = 0;
    private long m_LastHomingBullets = 0;

    Ship()
    {
        reset();
    }

    void reset()
    {
        m_PosX = Application.FIELD_WIDTH*0.5f;
        m_PosY = 500.0f;
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
            if(simuTime >= m_LastStraitBullets + 200)
            {
                Application.addEntity(new StraightBullet(m_PosX - 5.0f, m_PosY - 20.0f, -0.30f, -15.0f));
                Application.addEntity(new StraightBullet(m_PosX + 5.0f, m_PosY - 20.0f, +0.30f, -15.0f));
                m_LastStraitBullets = simuTime;
                Application.gainPoints(2);
                // Yeah, we gain points for shooting, because WHY NOT!?
            }
            if(simuTime >= m_LastHomingBullets + 500)
            {
                // Find a target for the bullets
                Entity best = null;
                float sqDist = 99999999.0f;
                for(Entity target : Application.entities())
                {
                    if((target.type() & Entity.ENEMY) == 0)
                        continue;
                    Vector2f t = target.position();
                    float sq = MathUtils.square(t.getX() - m_PosX) + MathUtils.square(t.getY() - m_PosY);
                    if(sq < sqDist)
                    {
                        best = target;
                        sqDist = sq;
                    }
                }
                Application.addEntity(new HomingBullet(m_PosX - 23.0f, m_PosY - 2.0f, -3.0f, -10.0f, best));
                Application.addEntity(new HomingBullet(m_PosX + 23.0f, m_PosY - 2.0f, +3.0f, -10.0f, best));
                m_LastHomingBullets = simuTime;
            }
        }

        // Collision
        Entity other = Application.collide(this, Entity.ENEMY_BULLET);
        if(other != null)
            hit(simuTime);

        // Rocket engine particle effect
        if(simuTime % 40 == 0)
        {
            Texture particle = null;
            switch((int)(Math.random()*3.0))
            {
            case 0: particle = TextureManager.fire1; break;
            case 1: particle = TextureManager.fire2; break;
            case 2: particle = TextureManager.fire3; break;
            }
            Application.addEntity(new Particle(particle, m_PosX-5.0f, m_PosY + 31.0f, (float)(Math.random()-0.5)*2.0f, 2.5f, 10, true));
            Application.addEntity(new Particle(particle, m_PosX+5.0f, m_PosY + 31.0f, (float)(Math.random()-0.5)*2.0f, 2.5f, 10, true));
        }
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
    public Rectanglef boundingBox()
    {
        return new Rectanglef(
                m_PosX + COLLISION.getX(),
                m_PosY + COLLISION.getY(),
                COLLISION.getWidth(),
                COLLISION.getHeight());
    }

    @Override
    public long type()
    {
        return Entity.SHIP;
    }

}
