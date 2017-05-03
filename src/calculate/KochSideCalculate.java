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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author piete
 */
public class KochSideCalculate extends Task<ArrayList<Edge>> implements Observer {

    KochFractal KF = new KochFractal();
    ArrayList<Edge> edges;
    String side;
    KochManager KM;
    private int progress = 0;

    public KochSideCalculate(String side, int lvl, KochManager KM) {
        this.KM = KM;
        this.side = side;
        edges = new ArrayList<Edge>();

        KF.setLevel(lvl);
        KF.addObserver(this);
    }

    @Override
    public ArrayList<Edge> call() throws Exception {

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
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    KM.requestDrawing();
                }});
                
        return edges;
    }

    @Override
    public void update(Observable o, Object e) {
        edges.add((Edge) e);

        final Edge E = (Edge) e;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progress++;
                updateProgress(progress, KF.getNrOfEdges() / 3);
                updateMessage(progress + "");
                KM.addEdge((Edge) E);
                KM.drawWhiteEdge((Edge) E);
            }
        });
        try {
            Thread.sleep(1);
        } catch (Exception exc) {
            Thread.currentThread().interrupt();
        }

    }
}
