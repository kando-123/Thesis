package ge.world;

import ge.utilities.*;
import java.util.*;

/**
 * Perlin Noise generator. Works on a pixel map (integral point coordinates).
 *
 * @author Kay Jay O'Nail
 */
public class PerlinNoise
{
    /**
     * The width of the noised area, in pixels. Shall be positive.
     */
    private final int areaWidth;
    
    /**
     * The height of the noised area, in pixels. Shall be positive.
     */
    private final int areaHeight;

    /**
     * The side of the square-shaped virtual chunks the area is divided into. Shall be
     * positive. Should be less than both <code>areaWidth</code> and
     * <code>areaHeight</code>.
     */
    private final int chunkSize;

    /**
     * The number of columns of the grid of the gradients.
     */
    private final int gradientCols;

    /**
     * The number of rows of the grid of the gradients.
     */
    private final int gradientRows;

    /**
     * The grid of the gradients. The gradients are stored as pairs of <i>x</i>
     * and <i>y</i> coordinates.
     */
    private final Map<Doublet<Integer>, Doublet<Double>> gradientVectors;

    /**
     * Number of octaves in noise generation. The default value is <code>1</code>.
     */
    private int octavesCount;

    /**
     * The parameter determining how successive octaves influence the final result.
     *
     * Shall be a positive fraction. The default value is <code>0.5</code>.
     */
    private double persistence;

    /**
     * The parameter determining how many times the area is fractally repeated. Shall be
     * greater than <code>1.0</code>. The default value is <code>2.0</code>.
     */
    private double lacunarity;

    /**
     * The lower bound of noise value.
     *
     * The default value is 0.
     */
    private double lowerBound;

    /**
     * The upper bound of noise value.
     *
     * The default value is 1.
     */
    private double upperBound;

    /**
     * The scalar (dot) product of vectors (x1, y1) and (x2, y2).
     *
     * @param x1 x-coordinate of the first vector
     * @param y1 y-coordinate of the first vector
     * @param x2 x-coordinate of the second vector
     * @param y2 y-coordinate of the second vector
     * @return the product of the vectors
     */
    private static double dotProduct(double x1, double y1, double x2, double y2)
    {
        return x1 * x2 + y1 * y2;
}

    /**
     * Linear interpolation.
     *
     * @param left first value of interpolation
     * @param right secondxvalue of interpolation
     * @param weight weight of the interpolation
     * @return
     */
    private static double lerp(double left, double right, double weight)
    {
        return left + weight * (right - left);
    }

    /**
     * Smoothstep function.
     */
    private static double smoothstep(double x)
    {
        return ((6d * x - 15d) * x + 10d) * x * x * x;
    }

    /**
     * Contructor.
     *
     * Initializes the fields and generates the gradient vectors.
     *
     * @param areaWidth width of the noised area, in pixels
     * @param areaHeight height of the noised area, in pixels
     * @param chunkSize side of the chunk
     */
    private PerlinNoise(int areaWidth, int areaHeight, int chunkSize)
    {
        assert (areaWidth > 0 && areaHeight > 0 && chunkSize > 0);
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.chunkSize = chunkSize;

        gradientCols = Math.ceilDiv(areaWidth, chunkSize) + 1;
        gradientRows = Math.ceilDiv(areaHeight, chunkSize) + 1;
        gradientVectors = new HashMap<>(gradientCols);
        Random random = new Random();
        for (int i = 0; i < gradientCols; ++i)
        {
            for (int j = 0; j < gradientRows; ++j)
            {
                double angle = random.nextDouble(0, Math.TAU);
                double xCoord = Math.cos(angle);
                double yCoord = Math.sin(angle);
                gradientVectors.put(new Doublet<>(i, j), new Doublet<>(xCoord, yCoord));
            }
        }
    }

    private double getRawNoise(Doublet<Integer> pixel)
    {
        // Naming:
        //   x, y - refer to global position within the area, counted in pixels
        //   p, q - refer to local position within the chunk, counted as fractions
        //   col, row - refer to indices
        //   a, b, c, d - refer to corners of the chunk, respectively:
        //     top left, top right, bottom left, bottom right.

        assert (pixel.left >= 0 && pixel.right >= 0);

        /* Adjust the coords. */
        pixel.left %= areaWidth;
        pixel.right %= areaHeight;

        /* Find the chunk's indices. */
        int chunkCol = pixel.left / chunkSize;
        int chunkRow = pixel.right / chunkSize;

        /* Find the coordinates of the corners. */
        int chunkLeftX = chunkCol * chunkSize;
        int chunkTopY = chunkRow * chunkSize;
        int chunkRightX = chunkLeftX + chunkSize;
        int chunkBottomY = chunkTopY + chunkSize;

        /* Find the local coordinates of the point. */
        double pixelLeftP = (double) (pixel.left - chunkLeftX) / (double) chunkSize;
        double pixelTopQ = (double) (pixel.right - chunkTopY) / (double) chunkSize;
        double pixelRightP = (double) (pixel.left - chunkRightX) / (double) chunkSize;
        double pixelBottomQ = (double) (pixel.right - chunkBottomY) / (double) chunkSize;

        /* Find the gradients. */
        var gradientA = gradientVectors.get(new Doublet<>(chunkCol, chunkRow));
        var gradientB = gradientVectors.get(new Doublet<>(chunkCol + 1, chunkRow));
        var gradientC = gradientVectors.get(new Doublet<>(chunkCol, chunkRow + 1));
        var gradientD = gradientVectors.get(new Doublet<>(chunkCol + 1, chunkRow + 1));

        /* Calculate the products. */
        double productA = dotProduct(pixelLeftP, pixelTopQ, gradientA.left, gradientA.right);
        double productB = dotProduct(pixelRightP, pixelTopQ, gradientB.left, gradientB.right);
        double productC = dotProduct(pixelLeftP, pixelBottomQ, gradientC.left, gradientC.right);
        double productD = dotProduct(pixelRightP, pixelBottomQ, gradientD.left, gradientD.right);

        /* Interpolate. */
        double horizontalTop = lerp(productA, productB, pixelLeftP);
        double horizontalBottom = lerp(productC, productD, pixelLeftP);

        return lerp(horizontalTop, horizontalBottom, pixelTopQ);
    }

    public List<Double> makeNoise(List<Doublet<Integer>> pixels)
    {
        List<Double> result = new ArrayList<>(pixels.size());
        double minimum = +Double.MAX_VALUE;
        double maximum = -Double.MAX_VALUE;
        for (var pixel : pixels)
        {
            assert (pixel.left >= 0 && pixel.left < areaWidth);
            assert (pixel.right >= 0 && pixel.right < areaHeight);

            double noise = getRawNoise(pixel);
            double frequency = lacunarity;
            double amplitude = persistence;
            for (int i = 1; i < octavesCount; ++i)
            {
                Doublet<Integer> newPixel = new Doublet<>(0, 0);
                newPixel.left = (int) (frequency * (double) pixel.left);
                newPixel.right = (int) (frequency * (double) pixel.right);
                noise += amplitude * getRawNoise(newPixel);
                frequency *= lacunarity;
                amplitude *= persistence;
            }
            result.add(noise);
            if (noise > maximum)
            {
                maximum = noise;
            }
            if (noise < minimum)
            {
                minimum = noise;
            }
        }

        if (minimum != maximum)
        {
            var prescaler = new Scaler(minimum, maximum, 0, 1);
            var scaler = new Scaler(0, 1, lowerBound, upperBound);
            for (int i = 0; i < result.size(); ++i)
            {
                double value = result.get(i);
                value = prescaler.transform(value);
                value = smoothstep(value);
                value = scaler.transform(value);
                result.set(i, value);
            }
        }
        else
        {
            // Do something, maybe.
        }
        return result;
    }

    public Map<Object, Double> makeNoise(Map<Object, Doublet<Integer>> pixels)
    {
        Map<Object, Double> result = new HashMap<>(pixels.size());
        double minimum = +Double.MAX_VALUE;
        double maximum = -Double.MAX_VALUE;
        var pixelsIterator = pixels.entrySet().iterator();
        while (pixelsIterator.hasNext())
        {
            var entry = pixelsIterator.next();
            Object key = entry.getKey();
            Doublet<Integer> pixel = entry.getValue();

            assert (pixel.left >= 0 && pixel.left < areaWidth);
            assert (pixel.right >= 0 && pixel.right < areaHeight);

            double noise = getRawNoise(pixel);
            double frequency = lacunarity;
            double amplitude = persistence;
            for (int i = 1; i < octavesCount; ++i)
            {
                Doublet<Integer> newPixel = new Doublet<>(0, 0);
                newPixel.left = (int) (frequency * (double) pixel.left);
                newPixel.right = (int) (frequency * (double) pixel.right);
                noise += amplitude * getRawNoise(newPixel);
                frequency *= lacunarity;
                amplitude *= persistence;
            }
            result.put(key, noise);
            if (noise > maximum)
            {
                maximum = noise;
            }
            if (noise < minimum)
            {
                minimum = noise;
            }
        }

        if (minimum != maximum)
        {
            var prescaler = new Scaler(minimum, maximum, 0, 1);
            var scaler = new Scaler(0, 1, lowerBound, upperBound);
            var resultIterator = result.entrySet().iterator();
            while (resultIterator.hasNext())
            {
                var entry = resultIterator.next();
                double value = entry.getValue();
                value = prescaler.transform(value);
                value = smoothstep(value);
                value = scaler.transform(value);
                entry.setValue(value);
            }
        }
        else
        {
            // Do something, maybe.
        }
        return result;
    }

    public static class Builder
    {
        private int areaWidth;
        private int areaHeight;
        private int chunkSize;
        private int octavesCount;
        private double persistence;
        private double lacunarity;
        private double lowerBound;
        private double upperBound;

        public Builder()
        {
            octavesCount = 1;
            persistence = 0.5;
            lacunarity = 2;
            lowerBound = 0;
            upperBound = 1;
        }

        public Builder setAreaWidth(int areaWidth) throws WrongValueException
        {
            if (areaWidth > 0)
            {
                this.areaWidth = areaWidth;
                return this;
            }
            else
            {
                throw new WrongValueException();
            }
        }

        public Builder setAreaHeight(int areaHeight) throws WrongValueException
        {
            if (areaHeight > 0)
            {
                this.areaHeight = areaHeight;
                return this;
            }
            else
            {
                throw new WrongValueException();
            }
        }

        public Builder setChunkSize(int chunkSize) throws WrongValueException
        {
            if (chunkSize > 0)
            {
                this.chunkSize = chunkSize;
                return this;
            }
            else
            {
                throw new WrongValueException();
            }
        }

        public Builder setOctavesCount(int octavesCount) throws WrongValueException
        {
            if (octavesCount > 0)
            {
                this.octavesCount = octavesCount;
                return this;
            }
            else
            {
                throw new WrongValueException();
            }
        }

        public Builder setPersistence(double persistence)
        {

            this.persistence = persistence;
            return this;
        }

        public Builder setLacunarity(double lacunarity) throws WrongValueException
        {
            if (lacunarity > 1)
            {
                this.lacunarity = lacunarity;
                return this;
            }
            else
            {
                throw new WrongValueException();
            }
        }

        public Builder setLowerBound(double lowerBound)
        {
            this.lowerBound = lowerBound;
            return this;
        }

        public Builder setUpperBound(double upperBound)
        {
            this.upperBound = upperBound;
            return this;
        }

        public PerlinNoise get()
        {
            if (areaWidth > 0 && areaHeight > 0 && chunkSize > 0)
            {
                PerlinNoise perlin = new PerlinNoise(areaWidth, areaHeight, chunkSize);
                perlin.octavesCount = octavesCount;
                perlin.persistence = persistence;
                perlin.lacunarity = lacunarity;
                perlin.lowerBound = lowerBound;
                perlin.upperBound = upperBound;

                return perlin;
            }
            else
            {
                return null;
            }
        }

        public static class WrongValueException extends Exception
        {
        }
    }
}
