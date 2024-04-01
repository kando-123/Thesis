/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.world;

import java.util.*;

class Scaler
{
    private final double slope;
    private final double intercept;

    public Scaler(double xMin, double xMax, double yMin, double yMax)
    {
        slope = (yMax - yMin) / (xMax - xMin);
        intercept = yMin - slope * xMin;
    }

    public double transform(double x)
    {
        return slope * x + intercept;
    }
}

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
     * The side of the square-shaped virtual chunks the area is divided into.
     * Shall be positive. Should be less than both <code>areaWidth</code> and
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
    private final Map<Pixel, Point> gradientVectors;

    /**
     * Number of octaves in noise generation. The default value is
     * <code>1</code>.
     */
    private int octavesCount;

    /**
     * The parameter determining how successive octaves influence the final
     * result.
     *
     * Shall be a positive fraction. The default value is <code>0.5</code>.
     */
    private double persistence;

    /**
     * The parameter determining how many times the area is fractally repeated.
     * Shall be greater than <code>1.0</code>. The default value is
     * <code>2.0</code>.
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
    private double dotProduct(double x1, double y1, double x2, double y2)
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
    private double lerp(double left, double right, double weight)
    {
        return left + weight * (right - left);
    }

    /**
     * Smoothstep function.
     */
    private double smoothstep(double x)
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
    public PerlinNoise(int areaWidth, int areaHeight, int chunkSize)
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
                double angle = random.nextDouble(0.0d, Math.TAU);
                double xCoord = Math.cos(angle);
                double yCoord = Math.sin(angle);
                gradientVectors.put(new Pixel(i, j), new Point(xCoord, yCoord));
            }
        }
        octavesCount = 1;
        persistence = 0.5f;
        lacunarity = 2.0f;
        lowerBound = 0d;
        upperBound = 1d;
    }

    /**
     * Overwrites the octaves count.
     *
     * The new value shall be positive. It should not be "too big" (a few
     * octaves should be fine).
     *
     * In case of a non-positive value, an exception is thrown.
     *
     * @param newValue new octaves count
     */
    public void setOctaves(int newValue)
    {
        if (newValue > 0)
        {
            octavesCount = newValue;
        }
    }

    public void setPersistence(double newValue)
    {
        if (newValue > 0.0d && newValue < 1.0d)
        {
            persistence = newValue;
        }
    }

    public void setLacunarity(double newValue)
    {
        if (newValue > 1.0d)
        {
            lacunarity = newValue;
        }
    }

    public void setBounds(double lower, double upper)
    {
        lowerBound = lower;
        upperBound = upper;
    }

    private double getRawNoise(Pixel pixel)
    {
        /* Naming */
        // x, y - refer to global position within the area, counted in pixels
        // p, q - refer to local position within the chunk, counted as fractions
        // col, row - refer to indices
        // a, b, c, d - refer to corners of the chunk, respectively:
        //  top left, top right, bottom left, bottom right.

        assert (pixel.xCoord >= 0 && pixel.yCoord >= 0);

        /* Adjust the coords. */
        pixel.xCoord %= areaWidth;
        pixel.yCoord %= areaHeight;

        /* Find the chunk's indices. */
        int chunkCol = pixel.xCoord / chunkSize;
        int chunkRow = pixel.yCoord / chunkSize;

        /* Find the coordinates of the corners. */
        int chunkLeftX = chunkCol * chunkSize;
        int chunkTopY = chunkRow * chunkSize;
        int chunkRightX = chunkLeftX + chunkSize;
        int chunkBottomY = chunkTopY + chunkSize;

        /* Find the local coordinates of the point. */
        double pixelLeftP = (double) (pixel.xCoord - chunkLeftX) / (double) chunkSize;
        double pixelTopQ = (double) (pixel.yCoord - chunkTopY) / (double) chunkSize;
        double pixelRightP = (double) (pixel.xCoord - chunkRightX) / (double) chunkSize;
        double pixelBottomQ = (double) (pixel.yCoord - chunkBottomY) / (double) chunkSize;

        /* Find the gradients. */
        var gradientA = gradientVectors.get(new Pixel(chunkCol, chunkRow));
        var gradientB = gradientVectors.get(new Pixel(chunkCol + 1, chunkRow));
        var gradientC = gradientVectors.get(new Pixel(chunkCol, chunkRow + 1));
        var gradientD = gradientVectors.get(new Pixel(chunkCol + 1, chunkRow + 1));

        /* Calculate the products. */
        double productA = dotProduct(pixelLeftP, pixelTopQ, gradientA.xCoord, gradientA.yCoord);
        double productB = dotProduct(pixelRightP, pixelTopQ, gradientB.xCoord, gradientB.yCoord);
        double productC = dotProduct(pixelLeftP, pixelBottomQ, gradientC.xCoord, gradientC.yCoord);
        double productD = dotProduct(pixelRightP, pixelBottomQ, gradientD.xCoord, gradientD.yCoord);

        /* Interpolate. */
        double horizontalTop = lerp(productA, productB, pixelLeftP);
        double horizontalBottom = lerp(productC, productD, pixelLeftP);

        return lerp(horizontalTop, horizontalBottom, pixelTopQ);
    }

    public List<Double> makeNoise(List<Pixel> pixels) throws Exception
    {
        boolean success = true;

        List<Double> result = new ArrayList<>(pixels.size());
        double minimum = Double.MAX_VALUE;
        double maximum = Double.MIN_VALUE;
        for (var pixel : pixels)
        {
            if (pixel.xCoord < 0 || pixel.xCoord >= areaWidth || pixel.yCoord < 0 || pixel.yCoord >= areaHeight)
            {
                success = false;
                break;
            }
            double noise = getRawNoise(pixel);
            double frequency = lacunarity;
            double amplitude = persistence;
            for (int i = 1; i < octavesCount; ++i)
            {
                Pixel newPixel = new Pixel(0, 0);
                newPixel.xCoord = (int) (frequency * (double) pixel.xCoord);
                newPixel.yCoord = (int) (frequency * (double) pixel.yCoord);
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

        if (success)
        {
            if (minimum != maximum)
            {
                var prescaler = new Scaler(minimum, maximum, 0d, 1d);
                var scaler = new Scaler(0d, 1d, lowerBound, upperBound);
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
        else
        {
            throw new Exception("PerlinNoise.makeNoise");
        }
    }
    
    public Map<Object, Double> makeNoise(Map<Object, Pixel> pixels) throws Exception
    {
        boolean success = true;

        Map<Object, Double> result = new HashMap<>(pixels.size());
        double minimum = Double.MAX_VALUE;
        double maximum = Double.MIN_VALUE;
        var pixelsIterator = pixels.entrySet().iterator();
        while (pixelsIterator.hasNext())
        {
            var entry = pixelsIterator.next();
            Object key = entry.getKey();
            Pixel pixel = entry.getValue();
            if (pixel.xCoord < 0 || pixel.xCoord >= areaWidth || pixel.yCoord < 0 || pixel.yCoord >= areaHeight)
            {
                success = false;
                break;
            }
            double noise = getRawNoise(pixel);
            double frequency = lacunarity;
            double amplitude = persistence;
            for (int i = 1; i < octavesCount; ++i)
            {
                Pixel newPixel = new Pixel(0, 0);
                newPixel.xCoord = (int) (frequency * (double) pixel.xCoord);
                newPixel.yCoord = (int) (frequency * (double) pixel.yCoord);
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

        if (success)
        {
            if (minimum != maximum)
            {
                var prescaler = new Scaler(minimum, maximum, 0d, 1d);
                var scaler = new Scaler(0d, 1d, lowerBound, upperBound);
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
        else
        {
            throw new Exception("PerlinNoise.makeNoise");
        }
    }
}
