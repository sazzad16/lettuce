/*
 * Copyright 2011-Present, Redis Ltd. and Contributors
 * All rights reserved.
 *
 * Licensed under the MIT License.
 *
 * This file contains contributions from third-party contributors
 * licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core;

import static io.lettuce.TestTags.UNIT_TEST;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ScoredValue}.
 *
 * @author Will Glozer
 * @author Mark Paluch
 */
@Tag(UNIT_TEST)
class ScoredValueUnitTests {

    @Test
    void shouldCreateEmptyScoredValueFromOptional() {

        Value<String> value = ScoredValue.from(42, Optional.<String> empty());

        assertThat(value.hasValue()).isFalse();
    }

    @Test
    void shouldCreateEmptyValue() {

        Value<String> value = ScoredValue.empty();

        assertThat(value.hasValue()).isFalse();
    }

    @Test
    void shouldCreateNonEmptyValueFromOptional() {

        ScoredValue<String> value = (ScoredValue) ScoredValue.from(4.2, Optional.of("hello"));

        assertThat(value.hasValue()).isTrue();
        assertThat(value.getValue()).isEqualTo("hello");
        assertThat(value.getScore()).isCloseTo(4.2, offset(0.01));
    }

    @Test
    void shouldCreateEmptyValueFromValue() {

        Value<String> value = ScoredValue.fromNullable(42, null);

        assertThat(value.hasValue()).isFalse();
    }

    @Test
    void shouldCreateNonEmptyValueFromValue() {

        ScoredValue<String> value = (ScoredValue<String>) ScoredValue.fromNullable(42, "hello");

        assertThat(value.hasValue()).isTrue();
        assertThat(value.getValue()).isEqualTo("hello");
    }

    @Test
    void justShouldCreateValueFromValue() {

        ScoredValue<String> value = ScoredValue.just(42, "hello");

        assertThat(value.hasValue()).isTrue();
        assertThat(value.getValue()).isEqualTo("hello");
    }

    @Test
    void justShouldRejectEmptyValueFromValue() {
        assertThatThrownBy(() -> ScoredValue.just(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateNonEmptyValue() {

        ScoredValue<String> value = (ScoredValue<String>) ScoredValue.from(12, Optional.of("hello"));

        assertThat(value.hasValue()).isTrue();
        assertThat(value.getValue()).isEqualTo("hello");
    }

    @Test
    void equals() {
        ScoredValue<String> sv1 = (ScoredValue<String>) ScoredValue.fromNullable(1.0, "a");
        assertThat(sv1.equals(ScoredValue.fromNullable(1.0, "a"))).isTrue();
        assertThat(sv1.equals(null)).isFalse();
        assertThat(sv1.equals(ScoredValue.fromNullable(1.1, "a"))).isFalse();
        assertThat(sv1.equals(ScoredValue.fromNullable(1.0, "b"))).isFalse();
    }

    @Test
    void testHashCode() {
        assertThat(ScoredValue.fromNullable(1.0, "a").hashCode() != 0).isTrue();
        assertThat(ScoredValue.fromNullable(0.0, "a").hashCode() != 0).isTrue();
        assertThat(ScoredValue.fromNullable(0.0, null).hashCode() == 0).isTrue();
    }

    @Test
    void toStringShouldRenderCorrectly() {

        ScoredValue<String> value = (ScoredValue<String>) ScoredValue.from(12.34, Optional.of("hello"));
        Value<String> empty = ScoredValue.fromNullable(34, null);

        assertThat(value.toString()).contains("ScoredValue[12").contains("340000, hello]");
        assertThat(empty.toString()).contains(String.format("ScoredValue[%f].empty", 0d));
    }

}
