package com.qiusuo.videoeditor.util.pinyinhelper;

public final class Pinyin {

    public static final int UP_CASE = -1;
    public static final int FIRST_UP_CASE = 0;
    public static final int LOW_CASE = 1;

    private Pinyin() {
    }

    /**
     * 将输入字符串转为拼音，转换过程中会使用之前设置的用户词典，以字符为单位插入分隔符
     * <p>
     * 例: "hello:中国"  在separator为","时，输出： "h,e,l,l,o,:,ZHONG,GUO,!"
     *
     * @param str       输入字符串
     * @param separator 分隔符
     * @return 中文转为拼音的字符串
     */
    public static String toPinyin(String str, String separator, int caseType) {
        if (str == null || str.length() == 0) {
            return str;
        }

        StringBuffer resultPinyinStrBuf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            resultPinyinStrBuf.append(Pinyin.toPinyin(str.charAt(i), caseType));
            if (i != str.length() - 1) {
                resultPinyinStrBuf.append(separator);
            }
        }
        return resultPinyinStrBuf.toString();
    }

    /**
     * 将输入字符转为拼音
     *
     * @param c 输入字符
     * @return return pinyin if c is chinese in uppercase, String.valueOf(c) otherwise.
     */
    public static String toPinyin(char c, int caseType) {
        String result = String.valueOf(c).toUpperCase();

        if (isChinese(c)) {
            if (c == PinyinData.CHAR_12295) {
                result = PinyinData.PINYIN_12295;
            } else {
                result = PinyinData.PINYIN_TABLE[getPinyinCode(c)];
            }
            switch (caseType) {
                case UP_CASE:
                    result = result.toUpperCase();
                    break;
                case LOW_CASE:
                    result = result.toLowerCase();
                    break;
                case FIRST_UP_CASE:
                    break;
            }
        }
        return result;
    }

    /**
     * 判断输入字符是否为汉字
     *
     * @param c 输入字符
     * @return return whether c is chinese
     */
    public static boolean isChinese(char c) {
        return PinyinData.CHAR_12295 == c || (PinyinData.MIN_VALUE <= c
                && c <= PinyinData.MAX_VALUE && getPinyinCode(c) > 0);
    }

    private static int getPinyinCode(char c) {
        int offset = c - PinyinData.MIN_VALUE;
        if (0 <= offset && offset < PinyinData.PINYIN_CODE_1_OFFSET) {
            return decodeIndex(PinyinCode1.PINYIN_CODE_PADDING, PinyinCode1.PINYIN_CODE, offset);
        } else if (PinyinData.PINYIN_CODE_1_OFFSET <= offset
                && offset < PinyinData.PINYIN_CODE_2_OFFSET) {
            return decodeIndex(PinyinCode2.PINYIN_CODE_PADDING, PinyinCode2.PINYIN_CODE,
                    offset - PinyinData.PINYIN_CODE_1_OFFSET);
        } else {
            return decodeIndex(PinyinCode3.PINYIN_CODE_PADDING, PinyinCode3.PINYIN_CODE,
                    offset - PinyinData.PINYIN_CODE_2_OFFSET);
        }
    }

    private static short decodeIndex(byte[] paddings, byte[] indexes, int offset) {
        //先得到index的低8位
        int index1 = offset / 8;
        int index2 = offset % 8;
        short realIndex;
        realIndex = (short) (indexes[offset] & 0xff);
        //再设置index的最高位(从右到左第9位)
        if ((paddings[index1] & PinyinData.BIT_MASKS[index2]) != 0) {
            realIndex = (short) (realIndex | PinyinData.PADDING_MASK);
        }

//  short highBit=(short)(paddings[index1] & PinyinData.BIT_MASKS[index2]);
//  highBit = (short)(highBit << 8 - index2);
//  realIndex = (short)(realIndex | highBit);

        return realIndex;
    }

}

