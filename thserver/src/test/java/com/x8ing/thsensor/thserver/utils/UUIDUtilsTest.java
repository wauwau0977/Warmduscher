package com.x8ing.thsensor.thserver.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UUIDUtilsTest {

    @Test
    public void convert1() {

        UUID uuidIn = UUID.randomUUID();

        System.out.println("UUID In: " + uuidIn);

        String shortUUID = UUIDUtils.toShortText(uuidIn);
        System.out.println("Short UUID: " + shortUUID);


        UUID uuidOut = UUIDUtils.fromShortText(shortUUID);
        System.out.println("UUID In: " + uuidIn);

        assertEquals(uuidIn, uuidOut);

        assertEquals(uuidIn.toString(), uuidOut.toString());

    }

    @Test
    public void convertNull() {

        String shortUUID = UUIDUtils.toShortText(null);
        System.out.println("Short UUID: " + shortUUID);
        UUID uuidOut = UUIDUtils.fromShortText(shortUUID);

        assertNull(uuidOut);

    }

    @Test
    public void testPrintAFew() {
        System.out.println("\n\nPrint a few samples:\n");
        for (int i = 0; i < 25; i++) {
            System.out.println("Short UUID sample: " + UUIDUtils.generateShortTextUUID());
        }

        System.out.println("\n\n");
    }

    @Test
    public void testPerformance() {

        // warm-up
        for (int i = 0; i < 10000; i++) {
            UUIDUtils.generateShortTextUUID();
        }

        long t0 = System.currentTimeMillis();
        final int LOOP = 100000;
        for (int i = 0; i < LOOP; i++) {
            String uuid = UUIDUtils.generateShortTextUUID();
            if ("trick_out_optimizer".equals(uuid)) {
                System.out.println("won't happen but the JIT will not know");
            }
        }
        long dt = System.currentTimeMillis() - t0;
        System.out.println("time for " + LOOP + " loops was " + dt + " ms. " + (1.0 * dt / LOOP) + " ms per one UUID");

        assertTrue( dt < 500,"Generating the UUID took too long. dt=" + dt);
    }

    @Test
    public void testDuplicates() {
        // to avoid obvious issues with uniqueness, of course, it's not a true evidence... but a start...
        final int LOOP = 10000;
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < LOOP; i++) {
            String uuid = UUIDUtils.generateShortTextUUID();
            assertFalse(ids.contains(uuid),"Found a duplicate. this should never ever happen. duplicate: " + uuid);
            ids.add(uuid);
        }

    }

    @Test
    public void testLength() {
        final int LOOP = 100000;
        final int EXPECTED_LENGTH = UUIDUtils.EXPECTED_FIXED_SIZE;
        for (int i = 0; i < LOOP; i++) {
            String uuid = UUIDUtils.generateShortTextUUID();
            int length = StringUtils.length(uuid);
            if (length != EXPECTED_LENGTH) {
                System.out.println("Got a UUID which did not expect the fixed length:" + length + " uuid:" + uuid);
            }
            assertEquals(EXPECTED_LENGTH, length);
        }
    }
}