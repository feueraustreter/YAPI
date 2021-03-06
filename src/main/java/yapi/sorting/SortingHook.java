// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting;

import yapi.runtime.ThreadUtils;

public class SortingHook<T> {

    public void hook(T... ts) {

    }

    private final void sleep(int sleepTime) {
        ThreadUtils.sleep(sleepTime);
    }

    private final void sleep(long sleepTime) {
        ThreadUtils.sleep(sleepTime);
    }

}