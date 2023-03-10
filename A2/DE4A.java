// DE4A.java CS5125/6025 cheng 2023
// A UDP server holding a UDP port on a host
// It uses the DH Group 5 q and alpha=2 to generate a Diffie-Hellman key pair.
// When a UDP datagram is received from a client, it interprets the content as
// the public key of the client under the same Group 5 system and
// sends its public key back.
// It computes the shared secret and prints it out.
// Usage: java DE4A serverPort
// Use a port number that is not well-known (an unsigned short larger than 1000)

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

public class DE4A {

  static int MAXBF = 1024;
  String hexQ = null;
  BigInteger q = null;
  static BigInteger alpha = new BigInteger("2");
  BigInteger privateKey;
  BigInteger publicKey;
  BigInteger clientPublicKey;
  byte[] publicKeyBytes = null;
  BigInteger preMasterSecret;
  String hexkey = null;

  void readQ(String filename) {
    Scanner in = null;
    try {
      in = new Scanner(new File(filename));
    } catch (FileNotFoundException e) {
      System.err.println(filename + " not found");
      System.exit(1);
    }
    hexQ = in.nextLine();
    in.close();
    q = new BigInteger(hexQ, 16);
  }

  void generateKeyPair() {
    Random random = new Random();
    privateKey = new BigInteger(1235, random);
    publicKey = alpha.modPow(privateKey, q);
    publicKeyBytes = publicKey.toByteArray();
  }

  void runUDPServer(int serverPort) {
    DatagramSocket ds = null;
    DatagramPacket dp = null;
    byte[] buff = new byte[MAXBF];
    try {
      ds = new DatagramSocket(serverPort);
      dp = new DatagramPacket(buff, MAXBF);
      ds.receive(dp); // blocking until receiving
      int len = dp.getLength();
      byte[] clientPublicKeyBytes = new byte[len];
      for (int i = 0; i < len; i++) clientPublicKeyBytes[i] = buff[i];
      clientPublicKey = new BigInteger(clientPublicKeyBytes);
      InetAddress iadd = dp.getAddress(); // client's IP address
      int clientPort = dp.getPort();
      System.out.println(" from " + iadd.getHostAddress() + ":" + clientPort);
      dp =
        new DatagramPacket(
          publicKeyBytes,
          publicKeyBytes.length,
          iadd,
          clientPort
        );
      ds.send(dp);
    } catch (IOException e) {
      System.err.println("IOException");
      return;
    }
  }

  void computeSharedSecret() {
    preMasterSecret = clientPublicKey.modPow(privateKey, q); // public^private mod q
    hexkey = preMasterSecret.toString(16);
    System.out.println(hexkey);
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java DE4A port");
      System.exit(1);
    }
    DE4A de4 = new DE4A();
    de4.readQ("DHgroup5.txt");
    de4.generateKeyPair();
    de4.runUDPServer(Integer.parseInt(args[0]));
    de4.computeSharedSecret();
  }
}
