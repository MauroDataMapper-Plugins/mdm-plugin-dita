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
package uk.ac.ox.softeng.maurodatamapper.plugins.dita

import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.category.BarRenderer
import org.jfree.chart.renderer.category.CategoryItemRenderer
import org.jfree.chart.renderer.category.StandardBarPainter
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.svg.SVGGraphics2D

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Paint
import java.awt.Rectangle
import java.awt.Stroke

class DitaSummaryMetadataHelper {

    static byte[] drawGraph(Map<String, Integer> chartData) {

        def dataset = new DefaultCategoryDataset()
        chartData.each {key, value ->
            dataset.setValue(value, "Summary Metadata", key)
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                false, false, false);
        barChart.setBackgroundPaint(Color.white)
        final CategoryItemRenderer renderer = new BarRenderer() {

            Color[] colors = [
                    new Color(30,84,37,98),
                    new Color(45,81,113,98),
                    new Color(242,148,65,98),
                    new Color(170,64,122,98),
                    new Color(68,166,156,98),
                    new Color(218,0,51,98),
                    new Color(51,123,187,98),
                    new Color(99,199,77,98),
                    new Color(106,52,83,98),
                    new Color(44,232,245,98),
                    new Color(178,77,42,98),
                    new Color(255,245,64,98),
                    new Color(244,161,176,98),
                    new Color(115,47,32,98),
                    new Color(112,112,112,98),
                    new Color(191,191,191,98)
            ]
            /**
             * Returns the paint for an item.  Overrides the default behaviour inherited from
             * AbstractSeriesRenderer.
             *
             * @param row  the series.
             * @param column  the category.
             *
             * @return The item color.
             */
            @Override
            Paint getItemPaint(final int row, final int column) {
                colors[column % colors.length]

            }

            @Override
            Paint getItemOutlinePaint(int row, int column) {
                return colors[column % colors.length]
            }

            @Override
            Stroke getItemOutlineStroke(int row, int column) {
                return new BasicStroke(2f)
            }

        }
        renderer.setBarPainter(new StandardBarPainter());

        renderer.setDrawBarOutline(true)
        renderer.setShadowVisible(false)

        CategoryPlot plot = barChart.getCategoryPlot()
        plot.setRangeGridlinePaint(Color.gray);
        plot.setBackgroundPaint(Color.white)
        plot.setRenderer(renderer)

        SVGGraphics2D g2 = new SVGGraphics2D(1200, 400);
        Rectangle r = new Rectangle(0, 0, 1200, 400);
        barChart.draw(g2, r)
        return g2.getSVGElement().bytes

    }


}
