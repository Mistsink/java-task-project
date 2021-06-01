import java.awt.*;

public class VisController {

    private static int DELAY = 100;

    private ModalData data;
    private ViewFrame frame;

    public VisController(int sceneWidth, int sceneHeight, int N, ModalData.Type dataType){

        data = new ModalData(N, sceneHeight, dataType);

        /**
         * 初始化视图
         * 使用事件派发队列去更新视图，确保视图的安全
         * 原因：
         *      swing 是单线程的设计，基于事件驱动，
         *      从事件派发线程以外的线程中更新Swing组件是非常危险的
         */
        EventQueue.invokeLater(() -> {
            frame = new ViewFrame("Quick Sort With 3 Ways Visualization", sceneWidth, sceneHeight);

            /**
             * 使用一个独立的线程是为了当前 EventQueue 中的事件能够顺利派发
             *      官方文档中指出，awt 控件的绘制线程中，
             *      不允许有某一事件线程耗时很长以致于事件监听线程的阻塞
             */
            new Thread(() -> {
                run();
            }).start();
        });
    }

    private void run(){
        updateData(-1, -1, -1, -1, -1, -1);

        //  抽离出排序的主体逻辑
        quickSort3Ways(0, data.N()-1);

        updateData(-1, -1, -1, -1, -1, -1);
    }

    /**
     *  三路快排的运算逻辑
     *  [l, r]
     * @param l
     * @param r
     */
    private void quickSort3Ways(int l, int r){

        if( l > r )
            return;

        if( l == r ) {
            updateData(l, r, l, -1, -1, -1);
            return;
        }

        updateData(l, r, -1, -1, -1, -1);

        // 随机在arr[l...r]的范围中, 选择一个数值作为标定点pivot
        int p = (int)(Math.random()*(r-l+1)) + l;
        updateData(l, r, -1, p, -1, -1);

        data.swap(l, p);
        int v = data.get(l);
        updateData(l, r, -1, l, -1, -1);

        int lt = l;     // arr[l+1...lt] < v
        int gt = r + 1; // arr[gt...r] > v
        int i = l+1;    // arr[lt+1...i) == v
        updateData(l, r, -1, l, lt, gt);

        //  主要 三路快排的 逻辑处理
        while( i < gt ){
            if( data.get(i) < v ){
                data.swap( i, lt+1);
                i ++;
                lt ++;
            }
            else if( data.get(i) > v ){
                data.swap( i, gt-1);
                gt --;
            }
            else    // arr[i] == v
                i ++;

            updateData(l, r, -1, l, i, gt);
        }

        data.swap( l, lt );
        updateData(l, r, lt, -1, -1, -1);

        //  递归执行 pivot 两边的序列
        quickSort3Ways(l, lt-1 );
        quickSort3Ways(gt, r);
    }

    /**
     *  更新依赖数据、同时更新视图
     * @param l
     * @param r
     * @param fixedPivot    与 pivot 等值的 最小的 index
     * @param curPivot
     * @param curL
     * @param curR
     */
    private void updateData(int l, int r, int fixedPivot, int curPivot, int curL, int curR){
        data.l = l;
        data.r = r;
        if(fixedPivot != -1){
            data.fixedPivots[fixedPivot] = true;
            int i = fixedPivot;
            while(i < data.N() && data.get(i) == data.get(fixedPivot)){
                data.fixedPivots[i] = true;
                i ++;
            }
        }
        data.curPivot = curPivot;
        data.curL = curL;
        data.curR = curR;

        frame.render(data);
        VisUtil.pause(DELAY);
    }
}