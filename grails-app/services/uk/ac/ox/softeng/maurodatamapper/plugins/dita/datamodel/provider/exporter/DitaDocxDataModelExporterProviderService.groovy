/*
 * Copyright 2020-2022 University of Oxford and Health and Social Care Information Centre, also known as NHS Digital
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


import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class DitaDocxDataModelExporterProviderService extends DitaTranstypeDataModelExporterProviderService {

    @Override
    String getDisplayName() {
        'DITA MS Word Exporter'
    }

    @Override
    String getFileType() {
        'application/docx'
    }

    @Override
    String getFileExtension() {
        'docx'
    }

    String getTranstype(Map<String, Object> parameters) {
        'docx'
    }
}
