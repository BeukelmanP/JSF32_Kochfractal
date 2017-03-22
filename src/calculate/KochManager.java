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

    private ArrayList<Edge> edges = new ArrayList<>();

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        kochFractal.addObserver(this);
    }

    public void changeLevel(int nxt) {
        edges.clear();
        kochFractal.setLevel(nxt);
        TimeStamp tm = new TimeStamp();
        tm.setBegin("Start");
        application.clearKochPanel();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                kochFractal.generateLeftEdge();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                kochFractal.generateBottomEdge();
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                kochFractal.generateRightEdge();
            }
        });
        t1.start();
        t2.start();
        t3.start();
        tm.setEnd("Ready with calculation");
        System.out.println(tm.toString());
        application.setTextCalc(tm.toString());
        application.setTextNrEdges("Amount of Edges: " + kochFractal.getNrOfEdges());
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
