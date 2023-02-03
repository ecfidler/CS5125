// DE6A.java CS5125/6025 Cheng 2023
// RSA Key generation, encryption, and decryption
// Usage:  java DE6A

// import java.lang.*;
import java.math.*;
import java.util.*;

class DE6A {

  Random rand = new Random();
  BigInteger p = new BigInteger(1024, 200, rand);
  BigInteger q = new BigInteger(1024, 200, rand);
  BigInteger n = p.multiply(q);
  BigInteger phi = p
    .subtract(BigInteger.ONE)
    .multiply(q.subtract(BigInteger.ONE));
  BigInteger e = new BigInteger("65537");
  BigInteger d = e.modInverse(phi);

  void printKeys() {
    System.out.println("e: " + e.toString(16));
    System.out.println("n: " + n.toString(16));
    System.out.println("d: " + d.toString(16));
  }

  void encryption() {
    BigInteger M = new BigInteger(2040, rand);
    BigInteger C = M.modPow(e, n);
    BigInteger M2 = C.modPow(d, n);
    System.out.println("plaintext M: " + M.toString(16));
    System.out.println("ciphertext C: " + C.toString(16));
    System.out.println("decrypted plaintext: " + M2.toString(16));
  }
}

public class AFour {
  public static void main(String[] args) {
    DE6A de6 = new DE6A();
    de6.printKeys();
    de6.encryption();
  }
}