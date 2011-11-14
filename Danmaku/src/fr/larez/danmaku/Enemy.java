package fr.larez.danmaku;

/**
 * Base class for the enemies, i.e. stuff that we can hurt.
 */
public abstract class Enemy extends Entity {

    protected float m_Health;

    public Enemy(float health)
    {
        m_Health = health;
    }

    public void harm(float damage)
    {
        m_Health -= damage;
    }

    protected boolean dying()
    {
        return m_Health <= 0.0f;
    }

    @Override
    public long type()
    {
        return Entity.ENEMY;
    }

}
