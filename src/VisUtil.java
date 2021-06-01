import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * 绘制视图的工具类
 * 是各常量、工具函数的集合
 */
public class VisUtil {

    /**
     * 防止用户实例化
     */
    private VisUtil(){}

    /**
     * 颜色常量
     * 因为 Color 类中没有现成的各式颜色常量，故而自行封装
     */
    public static final Color Red = new Color(0xF44336);
    public static final Color Mazarin = new Color(0x7082E8);
    public static final Color LightBlue = new Color(0x03A9F4);
    public static final Color BlueGrey = new Color(0x607D8B);
    public static final Color Orange = new Color(255, 200, 0);
    public static final Color Grey = new Color(128, 128, 128);

    /**
     * 绘制 填充式 矩形
     * @param g     Graphics2D
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public static void fillRectangle(Graphics2D g, int x, int y, int w, int h){

        Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
        g.fill(rectangle);
    }

    /**
     * 颜色设置
     * 虽然原生框架也只有一行代码即可实现
     * 但是单独封装一个设置颜色的函数 是为了 分离出基本视图配置的 工具函数
     * 让逻辑更清晰，代码更有条理
     * @param g     Graphics2D
     * @param color
     */
    public static void setColor(Graphics2D g, Color color){
        g.setColor(color);
    }


    /**
     * 视图暂停
     * @param t     暂停的时间，毫秒单位
     */
    public static void pause(int t) {
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

}