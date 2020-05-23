package org.Query.Columnar;

public class BLE
{
	byte[] encr, decr;
	int ctr;

	public BLE(int size) {
		encr = new byte[size];
		decr = new byte[size];
	}

	public byte[] Decrypt() {
		byte val = 0;

		for (int i = 0; i < ctr; i++) {
			for (int k = 0; k < encr[i]; k++) {
				decr[k] = val;
			}

			if (val == 0) {
				val = 1;
			} else {
				val = 0;
			}
		}
		return decr;
	}

	public byte[] Encrypt(byte[] binary, int sum) {
		ctr = 0;
		byte counter = 0;

		for (int i = 0; i < sum; i++) {
			if (i == 0) {
				if (binary[i] == '1') {
					encr[ctr] = counter;

					ctr++;
					counter++;
				} else {
					counter++;
				}
			} else {
				if (binary[i] == binary[i - 1]) {
					counter++;
				} else {
					encr[ctr] = counter;
					ctr++;

					counter = 1;
				}
			}
		}

		encr[ctr] = counter;
		ctr++;
		return encr;
	}
}