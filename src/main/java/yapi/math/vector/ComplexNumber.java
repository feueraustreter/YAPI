// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.vector;

import yapi.math.coordinates.CartesianCoordinate;

public class ComplexNumber {

    private final double r;
    private final double i;

    public ComplexNumber(double real, double imag) {
        r = real;
        i = imag;
    }

    public double getReal() {
        return r;
    }

    public double getImaginary() {
        return i;
    }

    public ComplexNumber plus(ComplexNumber b) {
        return new ComplexNumber(r + b.r, i + b.i);
    }

    public ComplexNumber minus(ComplexNumber b) {
        return new ComplexNumber(r - b.r, i - b.i);
    }

    public ComplexNumber times(ComplexNumber b) {
        return new ComplexNumber(r * b.r - i * b.i, r * b.i + i * b.r);
    }

    public ComplexNumber divide(ComplexNumber b) {
        return times(b.reciprocal());
    }

    public ComplexNumber[] sqrt() {
        if (i == 0) {
            throw new ArithmeticException("Complex number imaginary part needs to be non-zero");
        }
        ComplexNumber[] complexNumbers = new ComplexNumber[2];
        double sqrt = Math.sqrt(r * r + i * i);
        double nR = Math.sqrt((r + sqrt) / 2);
        double nI = Math.signum(i) * Math.sqrt(((-r) + sqrt) / 2);
        complexNumbers[0] = new ComplexNumber(nR, nI);
        complexNumbers[1] = new ComplexNumber(-nR, -nI);
        return complexNumbers;
    }

    public ComplexNumber scalar(double s) {
        return new ComplexNumber(r * s, i * s);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(r, -i);
    }

    public ComplexNumber reciprocal() {
        if (r * r + i * i == 0) {
            throw new ArithmeticException("Complex number needs to be non-zero complex number.");
        }
        return new ComplexNumber(r / (r * r + i * i), -(i / (r * r + i * i)));
    }

    public CartesianCoordinate toCartesianCoordinate() {
        return new CartesianCoordinate(r, i);
    }

    public Vector toVector() {
        return new Vector(r, i);
    }

    @Override
    public String toString() {
        return "ComplexNumber{" +
                "r=" + r +
                ", i=" + i +
                '}';
    }
}