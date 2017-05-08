/*
 * Copyright (C) 2015 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prolificinteractive.geocoder.api.googlemaps;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.IOException;

/**
 * Is thrown for geocoder errors
 *
 * Call {@link #getErrorMessage()} to get error_message from response, if available
 * Call {@link #getStatus()} to get status, if available
 *
 * If this {@link Exception} is caused
 */
public class GoogleMapsException extends Exception {

    private Status mStatus;
    private String mErrorMessage;

    @NonNull
    public static GoogleMapsException forStatus(@NonNull final Status status) {
        final GoogleMapsException e = new GoogleMapsException();
        e.setStatus(status);
        return e;
    }

    public GoogleMapsException() {
    }

    public GoogleMapsException(final Throwable cause) {
        super(cause);
    }

    @Nullable
    public Status getStatus() {
        return mStatus;
    }

    void setStatus(final Status status) {
        mStatus = status;
    }

    @Nullable
    public String getErrorMessage() {
        return mErrorMessage;
    }

    void setErrorMessage(final String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public boolean isCausedByNetworkError() {
        return getCause() instanceof IOException;
    }

    @Override
    public String toString() {
        if (mErrorMessage != null && !mErrorMessage.isEmpty()) {
            return mErrorMessage;
        }
        if (mStatus != null) {
            return mStatus.name();
        }
        final Throwable cause = getCause();
        if (cause != null) {
            return cause.toString();
        }
        return super.toString();
    }
}
