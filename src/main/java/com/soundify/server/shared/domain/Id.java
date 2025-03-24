package com.soundify.server.shared.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class Id implements Serializable, Comparable<Id> {
    @Serial
        private static final long serialVersionUID = 2625269413446854731L;
        private final long msb;
        private final long lsb;
        public static final int Id_CHARS = 26;
        public static final int TIME_CHARS = 10;
        public static final int RANDOM_CHARS = 16;
        public static final int Id_BYTES = 16;
        public static final int TIME_BYTES = 6;
        public static final int RANDOM_BYTES = 10;
        public static final Id MIN = new Id(0L, 0L);
        public static final Id MAX = new Id(-1L, -1L);
        static final byte[] ALPHABET_VALUES = new byte[256];
        static final char[] ALPHABET_UPPERCASE = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
        static final char[] ALPHABET_LOWERCASE = "0123456789abcdefghjkmnpqrstvwxyz".toCharArray();
        private static final long INCREMENT_OVERFLOW = 0L;

        public Id(Id sid) {
            this.msb = sid.msb;
            this.lsb = sid.lsb;
        }

        public Id(long mostSignificantBits, long leastSignificantBits) {
            this.msb = mostSignificantBits;
            this.lsb = leastSignificantBits;
        }

        public Id(long time, byte[] random) {
            if ((time & -281474976710656L) != 0L) {
                throw new IllegalArgumentException("Invalid time value");
            } else if (random != null && random.length == 10) {
                long long0 = 0L;
                long long1 = 0L;
                long0 |= time << 16;
                long0 |= (long)(random[0] & 255) << 8;
                long0 |= (long)(random[1] & 255);
                long1 |= (long)(random[2] & 255) << 56;
                long1 |= (long)(random[3] & 255) << 48;
                long1 |= (long)(random[4] & 255) << 40;
                long1 |= (long)(random[5] & 255) << 32;
                long1 |= (long)(random[6] & 255) << 24;
                long1 |= (long)(random[7] & 255) << 16;
                long1 |= (long)(random[8] & 255) << 8;
                long1 |= (long)(random[9] & 255);
                this.msb = long0;
                this.lsb = long1;
            } else {
                throw new IllegalArgumentException("Invalid random bytes");
            }
        }

        public static Id fast() {
            long time = System.currentTimeMillis();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            return new Id(time << 16 | random.nextLong() & 65535L, random.nextLong());
        }

        public static Id min(long time) {
            return new Id(time << 16 | 0L, 0L);
        }

        public static Id max(long time) {
            return new Id(time << 16 | 65535L, -1L);
        }

        public static Id from(UUID uuid) {
            return new Id(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        }

        public static Id from(byte[] bytes) {
            if (bytes != null && bytes.length == 16) {
                long msb = 0L;
                long lsb = 0L;
                msb |= ((long)bytes[0] & 255L) << 56;
                msb |= ((long)bytes[1] & 255L) << 48;
                msb |= ((long)bytes[2] & 255L) << 40;
                msb |= ((long)bytes[3] & 255L) << 32;
                msb |= ((long)bytes[4] & 255L) << 24;
                msb |= ((long)bytes[5] & 255L) << 16;
                msb |= ((long)bytes[6] & 255L) << 8;
                msb |= (long)bytes[7] & 255L;
                lsb |= ((long)bytes[8] & 255L) << 56;
                lsb |= ((long)bytes[9] & 255L) << 48;
                lsb |= ((long)bytes[10] & 255L) << 40;
                lsb |= ((long)bytes[11] & 255L) << 32;
                lsb |= ((long)bytes[12] & 255L) << 24;
                lsb |= ((long)bytes[13] & 255L) << 16;
                lsb |= ((long)bytes[14] & 255L) << 8;
                lsb |= (long)bytes[15] & 255L;
                return new Id(msb, lsb);
            } else {
                throw new IllegalArgumentException("Invalid Id bytes");
            }
        }

        public static Id from(String string) {
            char[] chars = toCharArray(string);
            long time = 0L;
            long random0 = 0L;
            long random1 = 0L;
            time |= (long)ALPHABET_VALUES[chars[0]] << 45;
            time |= (long)ALPHABET_VALUES[chars[1]] << 40;
            time |= (long)ALPHABET_VALUES[chars[2]] << 35;
            time |= (long)ALPHABET_VALUES[chars[3]] << 30;
            time |= (long)ALPHABET_VALUES[chars[4]] << 25;
            time |= (long)ALPHABET_VALUES[chars[5]] << 20;
            time |= (long)ALPHABET_VALUES[chars[6]] << 15;
            time |= (long)ALPHABET_VALUES[chars[7]] << 10;
            time |= (long)ALPHABET_VALUES[chars[8]] << 5;
            time |= (long)ALPHABET_VALUES[chars[9]];
            random0 |= (long)ALPHABET_VALUES[chars[10]] << 35;
            random0 |= (long)ALPHABET_VALUES[chars[11]] << 30;
            random0 |= (long)ALPHABET_VALUES[chars[12]] << 25;
            random0 |= (long)ALPHABET_VALUES[chars[13]] << 20;
            random0 |= (long)ALPHABET_VALUES[chars[14]] << 15;
            random0 |= (long)ALPHABET_VALUES[chars[15]] << 10;
            random0 |= (long)ALPHABET_VALUES[chars[16]] << 5;
            random0 |= (long)ALPHABET_VALUES[chars[17]];
            random1 |= (long)ALPHABET_VALUES[chars[18]] << 35;
            random1 |= (long)ALPHABET_VALUES[chars[19]] << 30;
            random1 |= (long)ALPHABET_VALUES[chars[20]] << 25;
            random1 |= (long)ALPHABET_VALUES[chars[21]] << 20;
            random1 |= (long)ALPHABET_VALUES[chars[22]] << 15;
            random1 |= (long)ALPHABET_VALUES[chars[23]] << 10;
            random1 |= (long)ALPHABET_VALUES[chars[24]] << 5;
            random1 |= (long)ALPHABET_VALUES[chars[25]];
            long msb = time << 16 | random0 >>> 24;
            long lsb = random0 << 40 | random1 & 1099511627775L;
            return new Id(msb, lsb);
        }

        public UUID toUuid() {
            return new UUID(this.msb, this.lsb);
        }

        public byte[] toBytes() {
            byte[] bytes = new byte[]{(byte)((int)(this.msb >>> 56)), (byte)((int)(this.msb >>> 48)), (byte)((int)(this.msb >>> 40)), (byte)((int)(this.msb >>> 32)), (byte)((int)(this.msb >>> 24)), (byte)((int)(this.msb >>> 16)), (byte)((int)(this.msb >>> 8)), (byte)((int)this.msb), (byte)((int)(this.lsb >>> 56)), (byte)((int)(this.lsb >>> 48)), (byte)((int)(this.lsb >>> 40)), (byte)((int)(this.lsb >>> 32)), (byte)((int)(this.lsb >>> 24)), (byte)((int)(this.lsb >>> 16)), (byte)((int)(this.lsb >>> 8)), (byte)((int)this.lsb)};
            return bytes;
        }

        public String toString() {
            return this.toString(ALPHABET_UPPERCASE);
        }

        public String toLowerCase() {
            return this.toString(ALPHABET_LOWERCASE);
        }

        public Id toRfc4122() {
            long msb4 = this.msb & -61441L | 16384L;
            long lsb4 = this.lsb & 4611686018427387903L | Long.MIN_VALUE;
            return new Id(msb4, lsb4);
        }

        public Instant getInstant() {
            return Instant.ofEpochMilli(this.getTime());
        }

        public static Instant getInstant(String string) {
            return Instant.ofEpochMilli(getTime(string));
        }

        public long getTime() {
            return this.msb >>> 16;
        }

        public static long getTime(String string) {
            char[] chars = toCharArray(string);
            long time = 0L;
            time |= (long)ALPHABET_VALUES[chars[0]] << 45;
            time |= (long)ALPHABET_VALUES[chars[1]] << 40;
            time |= (long)ALPHABET_VALUES[chars[2]] << 35;
            time |= (long)ALPHABET_VALUES[chars[3]] << 30;
            time |= (long)ALPHABET_VALUES[chars[4]] << 25;
            time |= (long)ALPHABET_VALUES[chars[5]] << 20;
            time |= (long)ALPHABET_VALUES[chars[6]] << 15;
            time |= (long)ALPHABET_VALUES[chars[7]] << 10;
            time |= (long)ALPHABET_VALUES[chars[8]] << 5;
            time |= (long)ALPHABET_VALUES[chars[9]];
            return time;
        }

        public byte[] getRandom() {
            byte[] bytes = new byte[]{(byte)((int)(this.msb >>> 8)), (byte)((int)this.msb), (byte)((int)(this.lsb >>> 56)), (byte)((int)(this.lsb >>> 48)), (byte)((int)(this.lsb >>> 40)), (byte)((int)(this.lsb >>> 32)), (byte)((int)(this.lsb >>> 24)), (byte)((int)(this.lsb >>> 16)), (byte)((int)(this.lsb >>> 8)), (byte)((int)this.lsb)};
            return bytes;
        }

        public static byte[] getRandom(String string) {
            char[] chars = toCharArray(string);
            long random0 = 0L;
            long random1 = 0L;
            random0 |= (long)ALPHABET_VALUES[chars[10]] << 35;
            random0 |= (long)ALPHABET_VALUES[chars[11]] << 30;
            random0 |= (long)ALPHABET_VALUES[chars[12]] << 25;
            random0 |= (long)ALPHABET_VALUES[chars[13]] << 20;
            random0 |= (long)ALPHABET_VALUES[chars[14]] << 15;
            random0 |= (long)ALPHABET_VALUES[chars[15]] << 10;
            random0 |= (long)ALPHABET_VALUES[chars[16]] << 5;
            random0 |= (long)ALPHABET_VALUES[chars[17]];
            random1 |= (long)ALPHABET_VALUES[chars[18]] << 35;
            random1 |= (long)ALPHABET_VALUES[chars[19]] << 30;
            random1 |= (long)ALPHABET_VALUES[chars[20]] << 25;
            random1 |= (long)ALPHABET_VALUES[chars[21]] << 20;
            random1 |= (long)ALPHABET_VALUES[chars[22]] << 15;
            random1 |= (long)ALPHABET_VALUES[chars[23]] << 10;
            random1 |= (long)ALPHABET_VALUES[chars[24]] << 5;
            random1 |= (long)ALPHABET_VALUES[chars[25]];
            byte[] bytes = new byte[]{(byte)((int)(random0 >>> 32)), (byte)((int)(random0 >>> 24)), (byte)((int)(random0 >>> 16)), (byte)((int)(random0 >>> 8)), (byte)((int)random0), (byte)((int)(random1 >>> 32)), (byte)((int)(random1 >>> 24)), (byte)((int)(random1 >>> 16)), (byte)((int)(random1 >>> 8)), (byte)((int)random1)};
            return bytes;
        }

        public long getMostSignificantBits() {
            return this.msb;
        }

        public long getLeastSignificantBits() {
            return this.lsb;
        }

        public Id increment() {
            long newMsb = this.msb;
            long newLsb = this.lsb + 1L;
            if (newLsb == 0L) {
                ++newMsb;
            }

            return new Id(newMsb, newLsb);
        }

        public static boolean isValid(String string) {
            return string != null && isValidCharArray(string.toCharArray());
        }

        public int hashCode() {
            long bits = this.msb ^ this.lsb;
            return (int)(bits ^ bits >>> 32);
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            } else if (other.getClass() != Id.class) {
                return false;
            } else {
                Id that = (Id)other;
                if (this.lsb != that.lsb) {
                    return false;
                } else {
                    return this.msb == that.msb;
                }
            }
        }

        public int compareTo(Id that) {
            long min = Long.MIN_VALUE;
            long a = this.msb + Long.MIN_VALUE;
            long b = that.msb + Long.MIN_VALUE;
            if (a > b) {
                return 1;
            } else if (a < b) {
                return -1;
            } else {
                long c = this.lsb + Long.MIN_VALUE;
                long d = that.lsb + Long.MIN_VALUE;
                if (c > d) {
                    return 1;
                } else {
                    return c < d ? -1 : 0;
                }
            }
        }

        String toString(char[] alphabet) {
            char[] chars = new char[26];
            long time = this.msb >>> 16;
            long random0 = (this.msb & 65535L) << 24 | this.lsb >>> 40;
            long random1 = this.lsb & 1099511627775L;
            chars[0] = alphabet[(int)(time >>> 45 & 31L)];
            chars[1] = alphabet[(int)(time >>> 40 & 31L)];
            chars[2] = alphabet[(int)(time >>> 35 & 31L)];
            chars[3] = alphabet[(int)(time >>> 30 & 31L)];
            chars[4] = alphabet[(int)(time >>> 25 & 31L)];
            chars[5] = alphabet[(int)(time >>> 20 & 31L)];
            chars[6] = alphabet[(int)(time >>> 15 & 31L)];
            chars[7] = alphabet[(int)(time >>> 10 & 31L)];
            chars[8] = alphabet[(int)(time >>> 5 & 31L)];
            chars[9] = alphabet[(int)(time & 31L)];
            chars[10] = alphabet[(int)(random0 >>> 35 & 31L)];
            chars[11] = alphabet[(int)(random0 >>> 30 & 31L)];
            chars[12] = alphabet[(int)(random0 >>> 25 & 31L)];
            chars[13] = alphabet[(int)(random0 >>> 20 & 31L)];
            chars[14] = alphabet[(int)(random0 >>> 15 & 31L)];
            chars[15] = alphabet[(int)(random0 >>> 10 & 31L)];
            chars[16] = alphabet[(int)(random0 >>> 5 & 31L)];
            chars[17] = alphabet[(int)(random0 & 31L)];
            chars[18] = alphabet[(int)(random1 >>> 35 & 31L)];
            chars[19] = alphabet[(int)(random1 >>> 30 & 31L)];
            chars[20] = alphabet[(int)(random1 >>> 25 & 31L)];
            chars[21] = alphabet[(int)(random1 >>> 20 & 31L)];
            chars[22] = alphabet[(int)(random1 >>> 15 & 31L)];
            chars[23] = alphabet[(int)(random1 >>> 10 & 31L)];
            chars[24] = alphabet[(int)(random1 >>> 5 & 31L)];
            chars[25] = alphabet[(int)(random1 & 31L)];
            return new String(chars);
        }

        static char[] toCharArray(String string) {
            char[] chars = string == null ? null : string.toCharArray();
            if (!isValidCharArray(chars)) {
                throw new IllegalArgumentException(String.format("Invalid Id: \"%s\"", string));
            } else {
                return chars;
            }
        }

        static boolean isValidCharArray(char[] chars) {
            if (chars != null && chars.length == 26) {
                for(int i = 0; i < chars.length; ++i) {
                    try {
                        if (ALPHABET_VALUES[chars[i]] == -1) {
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException var3) {
                        return false;
                    }
                }

                if ((ALPHABET_VALUES[chars[0]] & 24) != 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        static {
            Arrays.fill(ALPHABET_VALUES, (byte)-1);

            int i;
            for(i = 0; i < ALPHABET_UPPERCASE.length; ++i) {
                ALPHABET_VALUES[ALPHABET_UPPERCASE[i]] = (byte)i;
            }

            for(i = 0; i < ALPHABET_LOWERCASE.length; ++i) {
                ALPHABET_VALUES[ALPHABET_LOWERCASE[i]] = (byte)i;
            }

            ALPHABET_VALUES[79] = 0;
            ALPHABET_VALUES[73] = 1;
            ALPHABET_VALUES[76] = 1;
            ALPHABET_VALUES[111] = 0;
            ALPHABET_VALUES[105] = 1;
            ALPHABET_VALUES[108] = 1;
        }
}