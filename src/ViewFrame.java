import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ViewFrame extends JFrame{

    private int canvasWidth;
    private int canvasHeight;
    public boolean ready = false;
    public ModalData.Type dataType = ModalData.Type.Default;

    public ViewFrame(String title, int canvasWidth, int canvasHeight){

        super(title);

        System.out.println("ViewFrame - ready" + ready);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        AlgoMenu menu = new AlgoMenu();
//        setContentPane(canvas);
        add(menu, BorderLayout.NORTH);

        System.out.println("in ready make canvas");
        AlgoCanvas canvas = new AlgoCanvas();
        add(canvas, BorderLayout.CENTER);


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
    private ModalData data = new ModalData(new int[0]);
    public ModalData getData() {
        return data;
    }

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
            int w = data.N() > 0 ? canvasWidth / data.N() : 1;
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


    private class AlgoMenu extends JPanel {

        AlgoMenu() {
            super();
//            setLocationRelativeTo(this.getParent());
//            setLocation(20,10);
            setLayout(new BorderLayout());
            JLabel title = new JLabel("请选择需要排序的数据类型：");
            title.setBounds(20, 10, 200, 16);
            add(title, BorderLayout.CENTER);

            AlgoInput input = new AlgoInput();
            add(input, BorderLayout.SOUTH);
        }
    }

    private class AlgoInput extends JPanel {
        private JTextArea textArea;
        private final String PlaceHolder_u = "请用 , 来分隔数组的各数字(0 <= "+ canvasHeight +")";
        private final String PlaceHolder_nu = "点击确认按钮查看排序动画";

        AlgoInput () {
            super();

            setLayout(new FlowLayout(FlowLayout.LEFT));
            setSize(canvasWidth, 40);

            JComboBox comboBox = renderComboBox();
            add(comboBox);

            JPanel textArea = renderTextArea();
            add(textArea);
        }

        private JComboBox renderComboBox () {
            JComboBox comboBox = new JComboBox();
            comboBox.addItem("默认随机生成");
            comboBox.addItem("近乎有序生成");
            comboBox.addItem("全值相等生成");
            comboBox.addItem("用户自定义输入");

            comboBox.addItemListener((e)-> {
                String tar = (String) e.getItem();
                dataType = tar.equals("默认随机生成") ? ModalData.Type.Default
                    :   tar.equals("近乎有序生成") ? ModalData.Type.NearlyOrdered
                    :   tar.equals("全值相等生成") ? ModalData.Type.Identical
                    : ModalData.Type.UserDefined;

                if (dataType == ModalData.Type.UserDefined) {
                    textArea.setEditable(true);
                    textArea.setText(PlaceHolder_u);
                } else {
                    textArea.setEditable(false);
                    textArea.setText(PlaceHolder_nu);
                }

            });
            return comboBox;
        }

        private JPanel renderTextArea () {
            JPanel textInput = new JPanel();
            textInput.setLayout(new FlowLayout());

            textArea = new JTextArea(PlaceHolder_nu, 1, canvasWidth / 20);
            textArea.setLineWrap(true);
            textArea.setEditable(false);
            textArea.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (dataType == ModalData.Type.UserDefined)
                        textArea.setText("");
                }
                @Override
                public void focusLost(FocusEvent e) {
                }
            });

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setSize(canvasWidth / 2, 20);
//            scrollPane.setBounds(0, 0, canvasWidth / 2, 16);

            textInput.add(scrollPane, FlowLayout.LEFT);


            JButton confirmBtn = new JButton("确认");
            confirmBtn.addActionListener((e) -> {
                if (dataType == ModalData.Type.UserDefined) {
                    data = input2data(textArea.getText());
                    if (data == null)
                        return;
                }

                ready = true;
            });

            textInput.add(confirmBtn);

            return textInput;
        }


        private ModalData input2data (String inputs) {
            inputs = inputs.replace(" ", "");
            String[] arr = inputs.split(",");
            if (inputs.length() == 0 || arr.length == 0 || inputs.charAt(0) < '0' || inputs.charAt(0) > '9') {
                textArea.setText("请输入正确的数组(0 <= "+ canvasHeight +")");
                return null;
            }

            int[] num = new int[arr.length];
            for (int i = 0; i < arr.length; i ++) {
                int t = Integer.parseInt(arr[i]);
                if (t <= 0) {
                    textArea.setText("请输入非负整数(0 <= "+ canvasHeight +")");
                    return null;
                }
                num[i] = t;
            }

            return new ModalData(num);
        }
    }
}