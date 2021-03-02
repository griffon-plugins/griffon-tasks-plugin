/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2021 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example;

import griffon.plugins.tasks.Tracker;
import org.codehaus.griffon.runtime.tasks.AbstractTask;

import griffon.annotations.core.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static griffon.util.GriffonClassUtils.requireState;
import static java.util.Objects.requireNonNull;

public class PrimeNumbersTask extends AbstractTask<List<Integer>, Integer> {
    private final int numbersToFind;
    private final List<Integer> numbers = new ArrayList<>();
    private final List<Integer> accumulator;

    public PrimeNumbersTask(int numbersToFind, @Nonnull List<Integer> accumulator) {
        super(PrimeNumbersTask.class.getSimpleName() + "-" + System.currentTimeMillis());
        requireState(numbersToFind > 0, "The quantity of prime numbers to find must be greater than zero");
        this.numbersToFind = numbersToFind;
        this.accumulator = requireNonNull(accumulator, "Argument 'accumulator' must not be null");
    }

    @Override
    public List<Integer> execute(Tracker<Integer> tracker) throws Exception {
        int candidate = -1;
        accumulator.clear();
        while (numbers.size() < numbersToFind) {
            candidate = nextPrimeNumber(++candidate);
            numbers.add(candidate);
            tracker.publish(candidate);
            tracker.setProgress(100 * numbers.size() / numbersToFind);
            Thread.sleep(200);
        }
        return numbers;
    }

    @Override
    public void process(@Nonnull List<Integer> chunks) {
        accumulator.addAll(chunks);
    }

    private int nextPrimeNumber(int candidate) {
        if (isPrime(candidate)) return candidate;
        return nextPrimeNumber(++candidate);
    }

    private boolean isPrime(int n) {
        //check if n is a multiple of 2
        if (n % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}