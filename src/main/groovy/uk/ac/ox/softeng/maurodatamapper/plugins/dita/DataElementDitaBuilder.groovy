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
