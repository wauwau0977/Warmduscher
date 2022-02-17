package com.x8ing.thsensor.thserver.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * This utility helps to convert UUID to a short textual representation or it parses it.
 * <p>
 * The benefits of having a short text are:
 * <p>
 * - It's shorter. A UUID hex representation with dashes takes 36 characters. e.g. 16d3089d-a817-4781-a2fc-21872f14ae40.
 * A short form is only 22 characters (fixed length): 3pUG3KBLoHic514hV79vF1
 * <p>
 * - The short text has only "safe" characters, no control characters or any other special characters (e.g. quotes, asterix, etc).
 * - The short text avoids similar looking characters like I and 1.
 */
public class UUIDUtils {

    final static int EXPECTED_FIXED_SIZE = 22;


    public static String toShortText(UUID uuid) {

        if (uuid == null) {
            return "";
        }

        byte[] bytes = ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).array();
        return Base58BitcoinFlavor.encode(bytes);
    }

    public static UUID fromShortText(String shortTextUUID) {
        if (StringUtils.isEmpty(shortTextUUID)) {
            return null;
        }

        byte[] bytes = Base58BitcoinFlavor.decode(shortTextUUID);
        ByteBuffer bb = ByteBuffer.wrap(bytes);

        return new UUID(bb.getLong(), bb.getLong());
    }

    /**
     * This method generates a unique short text which contains only simple characters with a fixed size.
     * The text itself has no sematic meaning anymore.
     * <p>
     * Examples:
     * - QMXpzubjJDT5azBE3PTx2b
     * - NUUxTaCt1QvwXifqTuSv6X
     * - FUHK1JQLrW5PykcJLYrVWM
     * - Kxr3hg9eDXb58SvriHEd4b
     */
    public static String generateShortTextUUID() {
        String uuid = toShortText(UUID.randomUUID());

        int lengthOrig = StringUtils.length(uuid);
        if (lengthOrig < EXPECTED_FIXED_SIZE) {
            // length is not fixed. Add some more random characters.
            // Still we ONLY want the allowed characters and no special or similar looking one.
            // Hence take it of another UUID we generate
            String uuid2 = toShortText(UUID.randomUUID());
            uuid = uuid + StringUtils.substring(uuid2, 0, EXPECTED_FIXED_SIZE - lengthOrig);

        }
        return uuid;
    }
}
