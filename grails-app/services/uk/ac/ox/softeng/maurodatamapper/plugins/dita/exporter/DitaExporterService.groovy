package uk.ac.ox.softeng.maurodatamapper.plugins.dita.exporter

import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject
import uk.ac.ox.softeng.maurodatamapper.dita.processor.DitaProcessor

import org.springframework.beans.factory.annotation.Value

class DitaExporterService {

    private static DitaProcessor ditaProcessor

    private String ditaOtBaseDir

    DitaExporterService(@Value('${dita.ot.basedir:}') String ditaOtBaseDir) {
        this.ditaOtBaseDir = ditaOtBaseDir
        ditaProcessor = ditaOtBaseDir ? new DitaProcessor(ditaOtBaseDir) : new DitaProcessor()
    }

    ByteArrayOutputStream generateTransType(DitaProject ditaProject, String transtype) {
        toByteArrayOutputStream ditaProcessor.generateTransType(ditaProject, transtype)
    }

    ByteArrayOutputStream generatePdf(DitaProject ditaProject) {
        toByteArrayOutputStream ditaProcessor.generatePdf(ditaProject)
    }

    ByteArrayOutputStream generateDocx(DitaProject ditaProject) {
        toByteArrayOutputStream ditaProcessor.generateDocx(ditaProject)
    }

    ByteArrayOutputStream generateDitaMapZipToByteArrayOutputStream(DitaProject ditaProject) {
        ditaProcessor.generateDitaMapZipToOutputStream(ditaProject, new ByteArrayOutputStream())
    }

    ByteArrayOutputStream toByteArrayOutputStream(byte[] bytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length)
        byteArrayOutputStream.writeBytes(bytes)
        byteArrayOutputStream
    }
}
