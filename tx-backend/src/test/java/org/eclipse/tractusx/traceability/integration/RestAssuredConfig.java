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

package org.eclipse.tractusx.traceability.integration;

import io.restassured.RestAssured;
import org.eclipse.tractusx.traceability.common.config.ApplicationProfiles;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@TestConfiguration
@Profile(ApplicationProfiles.SPRING_INTEGRATION_TESTS)
class RestAssuredConfig {

    @EventListener(WebServerInitializedEvent.class)
    void onServletContainerInitialized(WebServerInitializedEvent event) {
        RestAssured.port = event.getWebServer().getPort();
    }
}
