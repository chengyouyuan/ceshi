package com.winhxd.b2c.message.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author jujinbiao
 * @className GeneratePwd
 * @description
 */
public class GeneratePwd {
    private static Random random = new Random();
    private static final int length = 8;
    private static final int length12 = 12;
    private static ArrayList<Character> lower = null;
    private static ArrayList<Character> upper = null;
    private static String value = "";

    public static ArrayList<Character> getLower() {
        return lower;
    }

    public static void setLower(ArrayList<Character> lower) {
        GeneratePwd.lower = lower;
    }

    public static ArrayList<Character> getUpper() {
        return upper;
    }

    public static void setUpper(ArrayList<Character> upper) {
        GeneratePwd.upper = upper;
    }

    public static String getValue() {
        return value;
    }

    public static void setValue(String value) {
        GeneratePwd.value = value;
    }

    public static String generatePwd() {
        String randomstr = "";
        while (true) {
            setValue("a-zA-Z2-9");
            randomstr = getRandom();
            if (randomstr.replaceAll("O", "").replaceAll("o", "").replaceAll("i", "").replaceAll("I", "").replaceAll("l", "").length() == 8) {
                break;
            }
        }

        return randomstr;
    }

    private static final String getRandom() {
        GeneratePwd thisc = new GeneratePwd();
        thisc.setCharset();
        String randomstr = "";
        try {
            // 建立一个random字符串
            for (int i = 0; i < length; i++) {
                // 取得随机偶数和随机奇数
                if (((int) (getFloat() * 100)) % 2 == 0) {
                    randomstr = randomstr
                            + randomChar((Character) getLower().get(2),
                            (Character) getUpper().get(2)).toString();

                } else {
                    if (((int) (getFloat() * 100)) % 2 == 0) {
                        randomstr = randomstr
                                + randomChar((Character) getLower().get(1),
                                (Character) getUpper().get(1)).toString();
                    } else{
                        randomstr = randomstr
                                + randomChar((Character) getLower().get(0),
                                (Character) getUpper().get(0)).toString();
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomstr;
    }

    public final void setCharset() {
        boolean more = true;

        setLower(new ArrayList<Character>(9));
        setUpper(new ArrayList<Character>(9));
        if ((getValue().charAt(1) == '-') && (getValue().charAt(0) != '\\')) {

            while (more && (getValue().charAt(1) == '-')) {
                if (getValue().charAt(0) == '\\') {
                    break;
                }else {
                    getLower().add(new Character(getValue().charAt(0)));
                    getUpper().add(new Character(getValue().charAt(2)));
                }

                if (getValue().length() <= 3) {
                    more = false;
                } else {
                    setValue(getValue().substring(3));
                }
            }
        }
        if ((lower == null)) {
            setCharset();
        }
    }

    private static final float getFloat() {
        return random.nextFloat();
    }

    private static final Character randomChar(Character lower, Character upper) {
        int tempval;
        char low = lower.charValue();
        char up = upper.charValue();

        tempval = (int) ((int) low + (getFloat() * ((int) (up - low))));

        return (new Character((char) tempval));
    }

    public static String generatePwd12() {
        String randomstr = "";
        while (true) {
            setValue("a-zA-Z2-9");
            randomstr = getRandom12();
            if (randomstr.replaceAll("O", "").replaceAll("o", "").replaceAll("i", "").replaceAll("I", "").replaceAll("l", "").length() == 12) {
                break;
            }
        }

        return randomstr;
    }

    private static final String getRandom12() {
        GeneratePwd thisc = new GeneratePwd();
        thisc.setCharset();
        String randomstr = "";
        try {
            // 建立一个random字符串
            for (int i = 0; i < length12; i++) {
                // 取得随机偶数和随机奇数
                if (((int) (getFloat() * 100)) % 2 == 0) {
                    randomstr = randomstr
                            + randomChar((Character) getLower().get(2),
                            (Character) getUpper().get(2)).toString();

                } else {
                    if (((int) (getFloat() * 100)) % 2 == 0) {
                        randomstr = randomstr
                                + randomChar((Character) getLower().get(1),
                                (Character) getUpper().get(1)).toString();
                    } else{
                        randomstr = randomstr
                                + randomChar((Character) getLower().get(0),
                                (Character) getUpper().get(0)).toString();
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomstr;
    }

}
