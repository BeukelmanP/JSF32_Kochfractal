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

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        kochFractal.addObserver(this);
        pool = Executors.newFixedThreadPool(3);

    }

    public void changeLevel(int nxt) {
        edges.clear();
        kochFractal.setLevel(nxt);
        timeCalculate.setBegin("Start");
        PoolController PC = new PoolController(this, application, pool, nxt);
        pool.submit(PC);
        //Thread pcThread = new Thread(PC);
       // pcThread.start();
        timeCalculate.setBegin("Test");
    }

    public synchronized void addedges(ArrayList<Edge> edges) {
        this.edges.addAll(edges);
        application.clearKochPanel();
        application.requestDrawEdges();
        timeCalculate.setEndBegin("Ready with calculation");
        application.setTextCalc(timeCalculate.toString());
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
