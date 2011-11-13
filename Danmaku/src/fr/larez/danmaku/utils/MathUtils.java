package fr.larez.danmaku.utils;

public class MathUtils {

    private MathUtils()
    {
        assert(false);
    }

    public static float square(float x)
    {
        return x*x;
    }

    public static float clamp(float a, float x, float b)
    {
        if(x < a)
            return a;
        else if(x > b)
            return b;
        else
            return x;
    }

}
