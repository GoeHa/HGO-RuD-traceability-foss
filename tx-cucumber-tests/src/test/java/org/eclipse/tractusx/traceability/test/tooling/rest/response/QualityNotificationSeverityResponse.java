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

package org.eclipse.tractusx.traceability.test.tooling.rest.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum QualityNotificationSeverityResponse {
    MINOR("MINOR"),
    MAJOR("MAJOR"),
    CRITICAL("CRITICAL"),
    LIFE_THREATENING("LIFE-THREATENING");

    private final String realName;

    QualityNotificationSeverityResponse(String realName) {
        this.realName = realName;
    }

    @JsonCreator
    public static QualityNotificationSeverityResponse fromValue(final String value) {
        return Stream.of(QualityNotificationSeverityResponse.values())
                .filter(severity -> severity.getRealName().equals(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Unsupported QualityNotificationSeverityRequest"));
    }

    @JsonValue
    public String getRealName() {
        return realName;
    }
}
