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
