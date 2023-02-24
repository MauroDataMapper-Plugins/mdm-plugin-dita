/*
 * Copyright 2020-2023 University of Oxford and NHS England
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
import uk.ac.ox.softeng.maurodatamapper.api.exception.ApiException
import uk.ac.ox.softeng.maurodatamapper.api.exception.ApiNotYetImplementedException
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.provider.exporter.DataModelExporterProviderService
import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject
import uk.ac.ox.softeng.maurodatamapper.plugins.DataModelDitaBuilder
import uk.ac.ox.softeng.maurodatamapper.plugins.dita.exporter.DitaExporterService
import uk.ac.ox.softeng.maurodatamapper.security.User

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.dita.dost.util.Configuration

@Slf4j
@CompileStatic
class DitaTranstypeDataModelExporterProviderService extends DataModelExporterProviderService {

    public static final CONTENT_TYPE = 'application/octet-stream'

    DitaExporterService ditaExporterService

    @Override
    String getDisplayName() {
        'DITA Transtype Exporter'
    }

    @Override
    String getVersion() {
        getClass().getPackage().getSpecificationVersion() ?: 'SNAPSHOT'
    }

    @Override
    String getContentType() {
        CONTENT_TYPE
    }

    @Override
    String getFileExtension() {
        ''
    }

    @Override
    Boolean canExportMultipleDomains() {
        false
    }

    @Override
    String getNamespace() {
        'uk.ac.ox.softeng.maurodatamapper.plugins.dita.datamodel'
    }

    String getTranstype(Map<String, Object> parameters) {
        parameters.transtype as String
    }

    @Override
    ByteArrayOutputStream exportDataModel(User currentUser, DataModel dataModel, Map<String, Object> parameters) throws ApiException {

        String transtype = getTranstype(parameters)

        if (!Configuration.transtypes.contains(transtype)) {
            throw new ApiBadRequestException('DTEPS', "Transtype $transtype not supported")
        }

        DitaProject ditaProject = DataModelDitaBuilder.builder().buildDitaProject(dataModel)
        ditaExporterService.generateTransType(ditaProject, transtype)
    }

    @Override
    ByteArrayOutputStream exportDataModels(User currentUser, List<DataModel> dataModels, Map<String, Object> parameters) throws ApiException {
        throw new ApiNotYetImplementedException('DPES', 'exportDataModels')
    }
}
