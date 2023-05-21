package com.e.cryptocracy.coinDetail.graph;

import com.e.cryptocracy.coinDetail.dto.coinHistory.History;
import com.e.cryptocracy.coinDetail.model.CoinDetailUI;
import com.highsoft.highcharts.common.hichartsclasses.HIHover;
import com.highsoft.highcharts.common.hichartsclasses.HILabels;
import com.highsoft.highcharts.common.hichartsclasses.HIMarker;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISpline;
import com.highsoft.highcharts.common.hichartsclasses.HIStates;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    HIOptions options = new HIOptions();
    HIChartView chartView;
    HIPlotOptions plotOptions = new HIPlotOptions();
    // int pointerInterval = 3600000;
    int pointerInterval = 3600;


    public Graph init(HIChartView chartView) {
        chartView.plugins = new ArrayList<>(Arrays.asList("series-label"));

        this.chartView = chartView;
        HITitle title = new HITitle();
        title.setText("");
        options.setTitle(title);


        HIXAxis xAxis = new HIXAxis();
        xAxis.setType("datetime");
        xAxis.setLabels(new HILabels());
        xAxis.getLabels().setOverflow("justify");
        options.setXAxis(new ArrayList<HIXAxis>() {{
            add(xAxis);
        }});

        HIYAxis yAxis = new HIYAxis();
        yAxis.setTitle(new HITitle());
        yAxis.getTitle().setText("");
        yAxis.setMinorGridLineWidth(0);
        yAxis.setGridLineWidth(0);
        yAxis.setAlternateGridColor(null);


        options.setYAxis(new ArrayList<HIYAxis>() {{
            add(yAxis);
        }});

        HITooltip tooltip = new HITooltip();
        tooltip.setValueSuffix(" m/s");
        options.setTooltip(tooltip);


        plotOptions.setSpline(new HISpline());
        plotOptions.getSpline().setLineWidth(2.5);
        plotOptions.getSpline().setStates(new HIStates());
        plotOptions.getSpline().getStates().setHover(new HIHover());
        plotOptions.getSpline().getStates().getHover().setLineWidth(2);
        plotOptions.getSpline().setMarker(new HIMarker());
        plotOptions.getSpline().getMarker().setEnabled(false);

        options.setPlotOptions(plotOptions);


        return this;
    }

    public void bindData(@NotNull ArrayList<History> coinHistoryData, CoinDetailUI coinDetailUI) {

        HISpline series1 = new HISpline();
        series1.setName(coinDetailUI.getCoinData().getName());
        Number[] series1_data = new Number[coinHistoryData.size()];
        for (int i = 0; i < coinHistoryData.size() - 1; i++) {
            Double price = 0.0;
            if (null != coinHistoryData.get(i).getPrice()) {
                price = Double.parseDouble(coinHistoryData.get(i).getPrice());
            }
            series1_data[i] = price;
        }
        series1.setData(new ArrayList<>(Arrays.asList(series1_data)));
        //if (!coinHistoryData.isEmpty())
            //plotOptions.getSpline().setPointStart(coinHistoryData.get(0).getTimestamp());

        plotOptions.getSpline().setPointInterval(pointerInterval);
        options.setSeries(new ArrayList<>(Arrays.asList(series1)));

        chartView.setOptions(options);
        chartView.redraw();

    }

}
