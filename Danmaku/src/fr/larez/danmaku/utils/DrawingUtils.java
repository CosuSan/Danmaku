package fr.larez.danmaku.utils;

import org.lwjgl.opengl.GL11;

public class DrawingUtils {

    private DrawingUtils()
    {
        assert(false);
    }

    public static void drawRect(float x1, float y1, float x2, float y2)
    {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.f, 0.f); GL11.glVertex2f(x1, y1);
        GL11.glTexCoord2f(1.f, 0.f); GL11.glVertex2f(x2, y1);
        GL11.glTexCoord2f(1.f, 1.f); GL11.glVertex2f(x2, y2);
        GL11.glTexCoord2f(0.f, 1.f); GL11.glVertex2f(x1, y2);
        GL11.glEnd();
    }

    public static void translate(float x, float y)
    {
        GL11.glTranslatef(x, y, 0.f);
    }

    public static void reset()
    {
        GL11.glLoadIdentity();
    }

}
