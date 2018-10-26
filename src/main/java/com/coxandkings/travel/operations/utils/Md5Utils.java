package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Md5Utils {

    private static Logger logger = Logger.getLogger(Md5Utils.class);

    private static MessageDigest messageDigest;

    static
    {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrptCreditNumber(String creditCardNumber) throws OperationException {
        if (!StringUtils.isEmpty(creditCardNumber) && creditCardNumber.length()!=0 && creditCardNumber.length()==16)
        {
            messageDigest.update(creditCardNumber.getBytes());
            byte[] hashValue = messageDigest.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i =0;i<hashValue.length;i++)
            {
                if ((0xff & hashValue[i]) < 0x10) {
                    hexValue.append("0" + Integer.toHexString(0xff & hashValue[i]));
                }
                else {
                    hexValue.append(Integer.toHexString(0xFF & hashValue[i]));
                }
            }
            return hexValue.toString();
        }
        else {
            logger.error("Credit card number should be of 16 digit");
            throw new OperationException("Credit card number should not be of length "+creditCardNumber.length());
        }
    }

    public static String decrptCreditNumber(String creditCardNumber)
    {
        return null;
    }

}
