// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math;

import yapi.exceptions.MathException;
import yapi.exceptions.math.RangeException;
import yapi.manager.worker.Task;
import yapi.manager.worker.TaskParallelization;
import yapi.manager.worker.WorkerPool;
import yapi.quick.Timer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class NumberUtils {

    private NumberUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Time Complexity: O(1)
     *
     * @since Version 1
     *
     * @param toRound the number you want to round.
     * @param digits the digits you want to round to.
     * @return the rounded number.
     */
    public static double round(double toRound, int digits) {
        int x = (int)Math.pow(10, digits);

        double r = (toRound * x);
        double t = r - (int) r;

        if (t >= 0.5) {
            return (((double)(int)r) + 1) / x;
        } else {
            return (((double)(int)r) + 0) / x;
        }
    }

    /**
     * Time Complexity: O(√n)
     *
     * @since Version 1
     *
     * @param n the number to check if it is prime
     * @return is prime yes or no.
     */
    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        long limit = (long) Math.sqrt(n);

        for (long i = 5; i <= limit; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Time Complexity: O(n+n*√n)
     *
     * @since Version 1
     *
     * @param n the upper limit to the Primes generated.
     * @return all primes between 0 and {@param n}
     */
    public static List<Long> getPrimes(long n) {
        List<Long> primes = new ArrayList<>();
        if (n < 1) {
            return primes;
        }
        if (n > 1) {
            primes.add(2L);
        }
        for (long i = 1; i <= n; i += 2) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @since Version 1
     *
     * @param a
     * @param b
     * @return
     */
    public static long greatestCommonDivisor(long a, long b) {
        if (b == 0) {
            return a < 0 ? -a : a;
        } else {
            return greatestCommonDivisor(b, a % b);
        }
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @since Version 1
     *
     * @param a
     * @param b
     * @return
     */
    public static long leastCommonMultiple(long a, long b) {
        long lcm = (a / greatestCommonDivisor(a, b)) * b;
        return lcm > 0 ? lcm : -lcm;
    }

    private static long greatestCommonFactor(long a, long b) {
        return b == 0 ? a : greatestCommonFactor(b, a % b);
    }

    /**
     * Time Complexity: ~O(log(a + b))
     *
     * @since Version 1
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean areCoprime(long a, long b) {
        return greatestCommonFactor(a, b) == 1;
    }

    /**
     * Time Complexity: O()
     *
     * @since Version 1
     *
     * @param n
     * @return
     */
    public static long nextPrime(long n) {
        if (isPrime(n)) {
            long l = lowPrimes(n);
            if (l != -1) {
                return l;
            }
            n += 2;
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            n += 2;
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            for (long i = n + 2; i < Long.MAX_VALUE; i += 6) {
                if (isPrime(i)) {
                    return i;
                }
                if (isPrime(i + 2)) {
                    return i + 2;
                }
            }
        } else {
            if (n < 2) {
                return 2;
            }
            if (n % 2 == 0) {
                n++;
            }
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            n += 2;
            if (n % 3 != 0 && isPrime(n)) {
                return n;
            }
            n += 2;
            for (long i = n; i < Long.MAX_VALUE; i += 6) {
                if (isPrime(i)) {
                    return i;
                }
                if (isPrime(i + 2)) {
                    return i + 2;
                }
            }
        }
        return -1;
    }

    private static int lowPrimes(long n) {
        if (n == 2) {
            return 3;
        } else if (n == 3) {
            return 5;
        } else if (n == 5) {
            return 7;
        } else if (n == 7) {
            return 11;
        }
        return -1;
    }

    /**
     * Time Complexity: O()
     *
     * @since Version 1
     *
     * @param n
     * @return
     */
    public static List<Long> primeFactorization(long n) {
        if (n < 2) {
            return new ArrayList<>();
        }
        List<Long> primes = new ArrayList<>();
        if (isPrime(n)) {
            primes.add(n);
            return primes;
        }

        long currentPrime = 2;
        while (!isPrime(n)) {
            if (n < currentPrime) {
                if (n != 1) {
                    primes.add(n);
                }
                return primes;
            }
            while (n % currentPrime == 0) {
                primes.add(currentPrime);
                n = n / currentPrime;
            }
            currentPrime = nextPrime(currentPrime);
        }

        if (n != 1) {
            primes.add(n);
        }
        return primes;
    }

    /**
     * Time Complexity: O(n)
     *
     * @since Version 1
     *
     * @param n
     * @return
     */
    public static List<Long> getDivisorsSorted(long n) {
        List<Long> divisors = new ArrayList<>();
        for (long i = 1; i <= Math.abs(n); i++) {
            if (n % i == 0) {
                divisors.add(i);
            }
        }
        return divisors;
    }

    /**
     * Time Complexity: O(√n)
     *
     * @since Version 1
     *
     * @param n
     * @return
     */
    public static List<Long> getDivisors(long n) {
        List<Long> divisors = new ArrayList<>();
        for (long i = 1; i <= Math.sqrt(Math.abs(n)); i++) {
            if (n % i == 0) {
                divisors.add(i);
                if (n / i != i) {
                    divisors.add(n / i);
                }
            }
        }
        return divisors;
    }

    /**
     *
     * @since Version 1
     *
     * @param s
     * @return
     */
    public static long sum(String s) {
        long l = 0;
        for (long lo : RangeSimple.getRange(s)) {
            l += lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long add(List<Long> longs) {
        long l = 0;
        for (long lo : longs) {
            l += lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1.2
     *
     * @param s
     * @return
     */
    public static long add(String s) {
        return sum(s);
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long subtract(List<Long> longs) {
        long l = 0;
        for (long lo : longs) {
            l -= lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long multiply(List<Long> longs) {
        long l = 1;
        for (long lo : longs) {
            l *= lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static long divide(List<Long> longs) {
        long l = 1;
        for (long lo : longs) {
            l /= lo;
        }
        return l;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static Long min(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        for (long l : longs) {
            if (l < current) {
                current = l;
            }
        }
        return current;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param longs
     * @return
     */
    public static Long minIndex(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        long index = 0;
        for (int i = 0; i < longs.size(); i++) {
            long l = longs.get(i);
            if (l < current) {
                current = l;
                index = i;
            }
        }
        return index;
    }

    /**
     *
     * @since Version 1
     *
     * @param longs
     * @return
     */
    public static Long max(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        for (long l : longs) {
            if (l > current) {
                current = l;
            }
        }
        return current;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param longs
     * @return
     */
    public static Long maxIndex(List<Long> longs) {
        if (longs.isEmpty()) {
            throw new RangeException("List is Empty");
        }
        long current = longs.get(0);
        long index = 0;
        for (int i = 0; i < longs.size(); i++) {
            long l = longs.get(i);
            if (l > current) {
                current = l;
                index = i;
            }
        }
        return index;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double pythagoras(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
     *
     * @since Version 1.2
     *
     *             input > 1 -> input * (input - 1)!
     * input! := { input = 0 -> 1
     *             input < 0 -> Error
     *
     * @param input
     * @return
     */
    public static long factorial(long input) {
        if (input < 0) {
            throw new MathException("Factorial of negatives is not defined");
        }
        if (input == 0) {
            return 1;
        }
        long output = 1;
        while (input > 0) {
            output *= input--;
        }
        return output;
    }

    public static long factorialNegative(long input) {
        if (input == 0) {
            return 1;
        }
        long output = 1;
        if (input < 0) {
            while (input < 0) {
                output *= input++;
            }
        } else {
            while (input > 0) {
                output *= input--;
            }
        }
        return output;
    }

    /**
     * Inputs bigger than   100.000 can need more than    20 seconds
     * Inputs bigger than   400.000 can need more than  1:00 minute
     * Inputs bigger than 1.000.000 can need more than 10:00 minutes
     *
     * @since Version 1.2
     *
     *             input > 1 -> input * (input - 1)!
     * input! := { input = 0 -> 1
     *             input < 0 -> Error
     *
     * @param bigInteger
     * @return
     */
    public static BigInteger factorial(BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.ZERO) < 0) {
            throw new MathException("factorial of negatives is not defined");
        }
        if (bigInteger.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ONE;
        }
        BigInteger num = BigInteger.ONE;
        BigInteger b = bigInteger.add(BigInteger.ZERO);
        while (b.compareTo(BigInteger.ZERO) > 0) {
            num = num.multiply(b);
            b = b.subtract(BigInteger.ONE);
        }
        return num;
    }

    // TODO: Check reduce out
    // Swift CODE
    // func factorial(_ n: Int) -> Int { return n < 2 ? 1 : (2...n).reduce(1, *) }

    public static BigInteger factorialNegative(BigInteger input) {
        if (input.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ONE;
        }
        BigInteger num = BigInteger.ONE;
        BigInteger b = input.add(BigInteger.ZERO);
        if (input.compareTo(BigInteger.ZERO) < 0) {
            while (b.compareTo(BigInteger.ZERO) < 0) {
                num = num.multiply(b);
                b = b.add(BigInteger.ONE);
            }
        } else {
            while (b.compareTo(BigInteger.ZERO) > 0) {
                num = num.multiply(b);
                b = b.subtract(BigInteger.ONE);
            }
        }
        return num;
    }

    public static void main(String[] args) {
        //fastFactorial(BigInteger.valueOf(4000000));
        Timer timer = new Timer();
        timer.start();
        fastFactorial(BigInteger.valueOf(2000000));
        //  6318995µs ->  100000
        // 12416465µs ->  200000
        // 11633725µs ->  300000
        // 14663832µs ->  400000
        // 18217963µs ->  500000
        // 20134973µs ->  600000
        // 23867773µs ->  700000
        // 29343748µs ->  800000
        // 32119835µs ->  900000

        // 37174959µs -> 1000000 (100000 Blocks)
        // 37700641µs -> 1000000 ( 50000 Blocks)
        // 38959835µs -> 1000000 ( 25000 Blocks)
        // 61877676µs -> 1000000 ( 10000 Blocks)
        // 97177108µs -> 1000000 (  5000 Blocks)

        // 43806860µs -> 1100000
        // 47806848µs -> 1200000
        timer.stop();
        System.out.println(timer.getTime() / 1000 + "µs");
        if (false) {
            fastFactorial(BigInteger.valueOf(4000000));
        }
    }

    private static int calculateThreadCount(BigInteger bigInteger, BigInteger count) {
        BigInteger threads = bigInteger.divide(count);
        if (threads.compareTo(BigInteger.ZERO) <= 0) {
            threads = BigInteger.ONE;
        }
        if (threads.compareTo(BigInteger.valueOf(16)) > 0) {
            threads = BigInteger.valueOf(16);
        }
        return threads.intValue();
    }

    public static BigInteger fastFactorial(BigInteger bigInteger) {
        return fastFactorial(bigInteger, BigInteger.valueOf(100000));
    }

    public static BigInteger fastFactorial(BigInteger bigInteger, BigInteger blocks) {
        int threads = calculateThreadCount(bigInteger, blocks);
        if (threads <= 0) {
            threads = 1;
        }
        if (threads > Runtime.getRuntime().availableProcessors()) {
            threads = Runtime.getRuntime().availableProcessors();
        }

        TaskParallelization<BigInteger> bigIntegerTaskParallelization = new TaskParallelization<>();
        WorkerPool workerPool = new WorkerPool(threads);

        BigInteger value = bigInteger.add(BigInteger.ZERO);
        BigInteger t = blocks.add(BigInteger.ZERO);

        while (value.compareTo(t) > 0) {
            BigInteger v = value.add(BigInteger.ZERO);
            workerPool.work(new Task(){
                @Override
                public void run() {
                    bigIntegerTaskParallelization.addResult(factorial(v, v.subtract(t)));
                }
            });
            value = value.subtract(t);
        }
        if (value.compareTo(BigInteger.ONE) >= 0) {
            bigIntegerTaskParallelization.addResult(factorial(value));
        }
        workerPool.await();

        List<BigInteger> bigIntegers = bigIntegerTaskParallelization.merge();
        while (bigIntegers.size() > threads) {
            List<List<BigInteger>> integers = bigIntegerTaskParallelization.split(bigIntegers, threads);
            bigIntegerTaskParallelization.clear();
            for (List<BigInteger> i : integers) {
                List<BigInteger> bI = new ArrayList<>(i);
                workerPool.work(new Task() {
                    @Override
                    public void run() {
                        BigInteger result = BigInteger.ONE;
                        for (BigInteger b : bI) {
                            result = result.multiply(b);
                        }
                        bigIntegerTaskParallelization.addResult(result);
                    }
                });
            }
            workerPool.await();
            bigIntegers = bigIntegerTaskParallelization.merge();
        }

        workerPool.awaitClose();
        BigInteger result = BigInteger.ONE;
        for (BigInteger b : bigIntegers) {
            result = result.multiply(b);
        }
        return result;
    }

    private static BigInteger factorial(BigInteger bigInteger, BigInteger limit) {
        if (bigInteger.compareTo(BigInteger.ZERO) < 0) {
            throw new MathException("factorial of negatives is not defined");
        }
        if (bigInteger.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ONE;
        }
        if (limit.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ZERO;
        }
        BigInteger num = BigInteger.ONE;
        BigInteger b = bigInteger.add(BigInteger.ZERO);
        while (b.compareTo(limit) > 0) {
            num = num.multiply(b);
            b = b.subtract(BigInteger.ONE);
        }
        return num;
    }

    /**
     *
     * @since Version 1.2
     *
     * n over r := n! / (r! * (n-r)!)
     *
     * @param n
     * @param r
     * @return n over r or -1 if r < 0 or r > n
     */
    public static long over(long n, long r) {
        if (r < 0 || r > n) {
            return -1;
        }
        return factorial(n) / (factorial(r) * factorial(n - r));
    }

    /**
     *
     * @since Version 1.2
     *
     * n over r := n! / (r! * (n-r)!)
     *
     * @param n
     * @param r
     * @return n over r or -1 if r < 0 or r > n
     */
    public static BigInteger over(BigInteger n, BigInteger r) {
        if (r.compareTo(BigInteger.ZERO) < 0 || r.compareTo(n) > 0) {
            return BigInteger.valueOf(-1);
        }
        return factorial(n).divide(factorial(r).multiply(factorial(n.subtract(r))));
    }

    public static long square(long n) {
        return n * n;
    }

    public static BigInteger square(BigInteger n) {
        return n.multiply(n);
    }

    public static long cube(long n) {
        return n * n * n;
    }

    public static BigInteger cube(BigInteger n) {
        return n.multiply(n).multiply(n);
    }

    public static long power(long n, long s) {
        if (s == 0) {
            return 1;
        }
        if (s == 1) {
            return n;
        }
        if (s == 2) {
            return square(n);
        }
        if (s == 3) {
            return cube(n);
        }
        if (s < 0) {
            return 1 / power(n, s * -1);
        }

        long t = 1;
        for (long i = s; i >= 0; i--) {
            t *= n;
        }
        return t;
    }

    public static BigInteger power(BigInteger n, BigInteger s) {
        if (s.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ONE;
        }
        if (s.compareTo(BigInteger.ONE) == 0) {
            return n;
        }
        if (s.compareTo(BigInteger.TWO) == 0) {
            return square(n);
        }
        if (s.compareTo(BigInteger.valueOf(3)) == 0) {
            return cube(n);
        }
        if (s.compareTo(BigInteger.ZERO) < 0) {
            return BigInteger.ONE.divide(power(n, s.multiply(BigInteger.valueOf(-1))));
        }

        BigInteger t = BigInteger.ONE;
        BigInteger m = new BigInteger(s.toByteArray());
        while (m.compareTo(BigInteger.ZERO) >= 0) {
            t = t.multiply(n);
            m = m.subtract(BigInteger.ONE);
        }
        return t;
    }

    public static long sumOfNumbers(long n) {
        if (n < 1) {
            return -1;
        }
        return (n * (n + 1)) / 2;
    }

    public static BigInteger sumOfNumbers(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) < 0) {
            return BigInteger.valueOf(-1);
        }
        return n.multiply(n.add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
    }

    public static long sumOfSquares(long n) {
        if (n < 1) {
            return -1;
        }
        return (n * (n + 1) * (2 * n + 1)) / 6;
    }

    public static BigInteger sumOfSquares(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) < 0) {
            return BigInteger.valueOf(-1);
        }
        return n.multiply(n.add(BigInteger.ONE)).multiply(BigInteger.valueOf(2).multiply(n).add(BigInteger.ONE)).divide(BigInteger.valueOf(6));
    }

    public static long sumOfCubes(long n) {
        if (n < 1) {
            return -1;
        }
        return square(n) * square(n + 1) / 4;
    }

    public static BigInteger sumOfCubes(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) < 0) {
            return BigInteger.valueOf(-1);
        }
        return square(n).multiply(square(n.add(BigInteger.ONE))).divide(BigInteger.valueOf(4));
    }

    public static long sumOfPowers(long n, long s) {
        if (n < 1) {
            return -1;
        }
        long t = 0;
        for (long i = 0; i < n; i++) {
            t += power(i, s);
        }
        return t;
    }

    public static BigInteger sumOfPowers(BigInteger n, BigInteger s) {
        if (n.compareTo(BigInteger.ONE) < 0) {
            return BigInteger.valueOf(-1);
        }
        BigInteger t = BigInteger.ZERO;
        BigInteger m = n.add(BigInteger.ZERO);
        while (m.compareTo(BigInteger.ZERO) >= 0) {
            t = t.add(power(m, s));
            m = m.subtract(BigInteger.ONE);
        }
        return t;
    }

    public static long sumOfDigits(long n) {
        char[] chars = (n + "").toCharArray();
        long t = 0;
        for (char c : chars) {
            if (c == '-') {
                continue;
            }
            t += c - '0';
        }
        return t;
    }

    public static BigInteger sumOfDigits(BigInteger n) {
        char[] chars = n.toString().toCharArray();
        BigInteger t = BigInteger.ZERO;
        for (char c : chars) {
            if (c == '-') {
                continue;
            }
            t = t.add(BigInteger.valueOf(c - '0'));
        }
        return t;
    }

    public static long multiplicationOfDigits(long n) {
        char[] chars = (n + "").toCharArray();
        long t = 1;
        for (char c : chars) {
            if (c == '-') {
                continue;
            }
            t *= c - '0';
        }
        return t;
    }

    public static BigInteger multiplicationOfDigits(BigInteger n) {
        char[] chars = n.toString().toCharArray();
        BigInteger t = BigInteger.ONE;
        for (char c : chars) {
            if (c == '-') {
                continue;
            }
            t = t.multiply(BigInteger.valueOf(c - '0'));
        }
        return t;
    }

    public static long[] sortedSquares(long... input) {
        long[] longs = new long[input.length];
        int indexLow = 0;
        int indexHigh = input.length - 1;
        int index = input.length - 1;

        for (int i = 0; i < input.length; i++) {
            if (i != 0 && input[i - 1] > input[i]) {
                return new long[0];
            }

            if (Math.abs(input[indexLow]) > input[indexHigh]) {
                longs[index] = square(input[indexLow]);
                indexLow++;
                index--;
            } else {
                longs[index] = square(input[indexHigh]);
                indexHigh--;
                index--;
            }
        }
        return longs;
    }

    public static String formatNumber(int i) {
        return addCommas(i + "");
    }

    public static String formatNumber(long l) {
        return addCommas(l + "");
    }

    public static String formatNumber(BigInteger bigInteger) {
        return addCommas(bigInteger.toString());
    }

    public static String formatNumber(float f) {
        String s = f + "";
        if (!s.contains(".")) {
            return addCommas(s);
        }
        return addCommas(s.substring(0, s.indexOf('.'))) + s.substring(s.indexOf('.'));
    }

    public static String formatNumber(double d) {
        String s = d + "";
        if (!s.contains(".")) {
            return addCommas(s);
        }
        return addCommas(s.substring(0, s.indexOf('.'))) + s.substring(s.indexOf('.'));
    }

    public static String formatNumber(BigDecimal bigDecimal) {
        String s = bigDecimal.toPlainString();
        if (!s.contains(".")) {
            return addCommas(s);
        }
        return addCommas(s.substring(0, s.indexOf('.'))) + s.substring(s.indexOf('.'));
    }

    private static String addCommas(String s) {
        if (s.length() <= 6) {
            return s;
        }
        char[] chars = s.toCharArray();
        StringBuilder st = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            st.append(chars[i]);
            if ((chars.length - i) % 3 == 0 && i != 0) {
                st.append(',');
            }
        }
        return st.reverse().toString();
    }

}