package fr.larez.danmaku.utils;


public class Rectanglef {

    private float m_X, m_Y, m_Width, m_Height;

    public Rectanglef(float x, float y, float w, float h)
    {
        m_X = x;
        m_Y = y;
        m_Width = w;
        m_Height = h;
    }

    public boolean intersects(Rectanglef other)
    {
        float tx = m_X, ty = m_Y;
        float tw = m_Width, th = m_Height;
        float ox = other.m_X, oy = other.m_Y;
        float ow = other.m_Width, oh = other.m_Height;
        if(tw <= 0.0f || th <= 0.0f
        || ow <= 0.0f || oh <= 0.0f)
            return false;
        return tx+tw >= ox && ox+ow >= tx
                && ty+th >= oy && oy+oh >= ty;
    }

    public float getX()
    {
        return m_X;
    }

    public float getY()
    {
        return m_Y;
    }

    public float getWidth()
    {
        return m_Width;
    }

    public float getHeight()
    {
        return m_Height;
    }

}
