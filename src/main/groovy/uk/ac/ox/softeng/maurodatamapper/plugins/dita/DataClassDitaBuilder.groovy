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
