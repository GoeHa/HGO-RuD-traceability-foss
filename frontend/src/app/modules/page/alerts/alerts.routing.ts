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

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AlertDetailComponent } from '@page/alerts/detail/alert-detail.component';
import { AlertsComponent } from '@page/alerts/presentation/alerts.component';
import { I18NEXT_NAMESPACE_RESOLVER } from 'angular-i18next';

export /** @type {*} */
const ALERTS_ROUTING: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: AlertsComponent,
    data: { i18nextNamespaces: [ 'page.alert' ] },
    resolve: { i18next: I18NEXT_NAMESPACE_RESOLVER },
  },
  {
    path: ':alertId',
    pathMatch: 'full',
    component: AlertDetailComponent,
    data: { i18nextNamespaces: [ 'page.alert' ] },
    resolve: { i18next: I18NEXT_NAMESPACE_RESOLVER },
  },
];

@NgModule({
  imports: [ RouterModule.forChild(ALERTS_ROUTING) ],
  exports: [ RouterModule ],
})
export class AlertsRoutingModule {
}

