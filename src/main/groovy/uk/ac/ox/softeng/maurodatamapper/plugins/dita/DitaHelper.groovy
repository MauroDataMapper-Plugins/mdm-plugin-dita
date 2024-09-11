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
package uk.ac.ox.softeng.maurodatamapper.plugins.dita

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Div
import uk.ac.ox.softeng.maurodatamapper.dita.helpers.HtmlHelper

class DitaHelper {


    static String convertMarkdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet()

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build()
        HtmlRenderer renderer = HtmlRenderer.builder(options).build()

        // You can re-use parser and renderer instances
        Node document = parser.parse(markdown)
        String html = renderer.render(document)  // "<p>This is <em>Sparta</em></p>\n"
        html = html.replace("&gt;", ">").replace("&lt;","<")
        return html
    }

    static Div createDivFromHtmlOrMarkdown(String value) {

        if(containsHtml(value)) {
            // Assume it's html from a basic check
            HtmlHelper.replaceHtmlWithDita(value)
        } else {
            HtmlHelper.replaceHtmlWithDita(convertMarkdownToHtml(value))
        }

    }

    static boolean containsHtml(String value) {
        return value != null && value.find(/<\/?[a-z][\s\S]*>/)
    }

}
