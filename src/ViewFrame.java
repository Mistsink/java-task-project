import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame{

    private int canvasWidth;
    private int canvasHeight;

    public ViewFrame(String title, int canvasWidth, int canvasHeight){

        super(title);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        AlgoCanvas canvas = new AlgoCanvas();
        setContentPane(canvas);

        /**
         *  布局的自动整理，使 frame 大小调整为可以容纳 canvas 的适当大小
         */
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);

        setLocationRelativeTo(null);
    }

    public ViewFrame(String title){
        //  default width & height
        this(title, 1024, 768);
    }

    public int getCanvasWidth(){
        return canvasWidth;
    }
    public int getCanvasHeight(){
        return canvasHeight;
    }

    /**
     * ModalData
     * 依赖数据
     */
    private ModalData data;

    /**
     *  渲染函数
     *  每次根据依赖数据，重新渲染图形界面
     * @param data
     */
    public void render(ModalData data){
        this.data = data;
        //  重新刷新 该控件（包括其子控件）
        repaint();
    }

    private class AlgoCanvas extends JPanel{

        public AlgoCanvas(){
            /**
             *  双缓存
             *  相当于在两块画布上进行更新内容的绘制，绘制完成后切换画布，解决闪烁的问题
             */
            super(true);
        }

        @Override
        /**
         * 在创建绘制 Panel 的时候，会自动调用底层的 paintComponent 方法
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D)g;

            /**
             *  解决抗锯齿问题
             *  让图形显示更细腻
             */
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.addRenderingHints(hints);

            /**
             *  具体逻辑控制 以及 视图绘制
             */
            int w = canvasWidth / data.N();
            VisUtil.setColor(g2d, VisUtil.LightBlue);

            for(int i = 0 ; i < data.N() ; i ++ ) {

                /**
                 *  根据关键索引绘制不同颜色的柱形
                 */
                if ( i >= data.l && i <= data.r)
                    VisUtil.setColor(g2d, VisUtil.Orange);
                else
                    VisUtil.setColor(g2d, VisUtil.Grey);

                if( i == data.curPivot )
                    VisUtil.setColor(g2d, VisUtil.Mazarin);
                if( i >= data.l + 1 && i <= data.curL)
                    VisUtil.setColor(g2d, VisUtil.LightBlue);
                if( i >= data.curR && i <= data.r)
                    VisUtil.setColor(g2d, VisUtil.LightBlue);
                if( data.fixedPivots[i] )
                    VisUtil.setColor(g2d, VisUtil.Red);

                VisUtil.fillRectangle(g2d, i * w, canvasHeight - data.get(i), w - 1, data.get(i));
            }
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(canvasWidth, canvasHeight);
        }
    }
}