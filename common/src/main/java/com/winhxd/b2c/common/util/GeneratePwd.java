package com.winhxd.b2c.common.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * @author caoliming
 */
public class GeneratePwd {
    private static Random random = new Random();
    private static final int length = 8;
    private static final int length6 = 6;
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

    public static String generateOrdercode() {
        String randomstr = "";
        while (true) {
            setValue("a-zA-Z2-9");
            randomstr = getRandom6();
            if (randomstr.replaceAll("O", "").replaceAll("o", "").replaceAll("i", "").replaceAll("I", "").replaceAll("l", "").length() == 6) {
                break;
            }
        }

        return randomstr;
    }

    public static String generatePwd6Mobile() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 6; i++) {
            result = result * 10 + array[i];
        }
        if (("" + result).length() != 6) {
            return generatePwd6Mobile();
        }
        return "" + result;
    }

    public static String generateCode4Film() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result = result * 10 + array[i];
        }
        if (("" + result).length() != 8) {
            return generateCode4Film();
        }
        return "" + result;
    }

    private static final float getFloat() {
        return random.nextFloat();
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
                    } else {
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

    private static final String getRandom6() {
        GeneratePwd thisc = new GeneratePwd();
        thisc.setCharset();
        String randomstr = "";
        try {
            // 建立一个random字符串
            for (int i = 0; i < length6; i++) {
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
                    } else {
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
                    } else {
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

    private static final Character randomChar(Character lower, Character upper) {
        int tempval;
        char low = lower.charValue();
        char up = upper.charValue();

        tempval = (int) ((int) low + (getFloat() * ((int) (up - low))));

        return (new Character((char) tempval));
    }

    public final void setCharset() {
        boolean more = true;

        setLower(new ArrayList<Character>(9));
        setUpper(new ArrayList<Character>(9));
        if ((getValue().charAt(1) == '-') && (getValue().charAt(0) != '\\')) {

            while (more && (getValue().charAt(1) == '-')) {

                if (getValue().charAt(0) == '\\') {
                    break;
                } else {

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

    /**
     * 产生四位验证码（数字）
     *
     * @return String
     */
    public static String generate4MobileCode() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = result * 10 + array[i];
        }
        if (("" + result).length() != 4) {
            return generate4MobileCode();
        }
        return "" + result;
    }

    /**
     * 生成12位随机码
     *
     * @return string
     */
    public static String generate12Code() {
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

    /**
     * 产生10位验证码
     *
     * @return
     */
    public static String generatePwd10Mobile() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        long result = 0;
        for (int i = 0; i < 10; i++) {
            result = result * 10 + array[i];
        }
        if (("" + result).length() < 10) {
            return generatePwd10Mobile();
        }
        return "" + result;
    }

	public static String getRandomUUID() {
		String uuid = UUID.randomUUID().toString(); 
		return uuid = uuid.replace("-", "");  
	}

}
