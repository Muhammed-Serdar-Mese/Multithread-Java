import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainA {
    public static void main(String[] args) {

        long start = System.nanoTime();
        int N = 10000; // N değeri burada değiştirilebilir
        int M = 100; // M değeri burada değiştirilebilir

        // Rastgele bir dizi oluştur
        int[] array = generateRandomArray(N);

        // Alt kümeleri oluştur
        List<int[]> subsets = createSubsets(array, M);

        // İş parçacıklarını oluştur
        List<SubsetProcessorThread> threads = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            threads.add(new SubsetProcessorThread(subsets.get(i)));
        }

        // İş parçacıklarını başlat
        for (Thread thread : threads) {
            thread.start();
        }

        // İş parçacıklarının bitmesini bekle
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // İş parçacıklarından gelen sonuçları raporla
        for (int i = 0; i < M; i++) {
            System.out.println("Alt küme " + (i+1) + " sonuçları:");
            System.out.println("En büyük sayı: " + threads.get(i).getMax());
            System.out.println("En küçük sayı: " + threads.get(i).getMin());
            System.out.println("Toplam: " + threads.get(i).getSum());
            System.out.println("Ortalama: " + threads.get(i).getAverage());
            System.out.println();
        }
        long duration = (System.nanoTime() - start)/1000000;    
        System.out.println(duration + "ms");
    }

    // N elemanlı rastgele bir dizi oluştur
    private static int[] generateRandomArray(int N) {
        int[] array = new int[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(10) + 1; // 1-10 arası rastgele sayılar
        }
        return array;
    }

    // Verilen bir diziyi M sayıda alt kümelere böler
    private static List<int[]> createSubsets(int[] array, int M) {
        List<int[]> subsets = new ArrayList<>();
        int subsetSize = array.length / M;
        int startIndex = 0;
        int endIndex = subsetSize;
        for (int i = 0; i < M - 1; i++) {
            int[] subset = new int[subsetSize];
            System.arraycopy(array, startIndex, subset, 0, subsetSize);
            subsets.add(subset);
            startIndex = endIndex;
            endIndex += subsetSize;
        }
        // Son kalan elemanları ekle
        int[] lastSubset = new int[array.length - startIndex];
        System.arraycopy(array, startIndex, lastSubset, 0, array.length - startIndex);
        subsets.add(lastSubset);
        return subsets;
    }
}

// Her bir alt küme için işlem parçacığı (thread)
class SubsetProcessorThread extends Thread {
    private int[] subset;
    private int max;
    private int min;
    private int sum;
    private double average;

    public SubsetProcessorThread(int[] subset) {
        this.subset = subset;
    }

    @Override
    public void run() {
        max = findMax(subset);
        min = findMin(subset);
        sum = calculateSum(subset);
        average = calculateAverage(subset);
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getSum() {
        return sum;
    }

    public double getAverage() {
        return average;
    }

    private int findMax(int[] subset) {
        int max = Integer.MIN_VALUE;
        for (int num : subset) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    private int findMin(int[] subset) {
        int min = Integer.MAX_VALUE;
        for (int num : subset) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }

    private int calculateSum(int[] subset) {
        int sum = 0;
        for (int num : subset) {
            sum += num;
        }
        return sum;
    }

    private double calculateAverage(int[] subset) {
        return (double) calculateSum(subset) / subset.length;
    }

}
