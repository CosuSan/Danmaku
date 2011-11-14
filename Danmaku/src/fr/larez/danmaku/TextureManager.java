package fr.larez.danmaku;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * The texture manager, containing all the textures.
 *
 * It initially loads all the needed textures, which can then be accessed from
 * other classes.
 */
public class TextureManager {

    private static boolean init = false;

    public static Texture ship;
    public static Texture straightBullet;
    public static Texture homingBullet;
    public static Texture enemy1;
    public static Texture smallParticle;

    public static void loadAll()
    {
        if(init) return ;
        try
        {
            ship = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ship.png"));
            straightBullet = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/bullet.png"));
            homingBullet = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/bigbullet.png"));
            enemy1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/enemy1.png"));
            smallParticle = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/smallparticle.png"));
        } catch(Exception e) {
            System.err.println("Exception happened loading textures");
            e.printStackTrace();
            System.exit(1);
        }
        init = true;
    }

}
