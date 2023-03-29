// DE16B.java CS5125/6025 Cheng 2023
// Decoding Burrows-Wheeler
// Usage: java DE16B < transformed(with DE16A) > original

import java.io.*;
import java.lang.*;

class DE16B{
  static int BLOCKSIZE = 8192;
  static int numberOfSymbols = 256;
  int length = 0;  // length of block 
  int[] A = new int[numberOfSymbols]; 
  int[] L = new int[BLOCKSIZE];  // the Burrows-Wheeler transformation
  int[] F = new int[BLOCKSIZE]; 
  int[] T = new int[BLOCKSIZE];
  int I;  // the position of original after suffix sort

 void initializeA(){
    for (int i = 0; i < numberOfSymbols; i++) A[i] = i;    
 }
  
 void readBlock(){
   byte[] buffer = new byte[BLOCKSIZE + 2];
   try{
     length = System.in.read(buffer) - 2;
   }catch(IOException e){
      System.err.println(e.getMessage());
      System.exit(1);
   }
   if (length <= 0) return;
   I = Byte.toUnsignedInt(buffer[0]) * 256 + Byte.toUnsignedInt(buffer[1]);
   for (int i = 0; i < length; i++){
      int j = Byte.toUnsignedInt(buffer[i + 2]);
      int t = A[j];
      L[i] = t;
      for (int k = j; k > 0; k--) A[k] = A[k-1]; 
      A[0] = t; // move to front
   }
 }

 void deBW(){
   for (int i = 0; i < length; i++){
     int j = i - 1; for (; j >= 0; j--)
       if (L[i] < F[j]) F[j + 1] = F[j];
       else break;
     F[j + 1] = L[i];
   }
   int j = 0;
   for (int i = 0; i < length; i++){
    if (i > 0 && F[i] > F[i - 1]) j = 0;
    for (; j < length; j++) if (L[j] == F[i]) break;
    T[i] = j++;
   }

   System.out.println();
   System.out.println(String.format("I: %d", I));
   System.out.println(String.format("T length: %d\n", T.length));

   int transition = T[I];
   for (int i = 0; i < length; i++){
    int b = F[transition];
    System.out.write(b);
    transition = T[transition];
   }

   // Now we have I, L, F, and T 
   // Your code here for printing the decoded block using System.out.write().
   // Write one byte a time
 }

 void decode(){
   initializeA();
   while (true){
     readBlock();
     if (length <= 0) return;
     deBW();
     if (length < BLOCKSIZE) return;
   }
 }

 public static void main(String[] args){
  DE16B de16 = new DE16B();
  de16.decode();
 }
}


