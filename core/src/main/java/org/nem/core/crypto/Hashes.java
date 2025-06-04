package org.nem.core.crypto;

import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.nem.core.utils.ExceptionUtils;

/**
 * Static class that exposes hash functions.
 */
public class Hashes {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Supported hashing algorithms.
	 */
	public enum Algorithm {
		SHA3_256("Keccak-256", 32),
		SHA3_512("Keccak-512", 64),
		RIPEMD160("RIPEMD160", 20),
		BLAKE2B_256("BLAKE2B-256", 32);

		private final String name;
		private final int length;

		Algorithm(final String name, final int length) {
			this.name = name;
			this.length = length;
		}

		public String algorithm() {
			return this.name;
		}

		public int length() {
			return this.length;
		}
	}

	/**
	 * Performs a SHA3-256 hash of the concatenated inputs.
	 *
	 * @param inputs The byte arrays to concatenate and hash.
	 * @return The hash of the concatenated inputs.
	 * @throws CryptoException if the hash operation failed.
	 */
       public static byte[] sha3_256(final byte[]... inputs) {
	       return hash(Algorithm.SHA3_256, inputs);
       }

	/**
	 * Performs a SHA3-512 hash of the concatenated inputs.
	 *
	 * @param inputs The byte arrays to concatenate and hash.
	 * @return The hash of the concatenated inputs.
	 * @throws CryptoException if the hash operation failed.
	 */
       public static byte[] sha3_512(final byte[]... inputs) {
	       return hash(Algorithm.SHA3_512, inputs);
       }

	/**
	 * Performs a RIPEMD160 hash of the concatenated inputs.
	 *
	 * @param inputs The byte arrays to concatenate and hash.
	 * @return The hash of the concatenated inputs.
	 * @throws CryptoException if the hash operation failed.
	 */
       public static byte[] ripemd160(final byte[]... inputs) {
	       return hash(Algorithm.RIPEMD160, inputs);
       }

       /**
	* Performs a BLAKE2b-256 hash of the concatenated inputs.
	*
	* @param inputs The byte arrays to concatenate and hash.
	* @return The hash of the concatenated inputs.
	* @throws CryptoException if the hash operation failed.
	*/
       public static byte[] blake2b_256(final byte[]... inputs) {
	       return hash(Algorithm.BLAKE2B_256, inputs);
       }

       private static byte[] hash(final Algorithm algorithm, final byte[]... inputs) {
	       return ExceptionUtils.propagate(() -> {
		       final MessageDigest digest = MessageDigest.getInstance(algorithm.algorithm(), "BC");

			for (final byte[] input : inputs) {
				digest.update(input);
			}

			return digest.digest();
		}, CryptoException::new);
	}
}
