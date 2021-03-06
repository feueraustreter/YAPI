// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.diffihellman;

import java.math.BigInteger;
import java.security.SecureRandom;

public class DiffieHellman {

    public static void main(String[] args) {
        DiffieHellman diffieHellman1 = new DiffieHellman();
        DiffieHellman diffieHellman2 = new DiffieHellman(diffieHellman1.getPublicG(), diffieHellman1.getPublicP());

        BigInteger bigInteger1 = diffieHellman1.getValue();
        BigInteger bigInteger2 = diffieHellman2.getValue();

        diffieHellman1.init().round(bigInteger2).finish();
        diffieHellman2.init().round(bigInteger1).finish();

        System.out.println(diffieHellman1.getDiffieHellmanResult().getSharedSecret());
        System.out.println(diffieHellman2.getDiffieHellmanResult().getSharedSecret());
    }

    private static int bitLength = 1024;

    private class Values {

        private BigInteger g;
        private BigInteger p;

        private BigInteger privateValue = new BigInteger(bitLength, new SecureRandom());

        private BigInteger temp = BigInteger.ZERO;
        private BigInteger yourValue = BigInteger.ZERO;

        private void destroy() {
            g = BigInteger.ZERO;
            p = BigInteger.ZERO;
            privateValue = BigInteger.ZERO;
            temp = BigInteger.ZERO;
            yourValue = BigInteger.ZERO;
        }

        private void init() {
            g = BigInteger.probablePrime(bitLength, new SecureRandom());
            while (true) {
                BigInteger bigInteger = BigInteger.probablePrime(bitLength, new SecureRandom());
                if (bigInteger.compareTo(g) != 0) {
                    p = bigInteger;
                    break;
                }
            }
        }

        private void init(BigInteger g, BigInteger p) {
            this.g = g;
            this.p = p;
        }
    }

    private Values values;
    private DiffieHellmanResult diffieHellmanResult;

    public DiffieHellman() {
        values = new Values();
        values.init();
    }

    public DiffieHellman(String g, String p) {
        this(new BigInteger(g), new BigInteger(p));
    }

    public DiffieHellman(BigInteger g, BigInteger p) {
        values = new Values();
        values.init(g, p);
    }

    public class DiffieHellmanFactory {

        private Values values;
        private DiffieHellman diffieHellman;

        public DiffieHellmanFactory(Values values, DiffieHellman diffieHellman) {
            this.values = values;
            this.diffieHellman = diffieHellman;
        }

        public DiffieHellmanFactory round(BigInteger x) {
            values.temp = x.modPow(values.privateValue, values.p);
            return this;
        }

        public DiffieHellmanResult finish() {
            diffieHellman.diffieHellmanResult = new DiffieHellmanResult(values.temp);
            values.destroy();
            return diffieHellman.diffieHellmanResult;
        }

    }

    public class DiffieHellmanResult {

        private BigInteger sharedSecret;

        public DiffieHellmanResult(BigInteger sharedSecret) {
            this.sharedSecret = sharedSecret;
        }

        public BigInteger getSharedSecret() {
            return sharedSecret;
        }

        public void destroy() {
            sharedSecret = BigInteger.ZERO;
        }

    }

    public BigInteger getPublicG() {
        if (values.yourValue.compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalStateException();
        }
        return values.g;
    }

    public BigInteger getPublicP() {
        if (values.yourValue.compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalStateException();
        }
        return values.p;
    }

    public BigInteger getValue() {
        values.yourValue = values.g.modPow(values.privateValue, values.p);
        return values.yourValue.add(BigInteger.ZERO);
    }

    public DiffieHellmanFactory init() {
        if (diffieHellmanResult != null) {
            throw new IllegalStateException();
        }
        if (values.yourValue.compareTo(BigInteger.ZERO) == 0) {
            throw new IllegalStateException("Use getValue() first");
        }
        return new DiffieHellmanFactory(values, this);
    }

    public DiffieHellmanResult getDiffieHellmanResult() {
        if (diffieHellmanResult == null) {
            throw new IllegalStateException();
        }
        return diffieHellmanResult;
    }

}