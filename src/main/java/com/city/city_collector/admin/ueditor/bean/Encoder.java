package com.city.city_collector.admin.ueditor.bean;

public class Encoder {
    public static String toUnicode(String input) {
        StringBuilder builder = new StringBuilder();
        char[] chars = input.toCharArray();

        for (char ch : chars) {
            if (ch < 'Ā')
                builder.append(ch);
            else {
                builder.append(new StringBuilder().append("\\u").append(Integer.toHexString(ch & 0xFFFF)).toString());
            }

        }

        return builder.toString();
    }
}