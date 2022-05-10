package uk.ac.ox.softeng.maurodatamapper.plugins.dita.datamodel.provider.exporter

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j

/**
 * @since 09/05/2022
 */
@Slf4j
@Rollback
@Integration
class DitaDocxDataModelExporterProviderServiceSpec extends DataModelExporterProviderServiceSpec<DitaDocxDataModelExporterProviderService> {

    DitaDocxDataModelExporterProviderService ditaDocxDataModelExporterProviderService

    @Override
    DitaDocxDataModelExporterProviderService getExporterService() {
        ditaDocxDataModelExporterProviderService
    }

    void 'export complex model'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex.docx', 10)
    }
}
