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
    public static Texture fire1, fire2, fire3;

    public static void loadAll()
    {
        if(init) return ;
        try
        {
            ship = load("res/ship.png");
            straightBullet = load("res/bullet.png");
            homingBullet = load("res/bigbullet.png");
            enemy1 = load("res/enemy1.png");
            smallParticle = load("res/smallparticle.png");
            fire1 = load("res/fire1.png");
            fire2 = load("res/fire2.png");
            fire3 = load("res/fire3.png");
        } catch(Exception e) {
            System.err.println("Exception happened loading textures");
            e.printStackTrace();
            System.exit(1);
        }
        init = true;
    }

    private static Texture load(String path) throws Exception
    {
        return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
    }

}
