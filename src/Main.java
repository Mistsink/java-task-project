public class Main {
    public static void main(String[] args) {

        int sceneWidth = 800;
        int sceneHeight = 400;
        int N = 100;

        /**
         *  不同的数据生成方法
         *      默认随机生成
         *      基本已排序生成
         *      完全一致生成
         */
        new VisController(sceneWidth, sceneHeight, N, ModalData.Type.Default);
//        new VisController(sceneWidth, sceneHeight, N, ModalData.Type.NearlyOrdered);
//        new VisController(sceneWidth, sceneHeight, N, ModalData.Type.Identical);
    }
}
