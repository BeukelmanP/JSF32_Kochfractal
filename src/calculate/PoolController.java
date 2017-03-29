/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author piete
 */
public class PoolController implements Runnable {

    JSF31KochFractalFX application;
    ArrayList<Edge> edges = new ArrayList<>();
    ExecutorService pool;
    KochManager KM;
    int nxt;
    public PoolController(KochManager KM, JSF31KochFractalFX application, ExecutorService pool,int nxt) {
        this.application = application;
        this.pool = pool;
        this.nxt=nxt;
        this.KM=KM;
    }

    @Override
    public void run() {
        try {
            KochSideCalculate left = new KochSideCalculate("left", nxt);
            Future<ArrayList<Edge>> edgel = pool.submit(left);
            
            KochSideCalculate right = new KochSideCalculate("right", nxt);
            Future<ArrayList<Edge>> edger = pool.submit(right);
            
            KochSideCalculate bottom = new KochSideCalculate( "bottom", nxt);
            Future<ArrayList<Edge>> edgeb = pool.submit(bottom);
            
            edges.addAll(edgeb.get());
            edges.addAll(edgel.get());
            edges.addAll(edger.get());
            KM.addedges(edges);
        } catch (InterruptedException ex) {
            Logger.getLogger(PoolController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PoolController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
