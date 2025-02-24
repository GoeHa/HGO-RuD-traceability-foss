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
package org.eclipse.tractusx.traceability.qualitynotification.domain.contract.contract.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.common.properties.EdcProperties;
import org.eclipse.tractusx.traceability.qualitynotification.domain.contract.asset.model.CreateEdcAssetException;
import org.eclipse.tractusx.traceability.qualitynotification.domain.contract.asset.model.EdcContext;
import org.eclipse.tractusx.traceability.qualitynotification.domain.contract.contract.model.CreateEdcContractDefinitionException;
import org.eclipse.tractusx.traceability.qualitynotification.domain.contract.contract.model.EdcContractDefinitionCriteria;
import org.eclipse.tractusx.traceability.qualitynotification.domain.contract.contract.model.EdcCreateContractDefinitionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.eclipse.tractusx.traceability.common.config.EdcRestTemplateConfiguration.EDC_REST_TEMPLATE;
import static org.eclipse.tractusx.traceability.common.config.JsonLdConfigurationTraceX.NAMESPACE_EDC;
import static org.eclipse.tractusx.traceability.common.model.SecurityUtils.sanitize;
import static org.eclipse.tractusx.traceability.common.model.SecurityUtils.sanitizeHtml;

@Slf4j
@Component
public class EdcContractDefinitionService {

    private static final String ASSET_SELECTOR_ID = "https://w3id.org/edc/v0.0.1/ns/id";
    private static final String ASSET_SELECTOR_EQUALITY_OPERATOR = "=";
    private static final String ASSET_SELECTOR_TYPE = "CriterionDto";
    private static final String CONTRACT_DEFINITION_TYPE = "ContractDefinition";

    private final RestTemplate restTemplate;
    private final EdcProperties edcProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public EdcContractDefinitionService(@Qualifier(EDC_REST_TEMPLATE) RestTemplate edcRestTemplate, EdcProperties edcProperties, ObjectMapper objectMapper) {
        this.restTemplate = edcRestTemplate;
        this.edcProperties = edcProperties;
        this.objectMapper = objectMapper;
    }

    public String createContractDefinition(String notificationAssetId, String accessPolicyId) throws JsonProcessingException {
        EdcContractDefinitionCriteria edcContractDefinitionCriteria = EdcContractDefinitionCriteria
                .builder()
                .type(ASSET_SELECTOR_TYPE)
                .operandLeft(ASSET_SELECTOR_ID)
                .operandRight(notificationAssetId)
                .operator(ASSET_SELECTOR_EQUALITY_OPERATOR)
                .build();


        EdcContext edcContext = new EdcContext(NAMESPACE_EDC);
        EdcCreateContractDefinitionRequest createContractDefinitionRequest = EdcCreateContractDefinitionRequest.builder()
                .contractPolicyId(accessPolicyId)
                .edcContext(edcContext)
                .type(CONTRACT_DEFINITION_TYPE)
                .accessPolicyId(accessPolicyId)
                .id(accessPolicyId)
                .assetsSelector(edcContractDefinitionCriteria)
                .build();

        final ResponseEntity<String> createContractDefinitionResponse;
        log.info("EdcCreateContractDefinitionRequest {}", objectMapper.writeValueAsString(createContractDefinitionRequest));
        try {
            createContractDefinitionResponse = restTemplate.postForEntity(edcProperties.getContractDefinitionsPath(), createContractDefinitionRequest, String.class);
        } catch (RestClientException e) {
            log.error("Failed to create edc contract definition for {} notification asset and {} policy definition id. Reason: ", notificationAssetId, accessPolicyId, e);

            throw new CreateEdcContractDefinitionException(e);
        }

        HttpStatusCode responseCode = createContractDefinitionResponse.getStatusCode();

        if (responseCode.value() == 409) {
            log.info("{} asset contract definition already exists in the EDC", notificationAssetId);

            throw new CreateEdcContractDefinitionException("Asset contract definition already exists in the EDC");
        }

        if (responseCode.value() == 200) {
            return accessPolicyId;
        }
        String bodyWithoutLineBreaks = sanitize(createContractDefinitionResponse.getBody());
        String cleanBody = sanitizeHtml(bodyWithoutLineBreaks);
        log.error("Failed to create EDC contract definition for {} notification asset id. Body: {}, status: {}", notificationAssetId, cleanBody, createContractDefinitionResponse.getStatusCode());

        throw new CreateEdcAssetException("Failed to create EDC contract definition for %s notification asset id".formatted(notificationAssetId));
    }
}
