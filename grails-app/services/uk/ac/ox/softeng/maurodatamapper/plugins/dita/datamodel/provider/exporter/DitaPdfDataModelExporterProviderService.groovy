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

import uk.ac.ox.softeng.maurodatamapper.api.exception.ApiException
import uk.ac.ox.softeng.maurodatamapper.core.facet.MetadataService
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataClassService
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataElementService
import uk.ac.ox.softeng.maurodatamapper.datamodel.provider.exporter.DataModelExporterProviderService
import uk.ac.ox.softeng.maurodatamapper.dita.ShortDesc
import uk.ac.ox.softeng.maurodatamapper.dita.Title
import uk.ac.ox.softeng.maurodatamapper.dita.Topic
import uk.ac.ox.softeng.maurodatamapper.dita.processor.DitaProcessor
import uk.ac.ox.softeng.maurodatamapper.security.User

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class DitaPdfDataModelExporterProviderService extends DataModelExporterProviderService {

    DataClassService dataClassService
    DataElementService dataElementService
    MetadataService metadataService

    @Override
    String getDisplayName() {
        'DITA PDF Exporter'
    }

    @Override
    String getVersion() {
        getClass().getPackage().getSpecificationVersion() ?: 'SNAPSHOT'
    }

    @Override
    String getFileType() {
        'application/pdf'
    }

    @Override
    String getFileExtension() {
        'pdf'
    }

    @Override
    Boolean canExportMultipleDomains() {
        true
    }

    @Override
    String getNamespace() {
        'uk.ac.ox.softeng.maurodatamapper.plugins.dita.datamodel'
    }

    @Override
    ByteArrayOutputStream exportDataModel(User currentUser, DataModel dataModel) throws ApiException {
        exportDataModels(currentUser, [dataModel])
    }

    @Override
    ByteArrayOutputStream exportDataModels(User currentUser, List<DataModel> dataModels) throws ApiException {
/*        log.info('Exporting DataModels to Excel')
        workbook = loadWorkbookFromFilename(DATAMODELS_IMPORT_TEMPLATE_FILENAME) as XSSFWorkbook
        loadDataModelsIntoWorkbook(dataModels).withCloseable { XSSFWorkbook workbook ->
            if (!workbook) return null
            new ByteArrayOutputStream().tap { ByteArrayOutputStream exportStream ->
                workbook.write(exportStream)
                log.info('DataModels exported')
            }
        }

 */

        Topic topic = new Topic(id: "myId",
                                title: new Title(stringContent: dataModels[0].label),
                                shortDesc: new ShortDesc(stringContent: dataModels[0].description))

       // System.err.println(topic.validate())

        //System.err.println(topic.outputAsString())

        byte[] bytes = DitaProcessor.generatePDF(topic)

        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length)
        baos.writeBytes(bytes)
        return baos
    }

}
