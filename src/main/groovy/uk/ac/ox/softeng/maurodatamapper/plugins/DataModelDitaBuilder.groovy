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
package uk.ac.ox.softeng.maurodatamapper.plugins

import uk.ac.ox.softeng.maurodatamapper.core.facet.Metadata
import uk.ac.ox.softeng.maurodatamapper.core.facet.Rule
import uk.ac.ox.softeng.maurodatamapper.core.model.CatalogueItem
import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataClass
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.DataType
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.EnumerationType
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.ModelDataType
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.ReferenceType
import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Body
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Section
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Topic
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.TopicRef
import uk.ac.ox.softeng.maurodatamapper.dita.enums.Toc
import uk.ac.ox.softeng.maurodatamapper.dita.helpers.IdHelper

/**
 * @since 10/05/2022
 */
class DataModelDitaBuilder {

    static DitaProject buildDitaProject(DataModel dataModel) {
        new DitaProject(dataModel.label, dataModel.label
        ).tap {
            registerTopic("", Topic.build(id: "datamodel") {
                title dataModel.label
                shortdesc dataModel.description
                topic(buildDataTypesTopic(dataModel))
                topic(buildDataClassTopic(dataModel))
                body(buildCatalogueItemBit(dataModel))
            })
            mainMap.topicRef(new TopicRef(keyRef: "datamodel", toc: Toc.YES))
        }
    }

    static Topic buildDataTypesTopic(DataModel dataModel) {
        Topic.build(id: 'datatypes') {
            title 'Data Types'
            shortdesc 'All DataTypes used by this DataModel'

            body {
                if (dataModel.primitiveTypes) section buildDataTypeSection('Primitive DataType', dataModel.primitiveTypes)
                if (dataModel.enumerationTypes) section buildDataTypeSection('Enumeration DataTypes', dataModel.enumerationTypes)
                if (dataModel.referenceTypes) section buildDataTypeSection('Reference DataTypes', dataModel.referenceTypes)
                if (dataModel.modelDataTypes) section buildDataTypeSection('Model DataTypes', dataModel.modelDataTypes)
            }
        }
    }

    static Section buildDataTypeSection(String name, Collection<? extends DataType> dataTypes) {
        Section.build {
            title name
            simpletable {
                stHead {
                    stentry {txt 'Name'}
                    stentry {txt 'Description'}
                }
                dataTypes.sort().each {dataType ->
                    strow {
                        stentry {txt dataType.label}
                        stentry {
                            txt dataType.description
                            if (dataType.domainType == DataType.ENUMERATION_DOMAIN_TYPE) {
                                txt 'Enumerations'
                                dl {
                                    (dataType as EnumerationType).enumerationValues.each {ev ->
                                        dlentry {
                                            dt ev.key
                                            dd ev.value
                                        }
                                    }
                                }
                            }
                            if (dataType.domainType == DataType.REFERENCE_DOMAIN_TYPE) {
                                txt "References: ${(dataType as ReferenceType).referenceClass.path}"
                            }
                            if (dataType.domainType == DataType.MODEL_DATA_DOMAIN_TYPE) {
                                txt "References: ${ModelDataType.getModelResource(dataType as ModelDataType)?.path ?: 'UNKNOWN'}"
                            }
                        }
                    }
                }
            }
        }
    }

    static Topic buildDataClassTopic(DataModel dataModel) {
        Topic.build(id: 'dataclasses') {
            title 'DataClasses'
            dataModel.childDataClasses.eachWithIndex {dc, i ->
                topic(buildDataClassBit(dc, '', i))
            }
        }
    }

    static Topic buildDataClassTopic(DataClass dc, String dcId) {
        Topic.build(id: "${dcId}.dataclasses") {
            title 'DataClasses'
            if (dc.dataClasses) {
                dc.dataClasses.eachWithIndex {cdc, j ->
                    topic(buildDataClassBit(cdc, "${dcId}.", j))
                }
            }
        }
    }

    static Topic buildDataClassBit(DataClass dc, String idPrefix, int i) {
        String dcId = "${idPrefix}dc[${i}]"
        return Topic.build {
            id dcId
            title dc.label
            shortdesc dc.description

            if (dc.dataClasses) {
                topic(buildDataClassTopic(dc, dcId))
            }
            if (dc.dataElements) {
                topic {
                    title 'DataElements'
                    body {
                        table {
                            tgroup(cols: 3) {
                                tHead {
                                    row {
                                        entry(colName: 'name') {txt 'Name'}
                                        entry(colName: 'datatype') {txt 'DataType'}
                                        entry(colName: 'description') {txt 'Description'}
                                    }
                                }
                                tBody {

                                    dc.dataElements.sort().each {dt ->
                                        row {
                                            entry(colName: 'name') {txt dt.label}
                                            entry(colName: 'datatype') {txt dt.dataType.label}
                                            entry(colName: 'description') {txt dt.description}
                                        }
                                    }


                                }
                            }
                        }
                    }
                }
            }
            body(buildCatalogueItemBit(dc))
        }
    }

    static Body buildCatalogueItemBit(CatalogueItem catalogueItem) {
        Body.build {
            if (catalogueItem.metadata) {
                section buildMetadataSection(catalogueItem.metadata)
            }
            if (catalogueItem.rules) {
                section buildRulesSection(catalogueItem.rules)
            }
        }
    }

    static Section buildMetadataSection(Collection<Metadata> metadata) {
        Section.build {
            title 'Metadata'
            simpletable {
                stHead {
                    stentry {txt 'Namespace'}
                    stentry {txt 'Key'}
                    stentry {txt 'Value'}
                }
                metadata.sort().each {md ->
                    strow {
                        stentry {txt md.namespace}
                        stentry {txt md.key}
                        stentry {txt md.value}
                    }
                }
            }
        }
    }

    static Section buildRulesSection(Collection<Rule> rules) {
        Section.build {
            title 'Rules'
            rules.each {rule ->
                dl {
                    dlentry {
                        dt 'Name'
                        dd rule.name
                    }
                    dlentry {
                        dt 'Description'
                        dd rule.description
                    }

                }
                if (rule.ruleRepresentations) {
                    simpletable {
                        stHead {
                            stentry {txt 'Language'}
                            stentry {txt 'Representation'}
                        }

                        rule.ruleRepresentations.sort().each {md ->
                            strow {
                                stentry {txt md.language}
                                stentry {txt md.representation}
                            }
                        }
                    }
                } else {
                    p {b 'No rule representations available for rule'}
                }

            }

        }
    }

}
