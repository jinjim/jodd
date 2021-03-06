// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.mutable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Holder of a value that is computed lazy.
 */
public class LazyValue<T> implements Supplier<T> {

	private Supplier<T> supplier;
	private volatile boolean initialized;
	private T value;

	/**
	 * Creates new instance of LazyValue.
	 */
	public static <T> LazyValue<T> of(final Supplier<T> supplier) {
		return new LazyValue<>(supplier);
	}

	private LazyValue(final Supplier<T> supplier) {
		this.supplier = supplier;
	}

	/**
	 * Returns the value. Value will be computed on first call.
	 */
	@Override
	public T get() {
		if (!initialized) {
			synchronized (this) {
				if (!initialized) {
					T t = supplier.get();
					value = t;
					initialized = true;
					supplier = null;
					return t;
				}
			}
		}
		return value;
	}

	/**
	 * Returns an optional of current value.
	 */
	public Optional<T> optional() {
		return Optional.ofNullable(get());
	}

}
