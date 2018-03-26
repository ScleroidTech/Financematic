package com.scleroid.financematic.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.LinearLayout;

import com.scleroid.financematic.R;

import com.scleroid.financematic.charting.charts.PieChart;
import java.util.ArrayList;

/**
 * Created by scleroid on 26/3/18.
 */
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;





    public class PiechartFragment extends Activity {

        int[] pieChartValues={25,15,20,40};

        public static final String TYPE = "type";
        private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };
        private CategorySeries mSeries = new CategorySeries("");
        private DefaultRenderer mRenderer = new DefaultRenderer();
        private GraphicalView mChartView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.piechart);

            mRenderer.setApplyBackgroundColor(true);
            mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
            mRenderer.setChartTitleTextSize(20);
            mRenderer.setLabelsTextSize(15);
            mRenderer.setLegendTextSize(15);
            mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
            mRenderer.setZoomButtonsVisible(true);
            mRenderer.setStartAngle(90);

            if (mChartView == null) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
                mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
                mRenderer.setClickEnabled(true);
                mRenderer.setSelectableBuffer(10);
                layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
            } else {
                mChartView.repaint();
            }
            fillPieChart();
        }

        public void fillPieChart(){
            for(int i=0;i<pieChartValues.length;i++)
            {
                mSeries.add(" Share Holder " + (mSeries.getItemCount() + 1), pieChartValues[i]);
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
                mRenderer.addSeriesRenderer(renderer);
                if (mChartView != null)
                    mChartView.repaint();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.activity_main, menu);
            return true;
        }
    }
