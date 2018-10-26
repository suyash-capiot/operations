package com.coxandkings.travel.operations.service.reconfirmation.common;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


@Component
public class HashGenerator {
    /**
     *
     * @return
     */
    public String getHash() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    /**
     *
     * @param length
     * @param random
     * @param symbols
     */
    public HashGenerator(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     *
     * @param length
     * @param random
     */
    public HashGenerator(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     *
     * @param length
     */
    public HashGenerator(int length) {
        this(length, new SecureRandom());
    }

    /**
     *
     */
    public HashGenerator() {
        this(21);
    }

}