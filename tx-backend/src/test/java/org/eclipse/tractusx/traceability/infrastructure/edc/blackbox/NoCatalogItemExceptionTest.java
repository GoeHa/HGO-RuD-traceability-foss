/********************************************************************************
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox;

import org.eclipse.tractusx.traceability.qualitynotification.domain.base.exception.NoCatalogItemException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoCatalogItemExceptionTest {

    @Test
    void givenNoCatalogItemException_thenShouldHaveProperMessage() {
        // given
        final String message = NoCatalogItemException.MESSAGE;
        NoCatalogItemException exception = new NoCatalogItemException();

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void givenNoCatalogItemExceptionWithThrowable_thenShouldHaveProperMessageAndThrowable() {
        // given
        final String message = NoCatalogItemException.MESSAGE;
        final Throwable exceptionParam = new RuntimeException("test");
        NoCatalogItemException exception = new NoCatalogItemException(exceptionParam);

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(exceptionParam);
    }
}
