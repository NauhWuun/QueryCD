package org.Query.Columnar.rollinghash;

public class CyclicHash {
	public final static int wordsize = 32;
	static CharacterHash hasher = CharacterHash.getInstance();
	public int hashvalue;
	int n;
	int myr;

	// myn is the length in characters of the blocks you want to hash
	public CyclicHash(int myn) {
		n = myn;
		if (n > wordsize) {
			throw new IllegalArgumentException();
		}

	}

	private static int fastleftshift1(int x) {
		return (x << 1) | (x >>> (wordsize - 1));
	}

	// this is purely for testing purposes
	public static int nonRollingHash(CharSequence s) {
		int value = 0;
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			int z = hasher.hashvalues[c];
			value = fastleftshift1(value) ^ z;
		}
		return value;
	}

	private int fastleftshiftn(int x) {
		return (x << n) | (x >>> (wordsize - n));
	}

	// add new character (useful to initiate the hasher)
	// to get a strongly universal hash value, you have to ignore the last or first (n-1) bits.
	public int eat(char c) {
		hashvalue = fastleftshift1(hashvalue);
		hashvalue ^= hasher.hashvalues[c];
		return hashvalue;
	}

	// remove old character and add new one
	// to get a strongly universal hash value, you have to ignore the last or first (n-1) bits.
	public int update(char outchar, char inchar) {
		int z = fastleftshiftn(hasher.hashvalues[outchar]);
		hashvalue = fastleftshift1(hashvalue) ^ z ^ hasher.hashvalues[inchar];
		return hashvalue;
	}

}