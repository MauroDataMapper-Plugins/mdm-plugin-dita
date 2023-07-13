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

import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataClass
import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.DitaMap
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.MapRef
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Topic
import uk.ac.ox.softeng.maurodatamapper.dita.enums.Toc
import uk.ac.ox.softeng.maurodatamapper.dita.helpers.IdHelper

class DataClassDitaBuilder {


    static MapRef createDataClassMap(DataClass dataClass, String parentPath = "", DitaProject ditaProject) {
        String path = parentPath + (parentPath==""?"":DitaProject.FILE_SEPARATOR) + "${IdHelper.makeValidId(dataClass.label)}"
        String topicId = IdHelper.makeValidId(path)
        DitaMap topicMap = DitaMap.build(id: topicId) {}

        Topic topic = Topic.build(id: "${topicId}_topic") {
            title dataClass.label
            body {
                if(dataClass.description) {
                    div DitaHelper.createDivFromHtmlOrMarkdown(dataClass.description)
                }

                dataClass.dataElements.each { dataElement ->
                    section DataElementDitaBuilder.createSectionFromDataElement(dataElement, path, ditaProject)
                }
            }
        }
        ditaProject.registerTopic(path, topic)
        topicMap.topicRef(keyRef: "${topicId}_topic") {
            dataClass.dataClasses.each { childDataClass ->
                    mapRef(createDataClassMap(childDataClass, path, ditaProject))
            }
        }

        ditaProject.registerMap(path, topicMap)
        return new MapRef(keyRef: topicId, toc: Toc.YES)

    }




}
