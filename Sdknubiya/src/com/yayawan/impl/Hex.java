package com.yayawan.impl;


/**
 * 16进制与字节转换
 *
 */
public class Hex {

    private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


    /**
     * 16进制转为字节
     * @param hex
     * @return
     * @throws IllegalArgumentException
     */
    public static byte[] hex2byte(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    public static byte hexCharToByte(String sourceChar)
    {
        if (sourceChar.length() > 1){
            sourceChar = sourceChar.substring(0, 1);
        }

        int a = 0 ;
        if("a".equals(sourceChar)){
            a=10;
        }
        if("b".equals(sourceChar)){
            a=11;
        }
        if("c".equals(sourceChar)){
            a=12;
        }
        if("d".equals(sourceChar)){
            a=13;
        }
        if("e".equals(sourceChar)){
            a = 14;
        }
        if("f".equals(sourceChar)){
            a = 15;
        }
        if(a>=0 && a< 10){
            a = Integer.parseInt(sourceChar);
        }
        switch (a)
        {
            case 0:
                return 0x00;
            case 1:
                return 0x01;
            case 2:
                return 0x02;
            case 3:
                return 0x03;
            case 4:
                return 0x04;
            case 5:
                return 0x05;
            case 6:
                return 0x06;
            case 7:
                return 0x07;
            case 8:
                return 0x08;
            case 9:
                return 0x09;
            case 10:
                return 0x0a;
            case 11:
                return 0x0b;
            case 12:
                return 0x0c;
            case 13:
                return 0x0d;
            case 14:
                return 0x0e;
            case 15:
                return 0x0f;
            default:
                return 0x00;
        }
    }

    /**
     * 字节转为16进制
     * @param b
     * @return
     */



    public static String byte2hex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }


    /**
     * 字节转为16进制
     * @param b
     * @return
     */
    public static String byte3hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF);
            if (stmp.length() == 1) {
                hs += "0" + stmp;
            } else {
                hs += stmp;
            }
        }
        //return hs;
        return hs.toUpperCase();
    }
}
