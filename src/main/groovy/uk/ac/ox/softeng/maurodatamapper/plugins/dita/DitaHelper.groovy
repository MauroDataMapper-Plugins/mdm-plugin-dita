package uk.ac.ox.softeng.maurodatamapper.plugins.dita

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet
import uk.ac.ox.softeng.maurodatamapper.dita.elements.langref.base.Div
import uk.ac.ox.softeng.maurodatamapper.dita.html.HtmlHelper

class DitaHelper {

    static final List<Character> disallowedCharacters = [' ','/']

    static String createDitaId(String input) {

        String response = input
        disallowedCharacters.each {
            response = response.replace(it, '_')
        }
        return response
    }


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
        return value.find(/<\/?[a-z][\s\S]*>/)
    }

}
