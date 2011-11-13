package fr.larez.danmaku;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class TextureManager {

    private static boolean init = false;

    public static Texture ship;
    public static Texture straitBullet;
    public static Texture homingBullet;

    public static void loadAll()
    {
        if(init) return ;
        try
        {
            ship = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ship.png"));
            straitBullet = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/bullet.png"));
            homingBullet = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/bigbullet.png"));
        } catch(Exception e) {
            System.err.println("Exception happened loading textures");
            e.printStackTrace();
            System.exit(1);
        }
        init = true;
    }

}
