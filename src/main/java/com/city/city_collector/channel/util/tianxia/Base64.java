package com.city.city_collector.channel.util.tianxia;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang.time.StopWatch;


public class Base64 {

    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();

    private static final int[] INV = new int[256];

    static {

        Arrays.fill(INV, -1);

        for (int i = 0, iS = CHARS.length; i < iS; i++) {

            INV[CHARS[i]] = i;

        }

        INV['='] = 0;

    }

    /**
     * Returns Base64 characters, a clone of used array.
     */

    public static char[] getAlphabet() {

        return CHARS.clone();

    }


    public static char[] encodeToChar(String s) {

        try {

            return encodeToChar(s.getBytes("UTF-8"), false);

        } catch (UnsupportedEncodingException ignore) {

            return null;

        }

    }

    public static char[] encodeToChar(byte[] arr) {

        return encodeToChar(arr, false);

    }

    /**
     * Encodes a raw byte array into a BASE64 <code>char[]</code>.
     */

    public static char[] encodeToChar(byte[] arr, boolean lineSeparator) {

        int len = arr != null ? arr.length : 0;

        if (len == 0) {

            return new char[0];

        }

        int evenlen = (len / 3) * 3;

        int cnt = ((len - 1) / 3 + 1) << 2;

        int destLen = cnt + (lineSeparator ? (cnt - 1) / 76 << 1 : 0);

        char[] dest = new char[destLen];

        for (int s = 0, d = 0, cc = 0; s < evenlen; ) {

            int i = (arr[s++] & 0xff) << 16 | (arr[s++] & 0xff) << 8

                    | (arr[s++] & 0xff);

            dest[d++] = CHARS[(i >>> 18) & 0x3f];

            dest[d++] = CHARS[(i >>> 12) & 0x3f];

            dest[d++] = CHARS[(i >>> 6) & 0x3f];

            dest[d++] = CHARS[i & 0x3f];

            if (lineSeparator && (++cc == 19) && (d < (destLen - 2))) {

                dest[d++] = '\r';

                dest[d++] = '\n';

                cc = 0;

            }

        }

        int left = len - evenlen; // 0 - 2.

        if (left > 0) {

            int i = ((arr[evenlen] & 0xff) << 10)

                    | (left == 2 ? ((arr[len - 1] & 0xff) << 2) : 0);

            dest[destLen - 4] = CHARS[i >> 12];

            dest[destLen - 3] = CHARS[(i >>> 6) & 0x3f];

            dest[destLen - 2] = left == 2 ? CHARS[i & 0x3f] : '=';

            dest[destLen - 1] = '=';

        }

        return dest;

    }

    /**
     * Decodes a BASE64 encoded char array.
     */

    public byte[] decode(char[] arr) {

        int length = arr.length;

        if (length == 0) {

            return new byte[0];

        }

        int sndx = 0, endx = length - 1;

        int pad = arr[endx] == '=' ? (arr[endx - 1] == '=' ? 2 : 1) : 0;

        int cnt = endx - sndx + 1;

        int sepCnt = length > 76 ? (arr[76] == '\r' ? cnt / 78 : 0) << 1 : 0;

        int len = ((cnt - sepCnt) * 6 >> 3) - pad;

        byte[] dest = new byte[len];

        int d = 0;

        for (int cc = 0, eLen = (len / 3) * 3; d < eLen; ) {

            int i = INV[arr[sndx++]] << 18 | INV[arr[sndx++]] << 12

                    | INV[arr[sndx++]] << 6 | INV[arr[sndx++]];

            dest[d++] = (byte) (i >> 16);

            dest[d++] = (byte) (i >> 8);

            dest[d++] = (byte) i;

            if (sepCnt > 0 && ++cc == 19) {

                sndx += 2;

                cc = 0;

            }

        }

        if (d < len) {

            int i = 0;

            for (int j = 0; sndx <= endx - pad; j++) {

                i |= INV[arr[sndx++]] << (18 - j * 6);

            }

            for (int r = 16; d < len; r -= 8) {

                dest[d++] = (byte) (i >> r);

            }

        }

        return dest;

    }

    // ---------------------------------------------------------------- byte

    public static byte[] encodeToByte(String s) {

        try {

            return encodeToByte(s.getBytes("UTF-8"), false);

        } catch (UnsupportedEncodingException ignore) {

            return null;

        }

    }

    public static byte[] encodeToByte(byte[] arr) {

        return encodeToByte(arr, false);

    }

    /**
     * Encodes a raw byte array into a BASE64 <code>byte[]</code>.
     */

    public static byte[] encodeToByte(byte[] arr, boolean lineSep) {

        int len = arr != null ? arr.length : 0;

        if (len == 0) {

            return new byte[0];

        }

        int evenlen = (len / 3) * 3;

        int cnt = ((len - 1) / 3 + 1) << 2;

        int destlen = cnt + (lineSep ? (cnt - 1) / 76 << 1 : 0);

        byte[] dest = new byte[destlen];

        for (int s = 0, d = 0, cc = 0; s < evenlen; ) {

            int i = (arr[s++] & 0xff) << 16 | (arr[s++] & 0xff) << 8

                    | (arr[s++] & 0xff);

            dest[d++] = (byte) CHARS[(i >>> 18) & 0x3f];

            dest[d++] = (byte) CHARS[(i >>> 12) & 0x3f];

            dest[d++] = (byte) CHARS[(i >>> 6) & 0x3f];

            dest[d++] = (byte) CHARS[i & 0x3f];

            if (lineSep && ++cc == 19 && d < destlen - 2) {

                dest[d++] = '\r';

                dest[d++] = '\n';

                cc = 0;

            }

        }

        int left = len - evenlen;

        if (left > 0) {

            int i = ((arr[evenlen] & 0xff) << 10)

                    | (left == 2 ? ((arr[len - 1] & 0xff) << 2) : 0);

            dest[destlen - 4] = (byte) CHARS[i >> 12];

            dest[destlen - 3] = (byte) CHARS[(i >>> 6) & 0x3f];

            dest[destlen - 2] = left == 2 ? (byte) CHARS[i & 0x3f] : (byte) '=';

            dest[destlen - 1] = '=';

        }

        return dest;

    }

    /**
     * Decodes a BASE64 encoded byte array.
     */

    public static byte[] decode(byte[] arr) {

        int length = arr.length;

        if (length == 0) {

            return new byte[0];

        }

        int sndx = 0, endx = length - 1;

        int pad = arr[endx] == '=' ? (arr[endx - 1] == '=' ? 2 : 1) : 0;

        int cnt = endx - sndx + 1;

        int sepCnt = length > 76 ? (arr[76] == '\r' ? cnt / 78 : 0) << 1 : 0;

        int len = ((cnt - sepCnt) * 6 >> 3) - pad;

        byte[] dest = new byte[len];

        int d = 0;

        for (int cc = 0, eLen = (len / 3) * 3; d < eLen; ) {

            int i = INV[arr[sndx++]] << 18 | INV[arr[sndx++]] << 12

                    | INV[arr[sndx++]] << 6 | INV[arr[sndx++]];

            dest[d++] = (byte) (i >> 16);

            dest[d++] = (byte) (i >> 8);

            dest[d++] = (byte) i;

            if (sepCnt > 0 && ++cc == 19) {

                sndx += 2;

                cc = 0;

            }

        }

        if (d < len) {

            int i = 0;

            for (int j = 0; sndx <= endx - pad; j++) {

                i |= INV[arr[sndx++]] << (18 - j * 6);

            }

            for (int r = 16; d < len; r -= 8) {

                dest[d++] = (byte) (i >> r);

            }

        }

        return dest;

    }

    // ---------------------------------------------------------------- string

    public static String encodeToString(String s) {

        try {

            return new String(encodeToChar(s.getBytes("UTF-8"), false));

        } catch (UnsupportedEncodingException ignore) {

            return null;

        }

    }

    public static String decodeToString(String s) {

        try {

            return new String(decode(s), "UTF-8");

        } catch (UnsupportedEncodingException ignore) {

            return null;

        }

    }

    public static String encodeToString(byte[] arr) {

        return new String(encodeToChar(arr, false));

    }

    /**
     * Encodes a raw byte array into a BASE64 <code>String</code>.
     */

    public static String encodeToString(byte[] arr, boolean lineSep) {

        return new String(encodeToChar(arr, lineSep));

    }

    /**
     * Decodes a BASE64 encoded string.
     */

    public static byte[] decode(String s) {

        int length = s.length();

        if (length == 0) {

            return new byte[0];

        }

        int sndx = 0, endx = length - 1;

        int pad = s.charAt(endx) == '=' ? (s.charAt(endx - 1) == '=' ? 2 : 1)

                : 0;

        int cnt = endx - sndx + 1;

        int sepCnt = length > 76 ? (s.charAt(76) == '\r' ? cnt / 78 : 0) << 1

                : 0;

        int len = ((cnt - sepCnt) * 6 >> 3) - pad;

        byte[] dest = new byte[len];

        int d = 0;

        for (int cc = 0, eLen = (len / 3) * 3; d < eLen; ) {

            int i = INV[s.charAt(sndx++)] << 18 | INV[s.charAt(sndx++)] << 12

                    | INV[s.charAt(sndx++)] << 6 | INV[s.charAt(sndx++)];

            dest[d++] = (byte) (i >> 16);

            dest[d++] = (byte) (i >> 8);

            dest[d++] = (byte) i;

            if (sepCnt > 0 && ++cc == 19) {

                sndx += 2;

                cc = 0;

            }

        }

        if (d < len) {

            int i = 0;

            for (int j = 0; sndx <= endx - pad; j++) {

                i |= INV[s.charAt(sndx++)] << (18 - j * 6);

            }

            for (int r = 16; d < len; r -= 8) {

                dest[d++] = (byte) (i >> r);

            }

        }

        return dest;

    }

//	public static void main(String[] args) {
//
//		StopWatch sw = new StopWatch();
//		sw.start();
//
//		String str = "{\"name\":\"vicken\",\"age\":20}";
//
//		for (int i = 0; i < 100000; i++) {
//			//Base64.encodeToByte(str);
//			Base64.encodeToString(str);
//		}
//
//		//System.out.println(Base64.encodeToString(str));
//
//		sw.stop();
//
//		System.out.println(sw.getTime());
//
//	}


	/*

	private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D',
		'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
		'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
		'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
		60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
		-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
		38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
		-1, -1 };


	public static String encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;

		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
				                            | ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
			                            | ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
			                            | ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static byte[] decode(String str) {
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {

			 b1 
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			 b2 
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			 b3 
			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toByteArray();
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			 b4 
			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toByteArray();
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return buf.toByteArray();
	}

	public static void main(String[] args) {
		Base64 base64 = new Base64();
		String testStr = "哩乫哿旰翜";
		System.out.println("加密前：" + testStr);
		String encodeStr = base64.encode(testStr.getBytes());
		System.out.println("加密数据：" + encodeStr);
		System.out.println("解密前：" + encodeStr);
		byte[] decodeStr = base64.decode(encodeStr);
		System.out.println("解密数据：" + new String(decodeStr));
	}

*/
}

