package org.Query.Columnar;

import java.security.SecureRandom;

import java.util.UUID;
import java.util.Random;

public final class RockRand
{
	public static String GetGUID() {
		return UUID.randomUUID().toString();
	}

	public static final class NonAtomicRandom extends Random {
		private static final long serialVersionUID = 1L;

		private volatile long _seed;

		public NonAtomicRandom() {
			this(++SEED_UNIQUIFIER + System.nanoTime());
		}

		public NonAtomicRandom(final long seed) {
			setSeed(seed);
		}

		@Override
		public final int next(final int bits) {
			return (int) ((_seed = (_seed * MULTIPLIER + ADDEND) & MASK) >>> (48 - bits));
		}

		@Override
		public final void setSeed(final long seed) {
			_seed = (seed ^ MULTIPLIER) & MASK;
		}
	}

	public static final class RandomContainer {
		private final Random _random;

		private RandomContainer(final Random random) {
			_random = random;
		}

		public final Random directRandom() {
			return _random;
		}

		public final double get() {
			return _random.nextDouble();
		}

		public final int get(final int n) {
			return (int) (_random.nextDouble() * n);
		}

		public final int get(final int min, final int max) {
			return min + (int) (_random.nextDouble() * (max - min + 1));
		}

		public final long get(final long min, final long max) {
			return min + (long) (_random.nextDouble() * (max - min + 1));
		}

		public final boolean nextBoolean() {
			return _random.nextBoolean();
		}

		public final void nextBytes(final byte[] array) {
			_random.nextBytes(array);
		}

		public final double nextDouble() {
			return _random.nextDouble();
		}

		public final float nextFloat() {
			return _random.nextFloat();
		}

		public final double nextGaussian() {
			return _random.nextGaussian();
		}

		public final int nextInt() {
			return _random.nextInt();
		}

		public final long nextLong() {
			return _random.nextLong();
		}
	}

	public static enum RandomType {
		SECURE,
		UNSECURE_ATOMIC,
		UNSECURE_THREAD_LOCAL,
		UNSECURE_VOLATILE
	}

	public static final class ThreadLocalRandom extends Random
	{
		private static final class Seed {
			long _seed;

			Seed(final long seed) {
				setSeed(seed);
			}

			final int next(final int bits) {
				return (int) ((_seed = (_seed * MULTIPLIER + ADDEND) & MASK) >>> (48 - bits));
			}

			final void setSeed(final long seed) {
				_seed = (seed ^ MULTIPLIER) & MASK;
			}
		}

		private static final long serialVersionUID = 1L;

		private final ThreadLocal<Seed> _seedLocal;

		public ThreadLocalRandom() {
			_seedLocal = new ThreadLocal<Seed>() {
				@Override
				public final Seed initialValue() {
					return new Seed(++SEED_UNIQUIFIER + System.nanoTime());
				}
			};
		}

		public ThreadLocalRandom(final long seed) {
			_seedLocal = new ThreadLocal<Seed>() {
				@Override
				public final Seed initialValue() {
					return new Seed(seed);
				}
			};
		}

		@Override
		public final int next(final int bits) {
			return _seedLocal.get().next(bits);
		}

		@Override
		public final void setSeed(final long seed) {
			if (_seedLocal != null)
				_seedLocal.get().setSeed(seed);
		}
	}

	private final static long ADDEND = 0xBL;

	private final static long MASK = (1L << 48) - 1;

	private final static long MULTIPLIER = 0x5DEECE66DL;

	private static final RandomContainer rnd = newInstance(RandomType.UNSECURE_THREAD_LOCAL);

	private static volatile long SEED_UNIQUIFIER = 8682522807148012L;

	public static final Random directRandom() {
		return rnd.directRandom();
	}

	public static final double get() {
		return rnd.nextDouble();
	}

	public static final int get(final int n) {
		return rnd.get(n);
	}

	public static final int get(final int min, final int max) {
		return rnd.get(min, max);
	}

	public static final long get(final long min, final long max) {
		return rnd.get(min, max);
	}

	public static final long getUnsignedLong() {
		return RockRand.get(0L, 0x7ffffffffffffffeL);
	}

	public static final long getUnsignedLongRound(int min, int max) {
		return RockRand.get(min, max);
	}

	public static final RandomContainer newInstance(final RandomType type) {
		switch (type) {
			case UNSECURE_ATOMIC:
				return new RandomContainer(new Random());

			case UNSECURE_VOLATILE:
				return new RandomContainer(new NonAtomicRandom());

			case UNSECURE_THREAD_LOCAL:
				return new RandomContainer(new ThreadLocalRandom());

			case SECURE:
				return new RandomContainer(new SecureRandom());
		}

		throw new IllegalArgumentException();
	}

	public static final boolean nextBoolean() {
		return rnd.nextBoolean();
	}

	public static final void nextBytes(final byte[] array) {
		rnd.nextBytes(array);
	}

	public static final double nextDouble() {
		return rnd.nextDouble();
	}

	public static final float nextFloat() {
		return rnd.nextFloat();
	}

	public static final double nextGaussian() {
		return rnd.nextGaussian();
	}

	public static final int nextInt() {
		return rnd.nextInt();
	}

	public static final int nextInt(final int n) {
		return get(n);
	}

	public static final long nextLong() {
		return rnd.nextLong();
	}
}
