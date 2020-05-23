package org.Query.Columnar;

import java.nio.ByteBuffer;

public class HyperLogLog
{
    private static final long Pow2_32 = 1L << 32;

    private final int b;
    private final int m;                            // number of registers m = 2^b
    private final double alphaM;

    private int[] M;                                // a collection of m registers

    public HyperLogLog(int b) {
        assert b >= 4 && b <= 16;

        this.b = b;
        this.m = 1 << b;
        this.alphaM
              = b == 4 ? 0.673 // m == 16
              : b == 5 ? 0.697 // m == 32
              : b == 6 ? 0.709 // m == 64
              : 0.7213 / (1 + 1.079 / m);

        this.M = new int[m];
    }

    public void update(ByteBuffer value) {
        int x = Util.SDBMHash(value.array());

        // determine position in register using first b bits of hashed value x
        int j = x >>> (Integer.SIZE - b);
        M[j] = Math.max(M[j], rank((x << b) | (1 << (b - 1)) + 1));
    }

    public double get() {
        // compute Z with 'indicator' function
        double Z = 0.0;

        for (int i = 0; i < m; ++i)
            Z += 1.0 / (1 << M[i]);

        // "raw" HyperLogLog estimate
        double E = alphaM * m * m / Z;

        if (E <= (5.0 / 2.0) * m) {
            // small range correction
            int V = 0;

            for (int v : M)
                if (v == 0) V++;

            return V == 0 ? E : m * Math.log(m / V);
        } else if (E <= Pow2_32 / 30.0) {
            // intermediate range collection - no correction
            return E;
        } else {
            // large range correction
            return -1 * Pow2_32 * Math.log(1.0 - E / Pow2_32);
        }
    }

    int rank(int w) {
        return w == 0 ? 0 : 1 + Integer.numberOfLeadingZeros(w);
    }
}
/**
 * public class HyperLogLogTest
 * {
 *     private List<String> wordList;
 *
 *     @Before
 *     public void setUp() throws IOException
 *     {
 *         wordList = new ArrayList<String>();
 *         BufferedReader reader = new BufferedReader(new FileReader("/usr/share/words"));
 *
 *         try {
 *             String line = reader.readLine();
 *
 *             while (line != null) {
 *                 for (String w : Arrays.asList(line.split("[,\\s]+")))
 *                     if (w.length() > 0)
 *                         wordList.add(w);
 *
 *                 line = reader.readLine();
 *             }
 *         }
 *         finally {
 *             reader.close();
 *         }
 *     }
 *
 *     @Test
 *     public void test() {
 *         for (int i = 5; i < 17; i++) {
 *             double expectedError = 1.04 / Math.sqrt(1 << i);
 *             testCardinality(new HyperLogLog(i), expectedError);
 *         }
 *     }
 *
 *     private void testCardinality(HyperLogLog counter, double expectedError) {
 *         for (String w : wordList)
 *             counter.update(ByteBufferUtil.bytes(w));
 *
 *         double count = counter.get();
 *         double actualError = (count - wordList.size()) / wordList.size();
 *         assertTrue("error rate should be in ±" + (expectedError * 100) + "%, but actually " + (actualError * 100), Math.abs(expectedError) >= Math.abs(actualError));
 *     }
 * }
 */