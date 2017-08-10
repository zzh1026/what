/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.volley;

/**
 * Default retry policy for requests.
 */
public class DefaultRetryPolicy implements RetryPolicy {
    /** The current timeout in milliseconds.
     *
     * 当前设置的超时时间
     */
    private int mCurrentTimeoutMs;

    /** The current retry count.
     * 当前当前已重试次数
     *
     * */
    private int mCurrentRetryCount;

    /** The maximum number of attempts.
     * 最多重试次数
     * */
    private final int mMaxNumRetries;

    /** The backoff multiplier for the policy.
     *
     * 对于请求失败之后的请求，并不会隔相同的时间去请求Server，不会以线性的时间增长去请求，
     * 而是一个曲线增长，一次比一次长，如果backoff因子是2，当前超时为3，即下次再请求隔6S。
     *
     * */
    private final float mBackoffMultiplier;

    /** The default socket timeout in milliseconds
     * 默认超时时间
     * */
    public static final int DEFAULT_TIMEOUT_MS = 10000;

    /** The default number of retries
     *
     * 默认的最大重试次数
     * */
    public static final int DEFAULT_MAX_RETRIES = 0;

    /** The default backoff multiplier
     *对于请求失败之后的请求，并不会隔相同的时间去请求Server，不会以线性的时间增长去请求，
     * 而是一个曲线增长，一次比一次长，如果backoff因子是2，当前超时为3，即下次再请求隔6S。
     * */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /**
     * Constructs a new retry policy using the default timeouts.
     */
    public DefaultRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    /**
     * Constructs a new retry policy.
     * @param initialTimeoutMs The initial timeout for the policy.
     * @param maxNumRetries The maximum number of retries.
     * @param backoffMultiplier Backoff multiplier for the policy.
     */
    public DefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }

    /**
     * Returns the current timeout.
     */
    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    /**
     * Returns the current retry count.
     */
    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }

    /**
     * Returns the backoff multiplier for the policy.
     */
    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }

    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     * @param error The error code of the last attempt.
     */
    @Override
    public void retry(VolleyError error) throws VolleyError {
        mCurrentRetryCount++;
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
        if (!hasAttemptRemaining()) {
            throw error;
        }
    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    protected boolean hasAttemptRemaining() {
        return mCurrentRetryCount <= mMaxNumRetries;
    }
}
