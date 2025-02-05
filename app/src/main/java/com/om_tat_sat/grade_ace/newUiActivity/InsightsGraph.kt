package com.om_tat_sat.grade_ace.newUiActivity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import com.om_tat_sat.grade_ace.R


class InsightsGraph : AppCompatActivity() {
    var graph:GraphView?=null
    var sem1:Double?=null
    var sem2:Double?=null
    var sem3:Double?=null
    var sem4:Double?=null
    var sem5:Double?=null
    var sem6:Double?=null
    var sem7:Double?=null
    var sem8:Double?=null
    var name:String?=null
    var ogpaType:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_insights_graph)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent=getIntent()
        ogpaType=intent.getStringExtra("ogpaType")
        name=intent.getStringExtra("name")
        sem1=intent.getDoubleExtra("sem1",0.0)
        sem2=intent.getDoubleExtra("sem2",0.0)
        sem3=intent.getDoubleExtra("sem3",0.0)
        sem4=intent.getDoubleExtra("sem4",0.0)
        sem5=intent.getDoubleExtra("sem5",0.0)
        sem6=intent.getDoubleExtra("sem6",0.0)
        sem7=intent.getDoubleExtra("sem7",0.0)
        sem8=intent.getDoubleExtra("sem8",0.0)

        graph=findViewById(R.id.graph_frag_insights)

        setdata()

    }
    fun setdata() {
        graph!!.removeAllSeries()

        val series = PointsGraphSeries(arrayOf<DataPoint>())
        series.appendData(DataPoint(1.0,sem1!!.toDouble()),false,10)
        series.appendData(DataPoint(2.0,sem2!!.toDouble()),false,10)
        series.appendData(DataPoint(3.0,sem3!!.toDouble()),false,10)
        series.appendData(DataPoint(4.0,sem4!!.toDouble()),false,10)
        series.appendData(DataPoint(5.0,sem5!!.toDouble()),false,10)
        series.appendData(DataPoint(6.0,sem6!!.toDouble()),false,10)
        series.appendData(DataPoint(7.0,sem7!!.toDouble()),false,10)
        series.appendData(DataPoint(8.0,sem8!!.toDouble()),false,10)


//        series.color = R.color.black
//        graph!!.title="GPA"
//        graph!!.titleColor=R.color.black
//        graph!!.legendRenderer.isVisible=true
//        graph!!.legendRenderer.align= LegendRenderer.LegendAlign.TOP
//        graph!!.legendRenderer.textColor=R.color.black
        //        series.color=R.color.black
//        series.color = Color.BLACK;




        //USED IN BAR GRAPH
//        series.spacing=20
//        series.isDrawValuesOnTop=true
//        series.valuesOnTopColor=R.color.black
//        series.title="OGPA"
//
//        series.setValueDependentColor (object : ValueDependentColor<DataPoint> {
//            override fun get(data: DataPoint?): Int {
//                return Color.rgb(  ((data?.x!! * 255/4).toInt()), (  Math.abs(data.y *255/6).toInt()  ), 100  )
//            }
//
//        })



        //USED FOR ZOMING AND ALL

//        val graphview=graph
//        // activate horizontal zooming and scrolling
//        graphview?.getViewport()!!.setScalable(true);
//
//// activate horizontal scrolling
//        graphview?.getViewport()!!.setScrollable(true);
//
//// activate horizontal and vertical zooming and scrolling
//        graphview?.getViewport()!!.setScalableY(true);
//
//// activate vertical scrolling
//        graphview?.getViewport()!!.setScrollableY(true);




//        // styling series
//        series.title = "Random Curve 1"
//        series.color = Color.GREEN
//        series.setDrawDataPoints(true)
//        series.setDataPointsRadius(10)
//        series.setThickness(8)


        //USED FOR DOTTED LINE GRAPH
// custom paint to make a dotted line
//        val paint = Paint()
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 10f
//        paint.setPathEffect(DashPathEffect(floatArrayOf(8f, 5f), 0f))
//        series.setCustomPaint(paint)



        //USED FOR POINT GRAPH FOR SPECIFIC TYPE ALLOCATIONS
//        graph?.addSeries(series);
//        series.setShape(PointsGraphSeries.Shape.RECTANGLE);
//        series.setColor(Color.RED);
//        series.setCustomShape(CustomShape { canvas, paint, x, y, dataPoint ->
//            paint.strokeWidth = 10f
//            canvas.drawLine(x - 20, y - 20, x + 20, y + 20, paint)
//            canvas.drawLine(x + 20, y - 20, x - 20, y + 20, paint)
//        })


        //USED FOR MIX ALLOCATION
        val series3 = BarGraphSeries(
            arrayOf(
                DataPoint(0.0, -2.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        graph!!.addSeries(series3)

        val series2 = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 3.0),
                DataPoint(1.0, 3.0),
                DataPoint(2.0, 6.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 5.0)
            )
        )
        graph!!.addSeries(series2)

        graph!!.gridLabelRenderer.gridColor= Color.BLACK
//        val staticLabelsFormatter = StaticLabelsFormatter(graph)
//        staticLabelsFormatter.setHorizontalLabels(arrayOf("old", "middle", "new"))
//        staticLabelsFormatter.setVerticalLabels(arrayOf("low", "middle", "high"))
//        graph!!.gridLabelRenderer.labelFormatter = staticLabelsFormatter
        graph!!.addSeries(series)
        graph!!.visibility= View.VISIBLE



    }
}