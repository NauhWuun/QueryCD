package org.Query.Columnar.rollinghash;

import java.util.Random;

public class CharacterHash {
	static CharacterHash charhash = new CharacterHash();
	public int hashvalues[] = new int[1 << 16];

	public CharacterHash() {
		Random r = new Random();
		for (int k = 0; k < hashvalues.length; ++k)
			hashvalues[k] = r.nextInt();
	}

	public static CharacterHash getInstance() {
		return charhash;
	}
}
