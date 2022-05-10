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

    void exportModel(UUID dataModelId, String filenameToSaveAs, int expectedBytesSize) {
        ByteArrayOutputStream byteArrayOutputStream = exporterService.exportDomain(admin, dataModelId)
        checkAndWriteToFile(byteArrayOutputStream, filenameToSaveAs, expectedBytesSize)
    }

    void exportModels(List<UUID> dataModelIds, String filenameToSaveAs, int expectedBytesSize) {
        ByteArrayOutputStream byteArrayOutputStream = exporterService.exportDomains(admin, dataModelIds)
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
