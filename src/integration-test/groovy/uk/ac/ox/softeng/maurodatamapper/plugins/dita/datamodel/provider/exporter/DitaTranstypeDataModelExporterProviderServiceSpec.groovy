package uk.ac.ox.softeng.maurodatamapper.plugins.dita.datamodel.provider.exporter

import uk.ac.ox.softeng.maurodatamapper.api.exception.ApiBadRequestException

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j

/**
 * @since 09/05/2022
 */
@Slf4j
@Rollback
@Integration
class DitaTranstypeDataModelExporterProviderServiceSpec extends DataModelExporterProviderServiceSpec<DitaTranstypeDataModelExporterProviderService> {

    DitaTranstypeDataModelExporterProviderService ditaTranstypeDataModelExporterProviderService

    @Override
    DitaTranstypeDataModelExporterProviderService getExporterService() {
        ditaTranstypeDataModelExporterProviderService
    }

    void 'export complex model as docx'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.docx', 10, [transtype: 'docx'])
    }

    void 'export complex model as pdf'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.pdf', 10, [transtype: 'pdf'])
    }

    void 'export complex model as xhtml'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.xhtml', 10, [transtype: 'xhtml'])
    }

    void 'export complex model as markdown'() {
        given:
        setupData()

        expect:
        exportModel(complexDataModelId, 'complex_tt.md', 10, [transtype: 'markdown'])
    }

    void 'export complex model as unknown'() {
        given:
        setupData()

        when:
        exportModel(complexDataModelId, 'complex_tt.wibble', 10, [transtype: 'wibble'])

        then:
        thrown(ApiBadRequestException)
    }
}
