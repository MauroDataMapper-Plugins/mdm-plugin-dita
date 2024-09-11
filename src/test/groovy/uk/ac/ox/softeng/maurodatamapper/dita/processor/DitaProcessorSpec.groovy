/*
 * Copyright 2020-2024 University of Oxford and NHS England
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
package uk.ac.ox.softeng.maurodatamapper.dita.processor

import uk.ac.ox.softeng.maurodatamapper.dita.DitaProject
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Topic
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.TopicRef
import uk.ac.ox.softeng.maurodatamapper.dita.enums.Toc
import uk.ac.ox.softeng.maurodatamapper.dita.processor.DitaProcessor

import groovy.util.logging.Slf4j
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

@Slf4j
class DitaProcessorSpec extends Specification {

    void "Test simple pdf generation"() {

        when:
        Topic testTopic = Topic.build(
            id: "myFirstTopic"
        ) {
            title "My first topic"
            body {
                p {
                    b "Hello, "
                    txt "World!"
                }
            }
        }

        DitaProject ditaProject = new DitaProject("myFirstDitaProject", "My First DITA Project")

        ditaProject.registerTopic("", testTopic)
        ditaProject.mainMap.topicRef(new TopicRef(keyRef: "myFirstTopic", toc: Toc.YES))

        byte[] fileContents = new DitaProcessor().generatePdf(ditaProject)
        Files.write(Paths.get('build/tmp/pdftest.pdf'), fileContents)


        then:
        noExceptionThrown()
        fileContents.size() > 7700 // The number of bytes of the generated pdf file
        fileContents.size() < 7800



        when: // Use PdfBox to validate the PDF

        Files.write(Paths.get('build/tmp/pdftest.pdf'), fileContents)
        PDDocument pdDocument = PDDocument.load(new File('build/tmp/pdftest.pdf'))
        PDFTextStripper pdfStripper = new PDFTextStripper()
        String text = pdfStripper.getText(pdDocument)
        log.debug(text)

        then:
        text.contains("My first topic")
        text.contains("Hello, World!")


    }
}
