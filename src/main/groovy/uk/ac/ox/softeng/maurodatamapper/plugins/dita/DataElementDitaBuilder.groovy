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
package uk.ac.ox.softeng.maurodatamapper.plugins.dita

import com.fasterxml.jackson.databind.ObjectMapper
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataElement
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.EnumerationType
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.PrimitiveType
import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Section
import uk.ac.ox.softeng.maurodatamapper.dita.helpers.IdHelper
import uk.ac.ox.softeng.maurodatamapper.dita.helpers.SimpleTableHelper

class DataElementDitaBuilder {

    static Section createSectionFromDataElement (DataElement dataElement, String parentPath, DitaProject ditaProject) {

        String path = parentPath + "/" + IdHelper.makeValidId(dataElement.label)
        String dataElementDitaId = IdHelper.makeValidId(path)
        new Section(id: dataElementDitaId).tap {
            title dataElement.label
            if(dataElement.description) {
                div DitaHelper.createDivFromHtmlOrMarkdown(dataElement.description)
            }
            if(dataElement.dataType instanceof PrimitiveType) {
                p "DataType: ${dataElement.dataType.label}"
            } else if(dataElement.dataType instanceof EnumerationType) {
                EnumerationType dataType = (EnumerationType) dataElement.dataType
                p {
                    b "Allowed Values:"
                }
                simpletable SimpleTableHelper.createSimpletable(
                        dataType.enumerationValues.collect {
                            ["key": it.key, "value": it.value] as Map
                        }
                )
            }
            if(dataElement.summaryMetadata[0]
                    && dataElement.summaryMetadata[0].summaryMetadataReports) {

                // We'll use the Jackson library because it preserves ordering on keys
                ObjectMapper mapper = new ObjectMapper()
                Map<String, Integer> map2 = mapper.readValue(dataElement.summaryMetadata[0].summaryMetadataReports[0].reportValue, LinkedHashMap.class)
                byte[] summaryMetadataChart = DitaSummaryMetadataHelper.drawGraph(map2)
                ditaProject.registerImage(path + "_summary.svg", dataElementDitaId + "_summary_img", "svg", summaryMetadataChart)
                image(keyRef: dataElementDitaId + "_summary_img") {
                    alt "Summary Metadata"
                }
            }

        }

//        List<ProfileProviderService> profileProviderServices = profileService.getUsedImportProfileServices(dataElement, dataModel.id, false)
//        System.err.println(profileProviderServices)
    }
}
