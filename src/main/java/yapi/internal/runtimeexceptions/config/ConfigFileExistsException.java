// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions.config;

import yapi.internal.runtimeexceptions.ConfigException;

public class ConfigFileExistsException extends ConfigException {

    public ConfigFileExistsException() {
        super();
    }

    public ConfigFileExistsException(String message) {
        super(message);
    }

    public ConfigFileExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigFileExistsException(Throwable cause) {
        super(cause);
    }

    protected ConfigFileExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}