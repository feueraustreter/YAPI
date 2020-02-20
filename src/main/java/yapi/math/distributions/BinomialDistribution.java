// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.distributions;

import yapi.internal.exceptions.MathException;
import yapi.math.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BinomialDistribution {

    private long size;
    private double probability;

    private double mu;
    private double sigma;

    private static MathContext mathContext = new MathContext(200, RoundingMode.HALF_UP);

    public BinomialDistribution(long size, double probability) {
        this.size = size;
        if (probability <= 1 && probability >= 0) {
            this.probability = probability;
        } else {
            throw new MathException("Probability needs to be between 0 and 1");
        }
        calcMu();
        calcSigma();
    }

    private void calcMu() {
        mu = size * probability;
    }

    private void calcSigma() {
        sigma = Math.sqrt(size * probability * (1 - probability));
    }

    public BigDecimal getBinomial(long experiment) {
        if (experiment < 0 || experiment > size) {
            return BigDecimal.valueOf(-1);
        }
        return new BigDecimal(over(size, experiment)).multiply(BigDecimal.valueOf(probability).pow((int)experiment, mathContext), mathContext).multiply(BigDecimal.valueOf(1 - probability).pow((int)(size - experiment), mathContext), mathContext);
    }

    public BigDecimal[] getBinomial(long startExperiment, long stopExperiment) {
        if (stopExperiment < startExperiment) {
            return new BigDecimal[0];
        }
        BigDecimal[] doubles = new BigDecimal[(int)(stopExperiment - startExperiment + 1)];
        int j = 0;
        for (long i = startExperiment; i <= stopExperiment; i++) {
            doubles[j] = getBinomial(i);
            j++;
        }
        return doubles;
    }

    public BigDecimal[] getBinomial() {
        return getBinomial(0, size);
    }

    private BigInteger over(long n, long r) {
        return NumberUtils.fastOver(BigInteger.valueOf(n), BigInteger.valueOf(r));
    }

    public BigDecimal getFunction(long experiment) {
        BigDecimal d = BigDecimal.ZERO;
        for (long i = 0; i <= experiment; i++) {
            d = d.add(getBinomial(i), mathContext);
        }
        return d;
    }

    public BigDecimal getProbability(long experiment) {
        return getBinomial(experiment);
    }

    public BigDecimal getProbabilityEqual(long experiment) {
        return getBinomial(experiment);
    }

    public BigDecimal getProbabilityNotEqual(long experiment) {
        return BigDecimal.ONE.subtract(getBinomial(experiment), mathContext);
    }

    public BigDecimal getProbabilityBetween(int x1, int x2) {
        // Todo: Make this method faster
        if (x2 < x1) {
            int x3 = x2;
            x2 = x1;
            x1 = x3;
        }
        x2--;

        BigDecimal d1 = getFunction(x1);
        BigDecimal d2 = d1.add(BigDecimal.ZERO);
        for (int i = x1 + 1; i <= x2; i++) {
            d2 = d2.add(getBinomial(i));
        }
        return d2.subtract(d1, mathContext);
    }

    public BigDecimal getProbabilityBetweenAndEqual(int x1, int x2) {
        // Todo: Make this method faster
        if (x2 < x1) {
            int x3 = x2;
            x2 = x1;
            x1 = x3;
        }
        x1--;

        BigDecimal d1 = getFunction(x1);
        BigDecimal d2 = d1.add(BigDecimal.ZERO);
        for (int i = x1 + 1; i <= x2; i++) {
            d2 = d2.add(getBinomial(i));
        }
        return d2.subtract(d1, mathContext);
    }

    public BigDecimal getProbabilityBelow(long experiment) {
        return getFunction(experiment - 1);
    }

    public BigDecimal getProbabilityBelowAndEqual(long experiment) {
        return getFunction(experiment);
    }

    public BigDecimal getProbabilityAbove(long experiment) {
        return BigDecimal.ONE.subtract(getFunction(experiment), mathContext);
    }

    public BigDecimal getProbabilityAboveAndEqual(long experiment) {
        return BigDecimal.ONE.subtract(getFunction(experiment - 1), mathContext);
    }

    public double getMu() {
        return mu;
    }

    public double getExpectedValue() {
        return getMu();
    }

    public double getSigma() {
        return sigma;
    }

    public double getExpectedDeviation() {
        return getSigma();
    }

}