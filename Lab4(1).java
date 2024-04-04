// Name: Eva Santana 
// ID: 42303398 
// HonorCode: I pledge that this is my work and I have not given nor recieved help from anyone

import java.io.*; 
import java.util.*; 

public class Lab4 {
    public static void main(String[] args) {
      System.out.println("Receivefile1");
      System.out.println("_______________________________");
      processFile("src/main/java/receivefile1.bin"); 

      System.out.println("Receivefile2");
      System.out.println("_______________________________");
      processFile("src/main/java/receivefile2.bin"); 

      System.out.println("Receivefile3");
      System.out.println("_______________________________");
      processFile("src/main/java/receivefile3.bin"); 

      System.out.println("Receivefile4");
      System.out.println("_______________________________");
      processFile("src/main/java/receivefile4.bin");
    } 

  public static boolean isPowerOfTwo(int n) {
    return (n & (n - 1)) == 0;
  }


  public static void processFile(String filePath) {
    String binaryString = "";

    // Reads transmitfile.bin
    try (FileInputStream fis = new FileInputStream("src/main/java/transmitfile.bin")) {
      // Stores content of the file 
        byte[] fileContent = fis.readAllBytes();
        binaryString = new String(fileContent);

    } catch (IOException e) {
        System.err.println("Error reading 'transmitfile.bin'. Make sure the file is a .bin file.");
    }

    // Calculate the size of the transmitted file 
    int transmitFileSize = binaryString.length(); 
    System.out.println("Transmitted file content: " + binaryString);
    System.out.println("total number of bytes read: " + transmitFileSize + " bytes\n");


    // Calculate M and K values 
    int m = binaryString.length(); 
    int k = 0; 
    while (Math.pow(2, k) < m + k + 1) {
      k++;
    }
    m = m  - k; 
    System.out.println("M data bits is: " + m);
    System.out.println("K check bits is: " + k + "\n");


    // Initialize lists to store check bit locations and values
    List<Integer> kCheckBitLocations = new ArrayList<>();
    List<Integer> kCheckBitValues = new ArrayList<>();
    // Iterate through binary string to find check bits
    for (int i = 0; i < binaryString.length(); i++) {
        // Check if the position is a power of two indicating a check bit location
        if (isPowerOfTwo(i + 1)) {
            kCheckBitLocations.add(i);
        }

    }
    // Reverse the binaryString
    StringBuilder sb = new StringBuilder(binaryString);
    String reversebinaryString = sb.reverse().toString();

    // Iterate through reversebinaryString to find check bit values
    for (int i = 0; i < reversebinaryString.length(); i++) {
        if (kCheckBitLocations.contains(i)) {
            kCheckBitValues.add(Character.getNumericValue(reversebinaryString.charAt(i)));
        }
    }
    // Reverse the kCheckBitValues list
    Collections.reverse(kCheckBitValues);

    System.out.println("Location of the k check bits are: " + kCheckBitLocations);
    System.out.println("The k check bit values are: " + kCheckBitValues + "\n");


    String recBinaryString = "";

    // Reads Recievefile.bin
    try (FileInputStream fis = new FileInputStream(filePath)) {
      // Stores content of the file 
        byte[] fileContent = fis.readAllBytes();
        recBinaryString = new String(fileContent);

    } catch (IOException e) {
        System.err.println("Error reading 'recievefile.bin'. Make sure the file is a .bin file.");
    }

    // Calculate the size of the recieved file 
    int recieveFileSize = recBinaryString.length(); 
    System.out.println("Received file content: " + recBinaryString);
    System.out.println("Total number of bytes read " + recieveFileSize + " bytes\n");

    // Check if revievedfile and transmitfile are different sizes 
    if (transmitFileSize != recieveFileSize) {
      System.out.println("Files are not the same size!");
      return;
    }

    // Calculate the location and values of check bits for received file
    List<Integer> receivedKCheckBitValues = new ArrayList<>();

    // Reverse the binaryString
    StringBuilder ab = new StringBuilder(recBinaryString);
    String reverseRecBinaryString = ab.reverse().toString();

    // Iterate through reversebinaryString to find check bit values
    for (int i = 0; i < reverseRecBinaryString.length(); i++) {
        if (kCheckBitLocations.contains(i)) {
              receivedKCheckBitValues.add(Character.getNumericValue(reverseRecBinaryString.charAt(i)));
        }
    }
    // Reverse the kCheckBitValues list
    Collections.reverse(receivedKCheckBitValues);


    System.out.println("Location of the k check bits are: " + kCheckBitLocations);
    System.out.println("The k check bit values are: " + receivedKCheckBitValues + "\n");

    // Calculate the syndrome word
    List<Integer> syndromeWord = new ArrayList<>();

    // XOR each index of kCheckBitValues and receivedKCheckBitValues
    for (int i = 0; i < kCheckBitValues.size(); i++) {
        int xorResult = kCheckBitValues.get(i) ^ receivedKCheckBitValues.get(i);
        syndromeWord.add(xorResult);
    }

    int temp = 0; 

    for (int i = 0; i < syndromeWord.size(); i++) {
        temp += syndromeWord.get(i);
    }

    System.out.println("The syndrome word is: " + syndromeWord);

    if (temp == 0) {
      System.out.println("No error detected in the received file.");
    } else if (temp == 1) {
      System.out.println("Only one syndrome bit is set to 1. The error bit is one of the check bits. No correction needed"); 
    } else {

      // Reverse the syndromeWord list
      List<Integer> reversedSyndromeWord = new ArrayList<>(syndromeWord);
      Collections.reverse(reversedSyndromeWord);

      // Calculate the error position from the reversed syndrome word
      int errorPosition = 0;
      for (int i = 0; i < reversedSyndromeWord.size(); i++) {
          if (reversedSyndromeWord.get(i) == 1) {
              errorPosition += Math.pow(2, i);
          }
      }

      System.out.println("The location of the error bit in the received data is: " + (int)errorPosition);

    }

  }
 
  
}