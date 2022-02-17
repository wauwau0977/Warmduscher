package com.x8ing.thsensor.thserver.utils;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class Base58BitcoinFlavorTest {

    @Test
    public void encodeAndDecode() {


        checkString(1);
        checkString(4);
        checkString(16);
        checkString(1024);

    }

    private void checkString(int length) {

        System.out.println("\nTest length " + length + " bytes");

        byte[] randomBytes = new byte[length];
        new Random().nextBytes(randomBytes);

        System.out.println("BYTES_IN:  " + Arrays.toString(randomBytes));

        String shortString = Base58BitcoinFlavor.encode(randomBytes);

        assertFalse(StringUtils.containsAny(shortString, new char[]{',', '.', '>', '\'', '"'}));

        System.out.println("SHORT_STRING: " + shortString);

        byte[] decodedBytes = Base58BitcoinFlavor.decode(shortString);

        System.out.println("BYTES_OUT: " + Arrays.toString(randomBytes));

        assertEquals( Arrays.toString(randomBytes), Arrays.toString(decodedBytes),"Bytes must match");

        System.out.println("\n");
    }

    @Test
    public void checkPerformance() {

        final int loops = 1000;

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            byte[] randomBytes = new byte[64];
            new Random().nextBytes(randomBytes);
            String encode = Base58BitcoinFlavor.encode(randomBytes);
            byte[] decodedBytes = Base58BitcoinFlavor.decode(encode);
            assertEquals(Arrays.toString(randomBytes), Arrays.toString(decodedBytes));
        }

        long dt = System.currentTimeMillis() - t0;

        System.out.println("Test iteration for " + loops + "  took " + dt + " ms.  1 encoding/decoding in " + (1.0 * dt / loops) + " ms.");
    }


    @Test
    public void generateShortTextUUID() {
        for (int i = 0; i < 20; i++) {
            System.out.println(UUIDUtils.generateShortTextUUID());
        }

    }

    @Test
    public void checkUnicodeToUTF() {
        String testString = "My Unicode String with special charcs � � � \\ \" = / , '   ";
        String base58 = Base58BitcoinFlavor.encodeUnicodeStringToBase58String(testString);

        System.out.println("\nEncoded the test String to base58");
        System.out.println("base:    " + testString);
        System.out.println("encoded: " + base58);

        assertFalse(StringUtils.containsAny(base58, new char[]{',', '�', '\\', '"'}));

        assertEquals(testString, Base58BitcoinFlavor.decodeBase58ToUnicodeString(base58));
    }

    @Test
    public void checkUnicodeToUTF2() {

        for (int i = 0; i < 25; i++) {
            String testString = RandomStringUtils.random(30);
            String base58 = Base58BitcoinFlavor.encodeUnicodeStringToBase58String(testString);

            System.out.println("\nEncoded the test String to base58");
            System.out.println("base:    " + testString);
            System.out.println("encoded: " + base58);

            assertFalse(StringUtils.containsAny(base58, new char[]{',', '�', '\\', '"'}));
            assertTrue(base58.matches("[a-zA-Z0-9]+"));

            assertEquals(testString, Base58BitcoinFlavor.decodeBase58ToUnicodeString(base58));
        }
    }

}