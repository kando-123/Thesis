package algorithms;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class Mask
{
    private static final int MAX_LENGTH = 32;

    private int register;
    private int length;

    public Mask(int length)
    {
        if (length > MAX_LENGTH || length <= 0)
        {
            throw new OutOfRangeException();
        }
        this.length = length;
    }

    public int get(int bit)
    {
        if (bit < 0 || bit >= length)
        {
            throw new OutOfRangeException();
        }
        else
        {
            return (register & (1 << bit)) >>> bit;
        }
    }

    public void set(int bit)
    {
        if (bit < 0 || bit >= length)
        {
            throw new OutOfRangeException();
        }
        else
        {
            register |= 1 << bit;
        }
    }

    public void clear(int bit)
    {
        if (bit < 0 || bit >= length)
        {
            throw new OutOfRangeException();
        }
        else
        {
            register &= ~(1 << bit);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = length - 1; i >= 0; --i)
        {
            builder.append(get(i));
        }
        return builder.toString();
    }

    static class OutOfRangeException extends RuntimeException
    {

    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public class CombinationsTest
{
    @Test
    public void testGenerateCombinationMasks()
    {
        var pairs = new Pair[5];
        var sets = new Set[pairs.length];
        pairs[0] = new Pair(5, 3);
        sets[0] = Set.of("11100",
                "11010", "11001", "10110", "10101",
                "10011", "01110", "01101", "01011",
                "00111");
        pairs[1] = new Pair(8, 7);
        sets[1] = Set.of("11111110",
                "11111101", "11111011", "11110111", "11101111",
                "11011111", "10111111", "01111111");
        pairs[2] = new Pair(6, 2);
        sets[2] = Set.of("110000",
                "101000", "100100", "100010", "100001",
                "011000", "010100", "010010", "010001",
                "001100", "001010", "001001", "000110",
                "000101", "000011");
        pairs[3] = new Pair(3, 3);
        sets[3] = Set.of("111");
        pairs[4] = new Pair(3, 0);
        sets[4] = Set.of("000");
        for (int i = 0; i < pairs.length; ++i)
        {
            var masks = generateCombinationMasks(pairs[i].x, pairs[i].y);
            var masksAsSet = Arrays.stream(masks).map(m -> m.toString()).collect(Collectors.toSet());
            if (!masksAsSet.equals(sets[i]))
            {
                fail("Different sets.");
            }
        }
    }

    private static Mask[] generateCombinationMasks(int n, int k)
    {
        int m = binomialCoefficient(n, k);
        Mask[] masks = new Mask[m];

        for (int i = 0; i < masks.length; ++i)
        {
            masks[i] = new Mask(n);
        }

        int[] indexes = new int[k];

        for (int i = 0; i < k; ++i)
        {
            indexes[i] = n - k + i;
        }

        for (int j = 0; j < k; ++j)
        {
            masks[0].set(indexes[j]);
        }

        for (int i = 1; i < m; ++i)
        {
            if (indexes[0] > 0)
            {
                --indexes[0];
            }
            else
            {
                int j = 1;
                while (indexes[j] == j)
                {
                    ++j;
                }

                --indexes[j];
                while (--j >= 0)
                {
                    indexes[j] = indexes[j + 1] - 1;
                }
            }

            for (int j = 0; j < k; ++j)
            {
                masks[i].set(indexes[j]);
            }
        }

        return masks;
    }

    private static int binomialCoefficient(int n, int k)
    {
        assert (n >= k);

        int product = 1;
        for (int i = n; i > k; --i)
        {
            product *= i;
        }
        for (int i = n - k; i > 1; --i)
        {
            product /= i;
        }

        return product;
    }

    private static class Pair
    {
        final int x;
        final int y;
        
        public Pair(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode()
        {
            int code = 17;
            code = 17 * x + code;
            code = 17 * y + code;
            return code;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final Pair other = (Pair) obj;
            if (this.x != other.x)
            {
                return false;
            }
            return this.y == other.y;
        }
    }
}
