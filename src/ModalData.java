import java.util.Arrays;


public class ModalData {

    public enum Type{
        Default,
        NearlyOrdered,
        Identical,
        UserDefined
    }

    private int[] numbers;
    public int l, r;
    public boolean[] fixedPivots;   //  记录已排序的 pivot
    public int curPivot;            //  当前的 pivot
    public int curL, curR;

    public ModalData(int N, int randomBound, Type dataType){

        numbers = new int[N];
        fixedPivots = new boolean[N];

        int lBound = 1;
        int rBound = randomBound;

        //  全部相同值的方案
        if(dataType == Type.Identical){
            int avgNumber = (lBound + rBound) / 2;
            lBound = avgNumber;
            rBound = avgNumber;
        }

        for( int i = 0 ; i < N ; i ++) {
            numbers[i] = (int)(Math.random()*(rBound-lBound+1)) + lBound;
            fixedPivots[i] = false;
        }

        //  近乎排好序的方案
        if(dataType == Type.NearlyOrdered){
            Arrays.sort(numbers);
            int swapTime = (int)(0.01*N);
            for(int i = 0 ; i < swapTime; i ++){
                int a = (int)(Math.random()*N);
                int b = (int)(Math.random()*N);
                swap(a, b);
            }
        }
    }

    public ModalData(int[] numbers) {
        this.numbers = numbers;
        fixedPivots = new boolean[numbers.length];
    }

    public ModalData(int N, int randomBound){
        this(N, randomBound, Type.Default);
    }

    public int N(){
        return numbers.length;
    }

    public int get(int index){
        if( index < 0 || index >= numbers.length)
            throw new IllegalArgumentException("Invalid index to access Sort Data.");

        return numbers[index];
    }

    public void swap(int i, int j) {

        if( i < 0 || i >= numbers.length || j < 0 || j >= numbers.length)
            throw new IllegalArgumentException("Invalid index to access Sort Data.");

        int t = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = t;
    }
}