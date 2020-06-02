// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.operator;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AbstractOperator implements Operator {

    private String operator;

    public AbstractOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public final String operator() {
        return operator;
    }

    @Override
    public abstract BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext);

}