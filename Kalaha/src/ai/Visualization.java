/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import com.mxgraph.model.mxGraphModel;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.jgraph.JGraph;
import org.jgraph.graph.ParentMap;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ListenableGraph;

import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DirectedMultigraph;

/**
 *
 * @author Torkelz
 */
public class Visualization extends JApplet{
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(530,320);
    //private final Thread thr;

    
    private JGraphModelAdapter<Node, DefaultEdge> jgAdapter;
    private ListenableGraph<Node, DefaultEdge> g;
    private JGraph jgraph;
    private Node root = null;
    
    public Visualization(){
    
    // Let's see if we can lay it out
//    JGraphFacade jgf = new JGraphFacade(jgraph);
//    JGraphFastOrganicLayout layoutifier = new JGraphFastOrganicLayout();
//    layoutifier.run(jgf);
//    System.out.println("Layout complete");
//
//    final Map nestedMap = jgf.createNestedMap(true, true);
//    jgraph.getGraphLayoutCache().edit(nestedMap);
//
//    jgraph.getGraphLayoutCache().update();
//    jgraph.refresh();

    //frame.setVisible(true);
        
        init();

        
        JFrame frameVisual = new JFrame();
        frameVisual.getContentPane().add(this);
        frameVisual.setTitle("JGraphT Adapter to JGraph Demo");
        frameVisual.setDefaultCloseOperation(frameVisual.EXIT_ON_CLOSE);
        frameVisual.pack();
        frameVisual.setVisible(true);
    
    }
    
    public void init(){        
        
        g = new ListenableDirectedMultigraph<Node, DefaultEdge>(
                    DefaultEdge.class);

        jgAdapter = new JGraphModelAdapter<Node, DefaultEdge>(g);

        jgraph = new JGraph(jgAdapter);
        
        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);

//        String v1 = "v1";
//        String v2 = "v2";
//        String v3 = "v3";
//        String v4 = "v4";
        Integer v1 = MoveIndicator.ONE.getValue();
        Integer v2 = MoveIndicator.TWO.getValue();
        Integer v3 = MoveIndicator.THREE.getValue();
        Integer v4 = MoveIndicator.FOUR.getValue();
        
        // add some sample data (graph manipulated via JGraphT)
//        g.addVertex(v1);
//        g.addVertex(v2);
//        g.addVertex(v3);
//        g.addVertex(v4);
////        
//        g.addEdge(v1, v2);
//        g.addEdge(v1, v3);
//        g.addEdge(v1, v4);
        
        
//        final  JGraphHierarchicalLayout hir = new JGraphHierarchicalLayout();
//        final JGraphFacade graphFacade = new JGraphFacade(jgraph);      
//        hir.run(graphFacade);
//        final Map nestedMap = graphFacade.createNestedMap(true, true);
//        jgraph.getGraphLayoutCache().edit(nestedMap);
        
        Node root = new Node(null, MoveIndicator.FAILURE);
        root.addChild(MoveIndicator.ONE);
        root.addChild(MoveIndicator.TWO);
        root.addChild(MoveIndicator.THREE);
        root.addChild(MoveIndicator.FOUR);
        root.addChild(MoveIndicator.FIVE);
        
        
        
    }

//    @Override
//    public void run() {
////    List roots = new ArrayList(); 
////    Iterator vertexIter = graphModel.vertexSet().iterator(); 
////    while (vertexIter.hasNext()) { 
////        Object vertex = vertexIter.next(); 
////        if (graphModel.inDegreeOf(vertex) == 0) { 
////            roots.add(graphAdapter.getVertexCell(vertex)); 
////        } 
////    } 
////
////    JGraphLayoutAlgorithm layout = new SugiyamaLayoutAlgorithm(); 
////    layout.applyLayout(graph, layout, roots.toArray(), null);
//    }
    public void add(Node _parent, Node _node){
        g.addVertex(_node);
        g.addEdge(_parent, _node);
    }
    
    public void setRoot(Node _root){
        //((mxGraphModel) jgraph.getModel()).clear();
        root = _root;
        g.addVertex(root);
    }
    public void updateTree(){
        //
        
        Object roots[] = {root};
        //Object roots = getRoots(); // replace getRoots with your own Object array of the cell tree roots. NOTE: these are the root cell(s) of
        //the tree(s), not the roots of the graph model.
        JGraphFacade facade = new JGraphFacade(jgraph, roots); // Pass the facade the JGraph instance
        JGraphLayout layout = new JGraphTreeLayout(); // Create an instance of the appropriate layout
        layout.run(facade); // Run the layout on the facade.
        Map nested = facade.createNestedMap(true, true); // Obtain a map of the resulting attribute changes from the facade
        jgraph.getGraphLayoutCache().edit(nested); // Apply the results to the actual graph
        
        int dummy = 0;
    }
    
    
    
    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }
    
    
        /**
     * a listenable directed multigraph that allows loops and parallel edges.
     */
    private static class ListenableDirectedMultigraph<V, E>
        extends DefaultListenableGraph<V, E>
        implements DirectedGraph<V, E>
    {
        private static final long serialVersionUID = 1L;

        ListenableDirectedMultigraph(Class<E> edgeClass)
        {
            super(new DirectedMultigraph<V, E>(edgeClass));
        }
    }
}
