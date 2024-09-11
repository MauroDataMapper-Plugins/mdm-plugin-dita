/*
 * Copyright 2020-2024 University of Oxford and NHS England
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
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package uk.ac.ox.softeng.maurodatamapper.plugins.dita.datamodel.provider.exporter

import uk.ac.ox.softeng.maurodatamapper.api.exception.ApiBadRequestException

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j

/**
 * @since 09/05/2022
 */
@Slf4j
@Rollback
@Integration
class DitaTranstypeDataModelExporterProviderServiceSpec extends DataModelExporterProviderServiceSpec<DitaTranstypeDataModelExporterProviderService> {

    DitaTranstypeDataModelExporterProviderService ditaTranstypeDataModelExporterProviderService

    @Override
    DitaTranstypeDataModelExporterProviderService getExporterService() {
        ditaTranstypeDataModelExporterProviderService
    }

    void 'export complex model as docx'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.docx', 10, [transtype: 'docx'])
    }

    void 'export complex model as pdf'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.pdf', 10, [transtype: 'pdf'])
    }

    void 'export complex model as xhtml'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.xhtml', 10, [transtype: 'xhtml'])
    }

    void 'export complex model as markdown'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.md', 10, [transtype: 'markdown'])
    }

    void 'export complex model as unknown'() {
        given:
        setupData()

        when:
        exportModel(complexDataModelId, 'complex_tt.wibble', 10, [transtype: 'wibble'])

        then:
        thrown(ApiBadRequestException)
    }
}
