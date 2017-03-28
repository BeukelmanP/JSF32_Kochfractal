/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
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

    ArrayList<Edge> edges = new ArrayList<>();
    TimeStamp timeCalculate = new TimeStamp();

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        kochFractal.addObserver(this);
    }

    public void changeLevel(int nxt) {
        edges.clear();
        kochFractal.setLevel(nxt);
        timeCalculate.setBegin("Start");

        KochSideCalculate left = new KochSideCalculate(this, "left", nxt);
        Thread tLeft = new Thread(left);
        tLeft.start();
        KochSideCalculate right = new KochSideCalculate(this, "right", nxt);
        Thread tRight = new Thread(right);
        tRight.start();
        KochSideCalculate bottom = new KochSideCalculate(this, "bottom", nxt);
        Thread tBottom = new Thread(bottom);
        tBottom.start();

        application.setTextNrEdges("Amount of Edges: " + kochFractal.getNrOfEdges());
    }

    public synchronized void addedges(ArrayList<Edge> edges) {
        this.edges.addAll(edges);
    }

    public synchronized void RequestDrawing() {
        count++;
        if (count == 3) {
            application.clearKochPanel();
            application.requestDrawEdges();
            count = 0;
            timeCalculate.setEnd("Ready with calculation");
            System.out.println(timeCalculate.toString());
            application.setTextCalc(timeCalculate.toString());
        }
    }

    public void drawEdges() {
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
        application.drawEdge((Edge) arg);
        edges.add((Edge) arg);
    }
}
