package uk.ac.ox.softeng.maurodatamapper.plugins.dita

import com.fasterxml.jackson.databind.ObjectMapper
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataClass
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.EnumerationType
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.PrimitiveType
import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject3
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Body
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.DitaMap
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.MapRef
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Topic
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.TopicRef
import uk.ac.ox.softeng.maurodatamapper.dita.enums.Toc
import uk.ac.ox.softeng.maurodatamapper.dita.helpers.SimpleTableHelper
import uk.ac.ox.softeng.maurodatamapper.dita.html.HtmlHelper
import uk.ac.ox.softeng.maurodatamapper.profile.provider.ProfileProviderService

class DataClassDitaBuilder {


    static MapRef createDataClassMap(DataClass dataClass, String parentPath = "", DitaProject3 ditaProject3) {
        String path = parentPath + (parentPath==""?"":DitaProject3.FILE_SEPARATOR) + "${DitaHelper.createDitaId(dataClass.label)}"
        String topicId = DitaHelper.createDitaId(path)
        DitaMap topicMap = DitaMap.build(id: topicId) {}

        Topic topic = Topic.build(id: "${topicId}_topic") {
            title dataClass.label
            body {
                div DitaHelper.createDivFromHtmlOrMarkdown(dataClass.description)

                dataClass.dataElements.each { dataElement ->
                    section DataElementDitaBuilder.createSectionFromDataElement(dataElement, path, ditaProject3)
                }
            }
        }
        ditaProject3.registerTopic(path, topic)
        topicMap.topicRef(keyRef: "${topicId}_topic") {
            dataClass.dataClasses.each { childDataClass ->
                    mapRef(createDataClassMap(childDataClass, path, ditaProject3))
            }
        }

        ditaProject3.registerMap(path, topicMap)
        return new MapRef(keyRef: topicId, toc: Toc.YES)

    }




}
