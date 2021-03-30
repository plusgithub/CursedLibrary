package com.cursedplanet.cursedlibrary.colour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColourLib {

    public String RGBToHex(int red, int green, int blue) {
        return RGBToHex(Arrays.asList(red, green, blue));
    }

    public String RGBToHex(List<Integer> rgbValues) {
        StringBuilder builder = new StringBuilder();
        for (Integer value : rgbValues) {
            String decimalValue = Integer.toHexString(value);

            if (decimalValue.length() <= 1) {
                decimalValue = "0" + decimalValue;
            }

            builder.append(decimalValue);
        }
        return builder.toString();
    }

    public List<Integer> hexToRGB(String hexValue) {
        List<Integer> rgbList = new ArrayList<>();

        String red = hexValue.substring(0, 1);
        String green = hexValue.substring(1, 2);
        String blue = hexValue.substring(2, 3);

        rgbList.add(Integer.parseInt(red, 16));
        rgbList.add(Integer.parseInt(green, 16));
        rgbList.add(Integer.parseInt(blue, 16));

        return rgbList;
    }
}