package com.qiusuo.videoeditor.util.pinyinhelper;

public class PinyinUtils {
    /**
     * 删除小写字母
     *
     * @param str 字符串
     * @return
     */
    public static String deleteLowerCase(String str) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c < 'a' || c > 'z') sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     *
     * @param inputStr 汉字
     * @return
     */
    public static String getPingYin(String inputStr) {
        String output = Pinyin.toPinyin(inputStr, "", Pinyin.FIRST_UP_CASE);
        return output;
    }

    /**
     * 汉字转换为汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        String pinyinName = Pinyin.toPinyin(chines, "", Pinyin.FIRST_UP_CASE);
        return deleteLowerCase(pinyinName);
    }
}

