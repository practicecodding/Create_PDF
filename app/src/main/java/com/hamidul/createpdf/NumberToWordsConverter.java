package com.hamidul.createpdf;

public class NumberToWordsConverter {

    // Array to hold number words
    private static final String[] units = {
            "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "",        // 0
            "",        // 1
            "Twenty",  // 2
            "Thirty",  // 3
            "Forty",   // 4
            "Fifty",   // 5
            "Sixty",   // 6
            "Seventy", // 7
            "Eighty",  // 8
            "Ninety"   // 9
    };

    // Converts integer part to words
    public static String convertIntToWords(int number) {
        if (number < 20) {
            return units[number];
        } else if (number < 100) {
            return tens[number / 10] + (number % 10 != 0 ? " " + units[number % 10] : "");
        } else if (number < 1000) {
            return units[number / 100] + " Hundred" + (number % 100 != 0 ? " " + convertIntToWords(number % 100) : "");
        } else if (number < 1000000) {
            return convertIntToWords(number / 1000) + " Thousand" + (number % 1000 != 0 ? " " + convertIntToWords(number % 1000) : "");
        } else if (number < 1000000000) {
            return convertIntToWords(number / 1000000) + " Million" + (number % 1000000 != 0 ? " " + convertIntToWords(number % 1000000) : "");
        } else {
            return convertIntToWords(number / 1000000000) + " Billion" + (number % 1000000000 != 0 ? " " + convertIntToWords(number % 1000000000) : "");
        }
    }

    // Convert the double number to words
    public static String convertDoubleToWords(double number) {
        // Split the number into integer and fractional part
        long taka = (long) number;  // Integer part (before the decimal point)
        long point = Math.round((number - taka) * 100); // Fractional part (after the decimal point)

        String takaPart = convertIntToWords((int) taka) + "";
        String pointPart = point > 0 ? " point " + convertIntToWords((int) point) + "" : "";

        // Capitalize the first letter, and make other letters lowercase
        return capitalizeFirstLetter(takaPart + pointPart);
    }

    // Utility to capitalize the first letter and make the rest lowercase
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // Capitalize the first character, make the rest lowercase
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
