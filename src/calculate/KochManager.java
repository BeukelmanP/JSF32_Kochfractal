/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author piete
 */
public class KochManager implements Observer {

    KochFractal kochFractal = new KochFractal();
    private JSF31KochFractalFX application;
    private int count = 0;
    ExecutorService pool;

    ArrayList<Edge> edges = new ArrayList<>();
    TimeStamp timeCalculate = new TimeStamp();

    KochSideCalculate bottom;
    KochSideCalculate left;
    KochSideCalculate right;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        kochFractal.addObserver(this);
        pool = Executors.newFixedThreadPool(3);

    }

    public JSF31KochFractalFX getApplication() {
        return application;
    }

    public synchronized void addEdge(Edge e) {
        edges.add(e);
    }

    public synchronized void requestDrawing() {
        count++;
        if (count > 2) {
            application.clearKochPanel();
            application.requestDrawEdges();
            timeCalculate.setEndBegin("Ready with calculation");
//            application.resetTextCalc();
//            application.setTextCalc(timeCalculate.toString());
            System.out.println(Thread.currentThread().getName());
        }
    }

    public synchronized void drawWhiteEdge(Edge e) {
        application.drawEdgeWhite(e);

    }

    public void changeLevel(int nxt) {
        count = 0;
        edges.clear();
        kochFractal.setLevel(nxt);
        timeCalculate.setBegin("Start");

        //check if calculations are running
        if (left != null) {
            left.KF.cancel();
            left.cancel();
            application.clearKochPanel();
        }
        if (right != null) {
            right.KF.cancel();
            right.cancel();
            application.clearKochPanel();
        }
        if (bottom != null) {
            bottom.KF.cancel();
            bottom.cancel();
            application.clearKochPanel();
        }

        //unbind progressbars
        application.progressLeft.progressProperty().unbind();
        application.progressRight.progressProperty().unbind();
        application.progressBottom.progressProperty().unbind();
        application.messageLeft.textProperty().unbind();
        application.messageRight.textProperty().unbind();
        application.messageBottom.textProperty().unbind();
        count = 0;

        //start new threads for calculation
        left = new KochSideCalculate("left", nxt, this);
        pool.submit(left);
        application.progressLeft.progressProperty().bind(left.progressProperty());
        application.messageLeft.textProperty().bind(left.messageProperty());
        right = new KochSideCalculate("right", nxt, this);
        pool.submit(right);
        application.progressRight.progressProperty().bind(right.progressProperty());
        application.messageRight.textProperty().bind(right.messageProperty());
        bottom = new KochSideCalculate("bottom", nxt, this);
        pool.submit(bottom);
        application.progressBottom.progressProperty().bind(bottom.progressProperty());
        application.messageBottom.textProperty().bind(bottom.messageProperty());
        System.out.println(Thread.currentThread().getName());
    }

    public synchronized void drawEdges() {
        application.clearKochPanel();
        TimeStamp tm = new TimeStamp();
        tm.setBegin("Start");
        for (Edge e : edges) {
            application.drawEdge(e);
        }
        tm.setEnd("Ready with Drawing");
        System.out.println(tm.toString());
        application.setTextDraw(tm.toString());
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        edges.add((Edge) arg);
    }
}
