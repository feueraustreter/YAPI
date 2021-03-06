// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.operator;

import java.math.BigDecimal;
import java.math.MathContext;

public interface Operator {

    String operator();

    BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext);

}