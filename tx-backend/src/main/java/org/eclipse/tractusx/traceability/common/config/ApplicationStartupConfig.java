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

package org.eclipse.tractusx.traceability.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.domain.base.IrsRepository;
import org.eclipse.tractusx.traceability.qualitynotification.application.contract.model.CreateNotificationContractRequest;
import org.eclipse.tractusx.traceability.qualitynotification.application.contract.model.NotificationMethod;
import org.eclipse.tractusx.traceability.qualitynotification.application.contract.model.NotificationType;
import org.eclipse.tractusx.traceability.qualitynotification.domain.contract.EdcNotificationContractService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.eclipse.tractusx.traceability.common.config.ApplicationProfiles.NOT_INTEGRATION_TESTS;


@Slf4j
@Component
@Profile(NOT_INTEGRATION_TESTS)
@RequiredArgsConstructor
public class ApplicationStartupConfig {
    private final IrsRepository irsRepository;
    private final EdcNotificationContractService edcNotificationContractService;
    private static final List<CreateNotificationContractRequest> NOTIFICATION_CONTRACTS = List.of(
            new CreateNotificationContractRequest(NotificationType.QUALITY_ALERT, NotificationMethod.UPDATE),
            new CreateNotificationContractRequest(NotificationType.QUALITY_ALERT, NotificationMethod.RECEIVE),
            new CreateNotificationContractRequest(NotificationType.QUALITY_INVESTIGATION, NotificationMethod.UPDATE),
            new CreateNotificationContractRequest(NotificationType.QUALITY_INVESTIGATION, NotificationMethod.RECEIVE)
    );

    @EventListener(ApplicationReadyEvent.class)
    public void registerIrsPolicy() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                irsRepository.createIrsPolicyIfMissing();
            } catch (Exception exception) {
                log.error("Failed to create Irs Policies: ", exception);
            }
        });

        executor.shutdown();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createNotificationContracts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            log.info("on ApplicationReadyEvent create notification contracts.");
            try {
                NOTIFICATION_CONTRACTS.forEach(edcNotificationContractService::handle);
            } catch (Exception exception) {
                log.error("Failed to create notification contracts: ", exception);
            }
            log.info("on ApplicationReadyEvent notification contracts created.");
        });
        executor.shutdown();
    }

}
