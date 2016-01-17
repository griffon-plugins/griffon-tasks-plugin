/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example;

import griffon.plugins.tasks.TaskContext;
import griffon.plugins.tasks.Tracker;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.tasks.AbstractTask;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PrimeNumbersTask extends AbstractTask<List<Integer>, Integer> {
    private final int numbersToFind;
    private final List<Integer> numbers = new ArrayList<>();
    private ObservableList<Integer> primes;

    public PrimeNumbersTask(int numbersToFind) {
        super(PrimeNumbersTask.class.getSimpleName() + "-" + System.currentTimeMillis());
        this.numbersToFind = numbersToFind;
    }

    public void setPrimes(ObservableList<Integer> primes) {
        this.primes = primes;
    }

    @Override
    public List<Integer> execute(Tracker<Integer> tracker) throws Exception {
        int candidate = -1;
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
        primes.addAll(chunks);
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