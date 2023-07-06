/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.assets.infrastructure.repository.rest.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.irs.registryclient.decentral.DecentralDigitalTwinRegistryService;
import org.eclipse.tractusx.traceability.shelldescriptor.domain.repository.ShellDescriptorLookupMetricRepository;
import org.eclipse.tractusx.traceability.shelldescriptor.infrastructure.repository.rest.registry.RegistryApiClient;
import org.eclipse.tractusx.traceability.shelldescriptor.infrastructure.repository.rest.registry.RegistryService;
import org.eclipse.tractusx.traceability.shelldescriptor.infrastructure.repository.rest.registry.shelldescriptor.RegistryShellDescriptor;
import org.eclipse.tractusx.traceability.shelldescriptor.infrastructure.repository.rest.registry.shelldescriptor.RegistryShellDescriptorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

@ExtendWith(MockitoExtension.class)
class RegistryServiceTest {

    @InjectMocks
    private RegistryService registryService;

    @Mock
    private RegistryApiClient registryApiClient;

    @Mock
    private ShellDescriptorLookupMetricRepository registryLookupMeterRegistry;

    @Mock
    private RegistryShellDescriptorResponse registryShellDescriptorResponse;

    @Mock
    private RegistryShellDescriptor registryShellDescriptor;

    @Mock
    private DecentralDigitalTwinRegistryService decentralDigitalTwinRegistryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        String bpn = "test-bpn";
        String manufacturerIdKey = "test-manufacturer-id-key";

        registryService = new RegistryService(objectMapper, registryApiClient, bpn, manufacturerIdKey, registryLookupMeterRegistry, Clock.systemUTC(), decentralDigitalTwinRegistryService);

    }

/*    @Test
    void testFindAssets() {
        // Given
        when(registryShellDescriptor.globalAssetId()).thenReturn(new GlobalAssetId(List.of("testGlobalAssetId")));

        List<RegistryShellDescriptor> items = new ArrayList<>();
        items.add(registryShellDescriptor);
        when(registryShellDescriptorResponse.items()).thenReturn(items);
        when(registryApiClient.fetchShellDescriptors(any())).thenReturn(registryShellDescriptorResponse);

        // When
        List<ShellDescriptor> shellDescriptors = registryService.findOwnShellDescriptors();

        // Then
        assertEquals(1, shellDescriptors.size());
    }*/
}
