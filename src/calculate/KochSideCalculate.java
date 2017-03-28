/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author piete
 */
public class KochSideCalculate implements Observer, Callable {

    KochFractal KF = new KochFractal();
    KochManager KM;
    ArrayList<Edge> edges;
    String side;

    public KochSideCalculate(KochManager KM, String side, int lvl) {
        this.KM = KM;
        edges = new ArrayList<Edge>();
        this.side = side;
        KF.setLevel(lvl);
    }

    /*@Override
    public void run() {
        KF.addObserver(this);
        switch (side) {
            case "left":
                KF.generateLeftEdge();
                break;
            case "right":
                KF.generateRightEdge();
                break;
            case "bottom":
                KF.generateBottomEdge();
                break;
        }
        KM.addedges(edges);
        KM.RequestDrawing();
    }*/

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    @Override
    public void update(Observable o, Object e) {
        edges.add((Edge) e);
    }

    @Override
    public ArrayList<Edge> call() throws Exception {
         KF.addObserver(this);
        switch (side) {
            case "left":
                KF.generateLeftEdge();
                break;
            case "right":
                KF.generateRightEdge();
                break;
            case "bottom":
                KF.generateBottomEdge();
                break;
        }
        KM.addedges(edges);
        KM.RequestDrawing();
        
        return edges;
    }
}
