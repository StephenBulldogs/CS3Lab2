import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Empirical Analysis of Sorting Algorithms
 * This program tests and compares the performance of:
 * - Insertion Sort (O(n^2))
 * - Selection Sort (O(n^2))
 * - Heap Sort (O(n log n))
 * - Java's Arrays.sort (O(n log n) - uses Dual-Pivot Quicksort)
 */
public class Main {

    /**
     * Insertion Sort Algorithm
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            // Move elements greater than key one position ahead
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Selection Sort Algorithm
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    public static void selectionSort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted portion
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }

            // Swap the found minimum element with the first element
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    /**
     * Heap Sort Algorithm
     * Time Complexity: O(n log n)
     * Space Complexity: O(1)
     */
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Heapify the reduced heap
            heapify(arr, i, 0);
        }
    }

    /**
     * Helper method to maintain heap property
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // If left child is larger than root
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }

    /**
     * Generate a random array of integers
     * @param size the size of the array
     * @param seed the seed for random number generation
     * @return array of random integers
     */
    public static int[] generateRandomArray(int size, long seed) {
        Random random = new Random(seed);
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(); // Generates random integers
        }
        return array;
    }

    /**
     * Time a sorting algorithm using System.nanoTime()
     * @param arr the array to sort
     * @param algorithm the algorithm to use ("insertion", "selection", "heapsort", "java")
     * @return execution time in milliseconds
     */
    public static long timeSortingAlgorithm(int[] arr, String algorithm) {
        // Create a copy to avoid modifying the original array
        int[] copy = Arrays.copyOf(arr, arr.length);

        // Start measuring execution time
        long startTime = System.nanoTime();

        // Execute the appropriate sorting algorithm
        switch (algorithm) {
            case "insertion":
                insertionSort(copy);
                break;
            case "selection":
                selectionSort(copy);
                break;
            case "heapsort":
                heapSort(copy);
                break;
            case "java":
                Arrays.sort(copy);  // Java's built-in sort (Dual-Pivot Quicksort)
                break;
        }

        // Stop measuring execution time
        long endTime = System.nanoTime();

        // Calculate the execution time in milliseconds
        long executionTime = (endTime - startTime) / 1000000;

        return executionTime;
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   Empirical Analysis of Sorting Algorithms                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Array sizes to test - start small and increase
        int[] smallSizes = {1000, 5000, 10000, 25000, 50000, 75000, 100000, 150000};

        // For larger arrays, only test efficient algorithms
        int[] largeSizes = {200000, 250000, 500000, 750000, 1000000, 10000000};

        // Number of trials per size for averaging
        int trials = 5;

        // All algorithms for small/medium arrays
        String[] algorithms = {"insertion", "selection", "heapsort", "java"};

        // Only efficient algorithms for large arrays
        String[] efficientAlgorithms = {"heapsort", "java"};

        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv"))) {
            // Write CSV header
            writer.println("Size,Algorithm,AvgTime(ms),Trial1,Trial2,Trial3,Trial4,Trial5");

            // ============================================================
            // Phase 1: Test small to medium arrays with all algorithms
            // ============================================================
            System.out.println("PHASE 1: Testing small to medium arrays (all algorithms)");
            System.out.println("─────────────────────────────────────────────────────────────");
            System.out.println();

            for (int size : smallSizes) {
                System.out.println("Testing array size: " + String.format("%,d", size));

                for (String algorithm : algorithms) {
                    long[] times = new long[trials];

                    // Run multiple trials
                    for (int trial = 0; trial < trials; trial++) {
                        // Generate a new random array for each trial
                        int[] arr = generateRandomArray(size, System.nanoTime() + trial);
                        times[trial] = timeSortingAlgorithm(arr, algorithm);
                    }

                    // Calculate average time
                    long sum = 0;
                    for (long time : times) {
                        sum += time;
                    }
                    double avg = sum / (double) trials;

                    // Write to CSV
                    writer.printf("%d,%s,%.2f", size, algorithm, avg);
                    for (long time : times) {
                        writer.printf(",%d", time);
                    }
                    writer.println();

                    // Display results
                    System.out.printf("  %-15s: avg = %8.2f ms  ", algorithm, avg);
                    System.out.print("(trials: ");
                    for (int i = 0; i < times.length; i++) {
                        System.out.print(times[i] + "ms");
                        if (i < times.length - 1) System.out.print(", ");
                    }
                    System.out.println(")");
                }
                System.out.println();
            }

            // ============================================================
            // Phase 2: Test large arrays with only efficient algorithms
            // ============================================================
            System.out.println();
            System.out.println("PHASE 2: Testing large arrays (efficient algorithms only)");
            System.out.println("─────────────────────────────────────────────────────────────");
            System.out.println("Note: Skipping O(n²) algorithms as they would take too long");
            System.out.println();

            for (int size : largeSizes) {
                System.out.println("Testing array size: " + String.format("%,d", size));

                for (String algorithm : efficientAlgorithms) {
                    long[] times = new long[trials];

                    for (int trial = 0; trial < trials; trial++) {
                        int[] arr = generateRandomArray(size, System.nanoTime() + trial);
                        times[trial] = timeSortingAlgorithm(arr, algorithm);
                    }

                    long sum = 0;
                    for (long time : times) {
                        sum += time;
                    }
                    double avg = sum / (double) trials;

                    writer.printf("%d,%s,%.2f", size, algorithm, avg);
                    for (long time : times) {
                        writer.printf(",%d", time);
                    }
                    writer.println();

                    System.out.printf("  %-15s: avg = %8.2f ms  ", algorithm, avg);
                    System.out.print("(trials: ");
                    for (int i = 0; i < times.length; i++) {
                        System.out.print(times[i] + "ms");
                        if (i < times.length - 1) System.out.print(", ");
                    }
                    System.out.println(")");
                }
                System.out.println();
            }

        } catch (IOException e) {
            System.err.println("Error writing results: " + e.getMessage());
            return;
        }

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   Analysis Complete!                                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Results saved to: results.csv");
        System.out.println();
        System.out.println("Next steps:");
        System.out.println("1. Import results.csv into Google Sheets or Excel");
        System.out.println("2. Create a line graph with:");
        System.out.println("   - X-axis: Array Size");
        System.out.println("   - Y-axis: Average Time (ms)");
        System.out.println("   - Separate lines for each algorithm");
        System.out.println("3. Export the graph as PDF for submission");
    }
}