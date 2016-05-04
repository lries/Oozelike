package oozelike.generators;

import oozelike.roguelike.Core;

public class NoiseFactory {
	/**************************************************************************
	 * Distort a noise map using a perlin noise offset. 
	 * It will be equally "clumpy" but in a less patterned way
	 * @param map the noise to distort
	 * @param degrees the amount to distort by
	 * @param octaves the octaves to use for the perlin noise
	 * @param xof whether to disort in both directions instead of just one 
	 * @return the new float[][]
	 **************************************************************************/
	public static float[][] distortNoiseMap (float[][] map, int degrees, int octaves, boolean xof){
		//Distorts a map. 
		float[][] newMap = new float[map.length][map[0].length];
		float[][] xOff = genPerlinNoise(genWhiteNoise(newMap.length, newMap[0].length), octaves);
		float[][] yOff = genPerlinNoise(genWhiteNoise(newMap.length, newMap[0].length), octaves);
		int xo, yo;
		for (int x=0; x<map.length; x++){
			for (int y=0; y<map[0].length; y++){
				if (xof) xo=(int) (x+xOff[x][y]*degrees); 
				else xo=x;
				yo=(int) (y+yOff[x][y]*degrees); 
				while (xo >= map.length) xo -= map.length;
				while (yo >= map[0].length) yo -= map[0].length;
				newMap[x][y]=map[xo][yo];
			}
		}
		return newMap;
	}

	/***********************************************
	 * Generate white noise
	 * @param width the width of the noise map
	 * @param height the height of the noise map
	 * @return a correctly sized float[][] of noise
	 ***********************************************/
	public static float[][] genWhiteNoise (int width, int height){
		float[][] noise = new float[width][height];
		for (int x=0; x<width; x++){
			for (int y=0; y<height; y++){
				noise[x][y] = (float) Core.rand.nextDouble();
			}
		}
		return noise;
	}

	/*************************************************************
	 * Smooths noise (helper for Perlin noise)
	 * @param baseNoise the noise to smooth
	 * @param octave the octave to smooth by (higher=more smooth)
	 * @return the smoothed noise
	 *************************************************************/
	private static float[][] genSmoothNoise(float[][] baseNoise, int octave){
		//Smooths noise out
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		float[][] smoothNoise = new float[width][height];

		int samplePeriod = 1<<octave;
		float sampleFrequency = 1f/samplePeriod;

		for (int i=0; i<width; i++){
			int si0 = (i/samplePeriod)*samplePeriod;
			int si1 = (si0 + samplePeriod) % width;
			float hblend = (i - si0) * sampleFrequency;

			for (int j=0; j<height; j++){
				int sj0 = (j/samplePeriod)*samplePeriod;
				int sj1 = (sj0 + samplePeriod) % height;
				float vblend = (j - sj0) * sampleFrequency;

				float top = interpolate(baseNoise[si0][sj0], baseNoise[si1][sj0], hblend);
				float bottom = interpolate(baseNoise[si0][sj1], baseNoise[si1][sj1], hblend);
				smoothNoise[i][j] = interpolate(top, bottom, vblend);
			}
		}
		return smoothNoise;
	}

	/***************************************************************
	 * Generate perlin noise.
	 * @param baseNoise white noise to be the base
	 * @param octaveCount the number of octaves (higher = smoother)
	 * @return perlin noise of the size of baseNoise
	 ***************************************************************/
	public static float[][] genPerlinNoise(float[][] baseNoise, int octaveCount){
		//Generates Perlin noise
		//More octaves = smoother; anything less than 7 or more than 10 is probably useless for a heightmap
		//Although you get tons of islands with o=6; that can be cool
		System.out.println("perlin?");
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		float[][][] smoothNoise = new float[octaveCount][][];
		float persistance = 0.5f;

		for (int i=0; i<octaveCount; i++){
			smoothNoise[i] = genSmoothNoise(baseNoise, i);
		}

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;
		for (int o = octaveCount-1; o>=0; o--){
			amplitude*=persistance;
			totalAmplitude += amplitude;
			for (int i=0; i<width; i++){
				for (int j=0; j<height; j++){
					perlinNoise[i][j] += smoothNoise[o][i][j]*amplitude;
				}
			}
		}
		for (int i = 0; i<width; i++)
		{
			for (int j = 0; j<height; j++)
			{
				perlinNoise[i][j] /= totalAmplitude;
			}
		}
		return perlinNoise;
	}

	/*********************************************************
	 * Interpolate noise between two points
	 * @param x0 point 1
	 * @param x1 point 2
	 * @param alpha noise value
	 * @return interpolated noise value
	 **********************************************************/
	private static float interpolate(float x0, float x1, float alpha) {
		return x0*(1-alpha) + alpha*x1;
	}
}
