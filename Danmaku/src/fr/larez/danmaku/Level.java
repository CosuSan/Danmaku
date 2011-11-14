package fr.larez.danmaku;

/**
 * A level defines what should appear when, and the background under the scene.
 *
 * Levels are usually packages, containing a subclass of Level and the rest of
 * the related classes (projectiles, enemies, ...).
 */
public abstract class Level {

    private String m_Name;
    private String m_Description;

    protected boolean m_Finished = false;

    public Level(String name, String description)
    {
        m_Name = name;
        m_Description = description;
    }

    public final String name()
    {
        return m_Name;
    }

    public final String description()
    {
        return m_Description;
    }

    public abstract void update(long simuTime);

    public void renderBackground(long simuTime)
    {
    }

    public void renderForeground(long simuTime)
    {
    }

    public final boolean finished()
    {
        return m_Finished;
    }

}
