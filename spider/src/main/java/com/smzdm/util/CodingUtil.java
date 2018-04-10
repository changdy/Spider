package com.smzdm.util;

/**
 * Created by Changdy on 2018/4/9.
 */
public class CodingUtil {
    public static String decodeUnicode(String theString) {
        char currentChar;
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            currentChar = theString.charAt(x++);
            if (currentChar == '\\') {
                currentChar = theString.charAt(x++);
                if (currentChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        currentChar = theString.charAt(x++);
                        switch (currentChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + currentChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + currentChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + currentChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed uxxxxencoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (currentChar == 't')
                        currentChar = '\t';
                    else if (currentChar == 'r')
                        currentChar = '\r';
                    else if (currentChar == 'n')
                        currentChar = '\n';
                    else if (currentChar == 'f')
                        currentChar = '\f';
                    outBuffer.append(currentChar);
                }
            } else
                outBuffer.append(currentChar);
        }
        return outBuffer.toString();
    }
}
