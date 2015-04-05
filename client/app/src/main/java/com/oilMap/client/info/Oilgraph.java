package com.oilMap.client.info;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oilMap.client.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeonsang on 2015-03-28.
 */
public class Oilgraph extends Fragment {

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.oilgraph_layout, container, false);
        // 표시할 수치값
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200,
                22030, 21200, 19500, 15500, 12600, 14000 });

        //그래픽 속성객체(achartengine라이브러리)
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        // 상단 표시 제목과 글자 크기
        renderer.setChartTitle("월별 주유비용");
        renderer.setChartTitleTextSize(20);

        // 분류에 대한 이름
        String[] titles = new String[] { "월별 소비량" };

        // 항목을 표시하는데 사용될 색상값
        int[] colors = new int[] { Color.YELLOW };

        // 분류명 글자 크기 및 각 색상 지정
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }

        // X,Y축 항목이름과 글자 크기
        renderer.setXTitle("월");
        renderer.setYTitle("소비금액");
        renderer.setAxisTitleTextSize(12);

        // 수치값 글자 크기 / X축 최소,최대값 / Y축 최소,최대값
        renderer.setLabelsTextSize(10);
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(12.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(24000);

        // X,Y축 라인 색상
        renderer.setAxesColor(Color.WHITE);
        // 상단제목, X,Y축 제목, 수치값의 글자 색상
        renderer.setLabelsColor(Color.CYAN);

        // X축의 표시 간격
        renderer.setXLabels(12);
        // Y축의 표시 간격
        renderer.setYLabels(5);

        // X,Y축 정렬방향
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // X,Y축 스크롤 여부 ON/OFF
        renderer.setPanEnabled(false, false);
        // ZOOM기능 ON/OFF
        renderer.setZoomEnabled(false, false);
        // ZOOM 비율
        renderer.setZoomRate(1.0f);
        // 막대간 간격
        renderer.setBarSpacing(0.5f);

        // 설정 정보 설정
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < titles.length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }

        // 그래프 객체 생성
        GraphicalView gv = ChartFactory.getBarChartView(getActivity(), dataset,
                renderer, BarChart.Type.STACKED);

        // 그래프를 LinearLayout에 추가
        LinearLayout graph = (LinearLayout) rootView.findViewById(R.id.graph);
        graph.addView(gv);
        return rootView;
    }
}