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

import uk.ac.ox.softeng.maurodatamapper.core.authority.Authority
import uk.ac.ox.softeng.maurodatamapper.core.container.Folder
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.bootstrap.BootstrapModels
import uk.ac.ox.softeng.maurodatamapper.datamodel.provider.exporter.DataModelExporterProviderService
import uk.ac.ox.softeng.maurodatamapper.test.integration.BaseIntegrationSpec

import groovy.util.logging.Slf4j
import spock.lang.Shared

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @since 09/05/2022
 */
@Slf4j
abstract class DataModelExporterProviderServiceSpec<E extends DataModelExporterProviderService> extends BaseIntegrationSpec {

    Authority testAuthority

    @Shared
    UUID complexDataModelId


    abstract E getExporterService()

    @Override
    void setupDomainData() {
        folder = new Folder(label: 'catalogue', createdBy: admin.emailAddress)
        checkAndSave(folder)
        testAuthority = Authority.findByLabel('Test Authority')
        checkAndSave(testAuthority)

        complexDataModelId = buildComplexDataModel().id
    }

    DataModel buildComplexDataModel() {
        BootstrapModels.buildAndSaveComplexDataModel(messageSource, folder, testAuthority)
    }

    void exportModel(UUID dataModelId, String filenameToSaveAs, int expectedBytesSize, Map<String, Object> params = [:]) {
        ByteArrayOutputStream byteArrayOutputStream = exporterService.exportDomain(admin, dataModelId, params)
        checkAndWriteToFile(byteArrayOutputStream, filenameToSaveAs, expectedBytesSize)
    }

    void exportModels(List<UUID> dataModelIds, String filenameToSaveAs, int expectedBytesSize, Map<String, Object> params = [:]) {
        ByteArrayOutputStream byteArrayOutputStream = exporterService.exportDomains(admin, dataModelIds, params)
        checkAndWriteToFile(byteArrayOutputStream, filenameToSaveAs, expectedBytesSize)
    }

    void checkAndWriteToFile(ByteArrayOutputStream byteArrayOutputStream, String filenameToSaveAs, int expectedBytesSize) {
        byte[] bytes = byteArrayOutputStream.toByteArray()
        Path outputPath = Paths.get('build/tmp', filenameToSaveAs)
        log.warn('File written to {}', outputPath)
        Files.write(outputPath, bytes)
        //        Assert.assertEquals('File should be expected size', expectedBytesSize, bytes.size())
    }
}
