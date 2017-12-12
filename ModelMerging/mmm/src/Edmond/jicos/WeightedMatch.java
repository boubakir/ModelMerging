package Edmond.jicos;

import java.util.ArrayList;

/* ************************************************************************* *
 *                                                                           *
 *        Copyright (c) 2004 Peter Cappello  <cappello@cs.ucsb.edu>          *
 *                                                                           *
 *    Permission is hereby granted, free of charge, to any person obtaining  *
 *  a copy of this software and associated documentation files (the          *
 *  "Software"), to deal in the Software without restriction, including      *
 *  without limitation the rights to use, copy, modify, merge, publish,      *
 *  distribute, sublicense, and/or sell copies of the Software, and to       *
 *  permit persons to whom the Software is furnished to do so, subject to    *
 *  the following conditions:                                                *
 *                                                                           *
 *    The above copyright notice and this permission notice shall be         *
 *  included in all copies or substantial portions of the Software.          *
 *                                                                           *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF       *
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.   *
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY     *
 *  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,     *
 *  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE        *
 *  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                   *
 *                                                                           *
 * ************************************************************************* */

/**
 *  This class implements a [minimum | maximum] cost maximum matching based on
 *  an O(n^3) implementation of Edmonds' algorithm, as presented by Harold N.
 *  Gabow in his Ph.D. dissertation, Computer Science, Stanford University,
 *  1973.
 *
 * Created on July 8, 2003, 11:00 AM
 *
 * @author  Peter Cappello
 */
/*
 * Gabow's implementation of Edmonds' algorithm is described in chapter 6 of
 * Nonbipartite Matching, of Combinatorial Optimization, Networks and Matroids,
 * authored by Eugene Lawler,
 * published by Holt, Rinehart, and Winston, 1976.
 * <p>
 * Lawler's description is referred to in the Notes and References section of
 * chapter 11, Weighted Matching, of
 * Combinatorial Optimation, Algorithms and Complexity,
 * authored by Christos Papadimitriou and Kenneth Steiglitz,
 * published by Prentice-Hall, 1982.
 * <p>
 * The implementation here mimics Gabow's description and Rothberg's C coding of
 * Gabow's description, making it easy for others to see the correspondence
 * between this code, Rothberg's C code, and Gabow's English description of the
 * algorithm, given in Appendix D of his dissertation.
 * <p>
 * Since the code mimics Gabow's description (Rothberg's C code does so even
 * more closely), the code below is not object-oriented, much less good Java. It
 * also violates many Java naming conventions.
 * <p>
 * Currently, the graph is assumed to be complete & symmetric.
 * <p>
 * It is unclear to me why cost values are doubled in setUp() and intialize(). I
 * think it may have to do with the implementation being entirely integer. When
 * I remove the doubling, the minimum weight maximum match fails on the test
 * graph.
 */


public final class WeightedMatch 
{
    // constants        
    /** The value that indicates that a minimum cost maximum match is sought. */    
    public final static boolean MINIMIZE = true;
    /** The value that indicates that a maximum cost maximum match is sought. */    
    public final static boolean MAXIMIZE = false;
    
    private final static boolean DEBUG = false;    
    private final static int UNMATCHED = 0;
    
    private int[][] costs;
    
    private  int V;           // the number of vertices in the graph
    private  int E;           // the number of edges    in the graph
    private  int dummyVertex; // artifical vertex for boundary conditions
    private  int dummyEdge;   // artifical edge   for boundary conditions
        
    private  int[] a;    // adjacency list
    private  int[] end;    
    private  int[] mate;
    private  int[] weight;
    
    private  int[] base;    
    private  int[] lastEdge = new int[3]; // Used by methods that undo blossoms.
    private  int[] lastVertex;
    private  int[] link;
    private  int[] nextDelta;
    private  int[] nextEdge;
    private  int[] nextPair;
    private  int[] nextVertex;                        
    private  int[] y;
    
    private  int delta, lastDelta;
    @SuppressWarnings("unused")
	private  int newBase, nextBase, oldBase, stopScan, pairPoint;
    private  int neighbor, newLast, nextPoint;
    @SuppressWarnings("unused")
	private  int firstMate, newMate, oldFirst, oldMate, secondMate;
    private  int f, nxtEdge, nextE, nextU;
    
    private  int e, v, i; // edge, vertex, index used by several methods.
    
    /** Construct a WeightedMatch object. */    
    public WeightedMatch( int[][] costs )
    {
        this.costs = costs;
    }
    
    /** The int cost matrix is assumed to be square and symmetric (undirected).
     * <p>
     * if ( minimizeWeight ) <br>
     * &nbsp;&nbsp;&nbsp;&nbsp; performs a minimum cost maximum matching;<br>
     * else<br>
     *    performs a maximum cost maximum matching.
     * @param minimizeWeight if ( minimizeWeight )
     *    performs a minimum cost maximum matching;
     * else
     *    performs a maximum cost maximum matching.
     * @return an array of the form vertex[i] = j, where vertex i is matched to vertex j.
     * The numbering of vertices is 1, ..., n, where the graph has n vertices. Thus,
     * the 0th element of the returned int[] is undefined.
     * <p>
     * I don't particularly like this, I am just propagating custom. I may change
     * this, at some point, so that vertices are numbered 0, ..., n-1.
     */
    public  int [] weightedMatch( boolean minimizeWeight )
    {                        
        if ( DEBUG )
        {
            System.out.println("weightedMatch: input costs matrix:");
            for ( int i = 0; i < costs.length; i++ )
            {
                System.out.print(" Row " + i + ":");
                for ( int j = i + 1; j < costs.length; j++)
                {
                    System.out.print(" " + costs[i][j]);
                }
                System.out.println("");
            }
        }
        
        int loop = 1;
        
        // W0. Input.
        input( costs );
        
        // W1. Initialize.
        initialize( costs, minimizeWeight );
        
        while ( true )
        {
            if ( DEBUG )
            {
                System.out.println("\n *** A U G M E N T " + (loop++));
            }
            // W2. Start a new search.
            delta = 0;
            for ( v = 1; v <= V; v++ )
            {
                if ( mate[v] == dummyEdge )
                {
                    // Link all exposed vertices.
                    pointer( dummyVertex, v, dummyEdge );
                }
            }
            
            if ( DEBUG )
            {
                for ( int q = 1; q <= V + 1; q++ )
                {           
                    System.out.println("W2: i: " + q + 
                    ", mate: " + mate[q] +
                    ", nextEdge: " + nextEdge[q] +
                    ", nextVertex: " + nextVertex[q] +
                    ", link: " + link[q] +
                    ", base: " + base[q] +
                    ", lastVertex: " + lastVertex[q] +
                    ", y: " + y[q] +
                    ", nextDelta: " + nextDelta[q] +
                    ", lastDelta: " + lastDelta
                    );
                }
            }
            
            // W3. Get next edge.
            while ( true )
            {                
                i = 1;
                for ( int j = 2; j <= V; j++ )
                {
                    /* !!! Dissertation, p. 213, it is nextDelta[i] < nextDelta[j]
                     * When I make it <, the routine seems to do nothing.
                     */
                    if ( nextDelta[i] > nextDelta[j] )
                    {
                        i = j;
                    }
                }                
                
                // delta is the minimum slack in the next edge.
                delta = nextDelta[i];
                
                if ( DEBUG )
                {
                    System.out.println("\nW3: i: " + i + " delta: " + delta);
                }
                
                if ( delta == lastDelta )
                {
                     if ( DEBUG )
                    {
                        System.out.println("\nW8: delta: " + delta + " lastDelta: " + lastDelta);
                    }
                    // W8. Undo blossoms.
                    setBounds();
                    unpairAll();
                    for ( i = 1; i <= V; i++ )
                    {
                        mate[i] = end[ mate[i] ];
                        if ( mate[i] == dummyVertex )
                        {
                            mate[i] = UNMATCHED;
                        }
                    }
                    
                    // W9.
                    return mate;
                }
                
                // W4. Assign pair links.
                v = base[i];
                
                if ( DEBUG )
                {
                    System.out.println("W4. delta: " + delta + " v: " + v + " link[v]: " + link[v]);
                }
                
                if ( link[v] >= 0 )
                {
                    if ( pair() )
                    {
                        break;
                    }             
                }
                else
                {
                    // W5. Assign pointer link.
                    if ( DEBUG )
                    {
                        System.out.println("W5. delta: " + delta + " v: " + v );
                    }
                    int w = bmate( v ); // blossom w is matched with blossom v.
                    if ( link[w] < 0 )
                    {
                        if ( DEBUG )
                        {
                            System.out.println("WeightedMatch: delta: " + delta + " v: " + v + " w: " + w + " link[w]: " + link[w]);
                        }
                        // w is unlinked.
                        pointer( v, w, oppEdge( nextEdge[i] )  );
                    }
                    else
                    {
                        // W6. Undo a pair link.
                        if ( DEBUG )
                        {
                            System.out.println("W6. v: " + v + " w: " + w );
                        }
                        unpair( v, w );
                    }
                }
            }
            
            // W7. Enlarge the matching.
            lastDelta -= delta;
            setBounds();
            int g = oppEdge( e );
            rematch( bend( e ), g );
            rematch( bend( g ), e );
        }
    }
    
    // Begin 5 simple functions
    //
     private int bend( int e ) { return base[ end[ e ] ]; }
    
     private int blink( int v ) { return base[ end[ link[ v] ] ]; }
    
     private int bmate( int v ) { return base[ end[ mate[ v ] ] ]; }
    
     private int oppEdge( int e ) 
    { 
        return ( (e - V) % 2 == 0 ) ? e - 1 : e + 1;
    }
    
     private int slack( int e )
    {
        return y[ end[ e ] ] + y[ end[ oppEdge( e ) ] ] - weight[ e ];
    }
    //
    // End 5 simple functions
    
     private void initialize( int[][] costs, boolean minimizeWeight )
    {        
        // initialize basic data structures
        setUp( costs );
        
        if ( DEBUG )
        {
            for ( int q = 0; q < V + 2*E + 2; q++ )
            {
                System.out.println("initialize: i: " + q + ", a: " + a[q] + " end: " + end[q] + " weight: " + weight[q] );
            }  
        }
        
        dummyVertex = V + 1;
        dummyEdge = V + 2*E + 1;
        end[ dummyEdge ] = dummyVertex; 
        
        if ( DEBUG )
        {
            System.out.println("initialize: dummyVertex: " + dummyVertex + 
            " dummyEdge: " + dummyEdge + " oppEdge(dummyEdge): " + oppEdge(dummyEdge));
        }
        
        int maxWeight = Integer.MIN_VALUE, minWeight = Integer.MAX_VALUE;
        for ( int i = 0; i < V; i++ )
        for ( int j = i + 1; j < V; j++ )
        {
            int cost = 2*costs[i][j];
            if ( cost > maxWeight )
            {
                maxWeight = cost;
            }
            if ( cost < minWeight )
            {
                minWeight = cost;
            }
        }
        
        if ( DEBUG )
        {
            System.out.println("initialize: minWeight: " + minWeight + ", maxWeight: "
            + maxWeight);
        }
        
        // If minimize costs, invert weights
        if ( minimizeWeight )
        {
            if ( V % 2 != 0 )
            {
                throw new IllegalArgumentException( "|V| must be even for a " +
                                             "minimum cost maximum matching." );
            }
            maxWeight += 2; // Don't want all 0 weight
            for ( int i = V + 1; i <= V + 2*E; i++)                
            {
                weight[i] = maxWeight - weight[i];
                //System.out.println("initialize: inverted weight[" + i + "]: " +
                //weight[i]);
            }
            maxWeight = maxWeight - minWeight;
        }    
        
        lastDelta = maxWeight/2;
        if ( DEBUG )
        {
            System.out.println("initialize: minWeight: " + minWeight + 
                " maxWeight: " + maxWeight + " lastDelta: " + lastDelta);  
        }
        
        int allocationSize = V + 2;
        mate =       new int[ allocationSize ];
        link =       new int[ allocationSize ];
        base =       new int[ allocationSize ];
        nextVertex = new int[ allocationSize ];
        lastVertex = new int[ allocationSize ];
        y =          new int[ allocationSize ];
        nextDelta =  new int[ allocationSize ];
        nextEdge =   new int[ allocationSize ];
        
        allocationSize = V + 2*E + 2;
        nextPair = new int[ allocationSize ];
        
        for ( i = 1; i <= V + 1; i++ )
        {
            mate[i] = nextEdge[i] = dummyEdge;
            nextVertex[i] = 0;
            link[i] = -dummyEdge;
            base[i] = lastVertex[i] = i;
            y[i] = nextDelta[i] = lastDelta;
            
            if ( DEBUG )
            {
                System.out.println("initialize: v: " + v + ", i: " + i + 
                ", mate: " + mate[i] +
                ", nextEdge: " + nextEdge[i] +
                ", nextVertex: " + nextVertex[i] +
                ", link: " + link[i] +
                ", base: " + base[i] +
                ", lastVertex: " + lastVertex[i] +
                ", y: " + y[i] +
                ", nextDelta: " + nextDelta[i] +
                ", lastDelta: " + lastDelta
                );
            }
        }
        //System.out.println("initialize: complete.");
    }
    
     private void input( int[][] costs )
    {
        V = costs.length;
        E = V*(V - 1)/2;
        
        int allocationSize = V + 2*E + 2;
        a      = new int[ allocationSize ];
        end    = new int[ allocationSize ];
        weight = new int[ allocationSize ];
        for ( int i = 0; i < allocationSize; i++ )
        {
            a[i] = end[i] = weight[i] = 0;
            //System.out.println("input: i: " + i + ", a: " + a[i] + " " + end[i] + " " + weight[i] );
        } 
        
        if ( DEBUG )
        {
            System.out.println("input: V: " + V + ", E: " + E + 
                ", allocationSize: " + allocationSize);
        }
    }
    
    /** Updates a blossom's pair list, possibly inserting a new edge. 
     * It is invoked by scan and mergePairs. 
     * It is invoked with global int e set to the edge to be inserted, neighbor
     * set to the end vertex of e, and pairPoint pointing to the next pair to be
     * examined in the pair list.
     */
     private void insertPair()
    {        
        if ( DEBUG )
        {
            System.out.println("Insert Pair e: " + e + " " + end[oppEdge(e)] + "- " + end[e]);
        }
        int deltaE; // !! check declaration.
        
        // IP1. Prepare to insert.
        deltaE = slack( e )/2;
        
        if ( DEBUG )
        {
            System.out.println("IP1: deltaE: " + deltaE);
        }
        
        nextPoint = nextPair[ pairPoint ];
        
        // IP2. Fint insertion point.
        for ( ; end[ nextPoint ] < neighbor; nextPoint = nextPair[ nextPoint ] )
        {
            pairPoint = nextPoint;            
        }
                
        if ( DEBUG )
        {
            System.out.println("IP2: nextPoint: " + nextPoint);
        }
        
        if ( end[ nextPoint ] == neighbor )
        {
            // IP3. Choose the edge.
            if ( deltaE >= slack( nextPoint ) / 2 ) // !!! p. 220. reversed in diss.
            {
                return;
            }
            nextPoint = nextPair[ nextPoint ];
        }
        
        // IP4. 
        nextPair[ pairPoint ] = e;
        pairPoint = e;
        nextPair[ e ]  = nextPoint;
        
        // IP5. Update best linking edge.
        if ( DEBUG )
        {
            System.out.println("IP5: newBase: " + newBase + " nextDelta[newBase]: "
            + nextDelta[ newBase ] + " deltaE: " + deltaE);
        }  
        if ( nextDelta[ newBase ] > deltaE )
        {
            nextDelta[ newBase ] = deltaE;                     
        }
    }
    
    /** Links the unlined vertices inthe path P( end[e], newBase ). 
     * Edge e completes a linking path. 
     * Invoked by pair.
     * Pre-condition:
     *    newBase == vertex of the new blossom.
     *    newLast == vertex that is currently last on the list of vertices for
     *               newBase's blossom.
     */
     private void linkPath( int e )
    {
        int u; // !! declaration?
        
        if ( DEBUG )
        {
            System.out.println("Link Path e = " + end[oppEdge(e)] + " END[e]: " + end[e]);
        }
        
        // L1. Done?
        for ( /* L1. */ v = bend( e ); v != newBase; v = bend( e ) )
        {
            // L2. Link next vertex.
            u = bmate( v );            
            link[u] = oppEdge( e );
            
            if ( DEBUG )
            {
                System.out.println(" L2: LINK[" + u + "]: " + link[u]);
            }                        
            
            // L3. Add vertices to blossom list.
            nextVertex[ newLast ] = v;
            nextVertex[ lastVertex[ v ] ] = u;
            newLast = lastVertex[ u ];
            i = v;
            
            // L4. Update base.
            do
            {
                base[i] = newBase;
                i = nextVertex[ i ];
            }
            while ( i != dummyVertex );
            
            // L5. Get next edge.
            e = link[v];
        }               
    }
    
    /** Merges a subblossom's pair list into a new blossom's pair list.
     * Invoked by pair.
     * Pre-condition:
     *    v is the base of a previously linked subblossom.
     */
     private void mergePairs( int v )
    {
        if ( DEBUG )
        {
            System.out.println("Merge Pairs v = " + v + " lastDelta: " + lastDelta );
        }
        // MP1. Prepare to merge.        
        nextDelta[v] = lastDelta;
        
        if ( DEBUG )
        {
            System.out.println(" mergePairs: v: " + v + " nextDelta[v]: "
            + nextDelta[ v ] + " lastDelta: " + lastDelta);
        } 
        
        pairPoint = dummyEdge;
        for ( f = nextEdge[v]; f != dummyEdge; )
        {
            // MP2. Prepare to insert.
            e = f;
            neighbor = end[e];
            f = nextPair[f];
            
            // MP3. Insert edge.
            if ( base[ neighbor ] != newBase )
            {
                insertPair();
            }
        }       
    }
    
    /**  Processes an edge joining 2 linked vertices. Invoked from W4 of 
     * weightedMatch. 
     * Pre-condition:
     *    v is the base of 1 end of the linking edge.
     * Pair checks whether the edge completes an augmenting path or a pair link
     * path.
     * returns true iff an augmenting path is found.
     */
     private boolean pair()
    {
        if ( DEBUG )
        {
            System.out.println("pair: v: " + v);
        }
        int u, w, temp;
        
        // PA1. Prepare to find edge.
        e = nextEdge[ v ];
        
        // PA2. Find edge.
        while ( slack( e ) != 2 * delta )
        {
            e = nextPair[ e ];
        }
        
        // PA3. Begin flagging vertices.
        w = bend( e );
        link[ bmate( w ) ] = -e; // Flag bmate(w)
        
        if ( DEBUG )
        {
            System.out.println(" PA3 LINK[" + bmate( w ) + "]: " + link[ bmate( w ) ] +
            " w: " + w +
            " bmate: " + bmate( w ) +
            " e: " + e);
        }
        
        u = bmate( v );
        
        // PA4. Flag vertices.
        while ( link[u] != -e ) // u is NOT FLAGGED
        {
            link[u] = -e;
            
            if ( DEBUG )
            {
                System.out.println(" PA4 LINK[" + u + "]: " + link[u] +
                " e: " + e);
            }
            
            if ( mate[w] != dummyEdge )
            {
                temp = v;
                v = w;
                w = temp;
            }
            v = blink( v );
            u = bmate( v );
        }
        
        // PA5. Augmenting path?
        if ( u == dummyVertex && v != w )
        {
            return true; // augmenting path found
        }
        
        // PA6. Prepare to link vertices.
        newLast = newBase = v;
        oldFirst = nextVertex[ v ];
        
        // PA7. Link vertices.
        linkPath( e );
        linkPath( oppEdge( e ) );
        
        // PA8. Finish linking.
        nextVertex[ newLast ] = oldFirst;
        if ( lastVertex[ newBase ] == newBase )
        {
            lastVertex[ newBase ] = newLast;
        }
        
        // PA9. Start new pair list.
        nextPair[ dummyEdge ] = dummyEdge;
        mergePairs( newBase );
        i = nextVertex[ newBase ];       
        do
        {
            // PA10. Merge subblossom's pair list
            mergePairs( i );
            i = nextVertex[ lastVertex[ i ] ];
            
            // PA11. Scan subblossom.
            scan( i, 2*delta - slack( mate[ i ] ) );
            i = nextVertex[ lastVertex[ i ] ];
        }
        // PA12. More blossoms?
        while ( i != oldFirst );
        
        // PA14.
        return false;
    }
    
    /**
     * pointer assigns a pointer link to a vertex. Vertices u & v are the bases
     * of blossoms matched with each other. Edge e joins a vertex in blossom u
     * to a linked vertex.
     *
     * pointer is invoked by weightedMatch to link exposed vertices (step W2)
     * and to link unlinked vertices (step W5), and from unpair (steps UP5, UP7)
     * to relink vertices.
     */
     private void pointer( int u, int v, int e )
    {
        if ( DEBUG )
        {
            System.out.println("\nPointer on entry: delta: " + delta + " u: " + u + " v: " + v + " e: " + e +
            " oppEdge(e) = " + oppEdge(e) );
            //System.out.println("Pointer: end[oppEdge(e)]" + end[oppEdge(e)]);
            //System.out.println("Pointer: u, v, e = " + u + " " + v + " " + end[oppEdge(e)] + " " + end[e]);
        }
        int i, del; // !! Is this declaration correct. Check both i & del.
        
        if ( DEBUG )
        {
            System.out.println("\nPointer: delta: " + delta + " u: " + u + " v: " + v + " e: " + e +
            " oppEdge(e) = " + oppEdge(e) );
            //System.out.println("Pointer: end[oppEdge(e)]" + end[oppEdge(e)]);
            //System.out.println("Pointer: u, v, e = " + u + " " + v + " " + end[oppEdge(e)] + " " + end[e]);
        }
        
        // PT1. Reinitialize values that may have changed.
        link[u] = -dummyEdge;
        
        if ( DEBUG )
        {
            System.out.println("PT1. LINK[" + u + "]: " + link[u] +  
            " dummyEdge: " + dummyEdge);
        }
        nextVertex[ lastVertex[u] ] = nextVertex[ lastVertex[v] ] = dummyVertex;
        
        //System.out.println("Pointer: PT2. " + (lastVertex[u] != u ));
        // PT2. Find unpairing value.
        if ( lastVertex[u] != u )
        {
            // u's blossom contains other vertices
            i = mate[ nextVertex[u] ];
            //System.out.println("Pointer: true: i: " + i);
            del = -slack( i )/2;           
        }
        else
        {
            //System.out.println("Pointer: false: lastDelta: " + lastDelta);
            del = lastDelta;
        }
        i = u;
        
        if ( DEBUG )
        {
            System.out.println(" PT3. del: " + del);
        }
        
        // PT3.
        for ( ; i != dummyVertex; i = nextVertex[i] )
        {
            y[i] += del;
            nextDelta[i] += del;
            if ( DEBUG )
            {
                System.out.println(" PT3: i: " + i + " nextDelta[i]: "
                + nextDelta[ i ] + " del: " + del);
            } 
        }
        
        // PT4. Link v & scan.
        
        if ( DEBUG )
        {
            System.out.println("POINTER: ?? LINK < 0 ?? LINK: " + link[v] + " v: " + v);
        }
        
        if ( link[v] < 0 )
        {            
            // v is unlinked.
            link[v] = e;
            
            if ( DEBUG )
            {
                System.out.println("PT4. LINK[" + v + "]: " + link[v] + " e: " + e);
            }
            
            nextPair[dummyEdge] = dummyEdge;
            scan( v, delta);
        }
        else
        {
            /* Yes, it looks like this statement can be factored out, and put
             * after if condition, eliminating the else. 
             * However, link is a global variable used in scan: 
             *
             * I'm not fooling with it!
             */
            link[v] = e;
            
            if ( DEBUG )
            {
                System.out.println("PT4.1. LINK[" + v + "]: " + + link[v] + " e: " + e);
            }
        }
    }
    
    /** Changes the matching along an alternating path.
     * Invoked by weightedMatch (W7) to augment the matching, and from unpair
     * (UP2) and unpairAll (UA6) to rematch a blossom.
     *
     * Pre-conditions:
     *    firstMate is the first base vertex on the alternating path.
     *    Edge e is the new matched edge for firstMate.
     */
     private void rematch( int firstMate, int e )
    {
        if ( DEBUG )
        {
            System.out.println( "rematch: firstMate: " + firstMate + 
            ", end[ oppEdge( e ) ]: " + end[ oppEdge( e )] + ", end[e]: " + end[e] );
        }
        // R1. Start rematching.
        mate[ firstMate ] = e;
        nextE = -link[ firstMate ];
        
        // R2. Done?
        while ( nextE != dummyEdge )
        {
            // R3. Get next edge.
            e = nextE;
            f = oppEdge( e );
            firstMate  = bend( e );
            secondMate = bend( f );
            nextE = -link[ firstMate ];
            
            // R4. Relink and rematch.
            link[ firstMate  ] = -mate[ secondMate ];
            link[ secondMate ] = -mate[ firstMate ];
            
            if ( DEBUG )
            {
                System.out.println("R4: LINK[" + firstMate + "]: " + link[firstMate] +
                " link[" + secondMate + "]: " + link[secondMate] + " firstMate: " + firstMate + 
                " secondMate: " + secondMate + " mate[secondMate]: " + mate[ secondMate ] +
                " mate[fisrtMate]: " + mate[ firstMate ]);
            }
            
            mate[ firstMate  ] = f;
            mate[ secondMate ] = e;
        }
    }
    
    /**
     * scan scans a linked blossom. Vertex x is the base of a blossom that has
     * just been linked by either pointer or pair. del is used to update y.
     * scan is invoked with the list head nextPair[dummyEdge] pointing to the
     * 1st edge on the pair list of base[x].
     */
     private void scan( int x, int del )
    {
        int u, delE; // !! is this declaration correct? Check both u & delE.
        
        if ( DEBUG )
        {
            System.out.println("Scan del= " + del + " x= " + x);
        }
        
        // SC1. Initialize.
        newBase = base[x];
        stopScan = nextVertex[ lastVertex[x] ];
        for ( ; x != stopScan; x = nextVertex[x] /* SC7. */ )
        {            
            // SC2. Set bounds & initialize for x.
            y[x] += del;
            nextDelta[x] = lastDelta;
            
            if ( DEBUG )
            {
                System.out.println(" SC2: x: " + x + " lastDelta: " + lastDelta
                + " nextDelta: " + nextDelta[x]);
            }
            
            pairPoint = dummyEdge;
            e = a[x]; // !!! in dissertation: if there are no edges, go to SC7.
            for ( ; e != 0; e = a[e] /* SC6. */ )
            {
                // SC3. Find a neighbor.
                neighbor = end[e];
                u = base[neighbor];
                
                // SC4. Pair link edge.
                if ( DEBUG )
                {
                    System.out.println("Scan: SC4: link[" + u + "]: " + link[u] );
                }
                if ( link[u] < 0 )
                {                
                    
                    if ( link[ bmate( u ) ] < 0 || lastVertex[u] != u )
                    {
                        delE = slack( e );
                        if ( nextDelta[ neighbor ] > delE )
                        {
                            nextDelta[ neighbor ] = delE;
                            nextEdge [ neighbor ] = e;
                            
                            if ( DEBUG )
                            {
                                System.out.println(" SC4.1: neighbor: " + neighbor + 
                                " nextDelta[neighbor]: " + nextDelta[neighbor] + 
                                " delE: " + delE);
                            } 
                        }
                    }
                }
                else
                {
                    // SC5.
                    if ( u != newBase )
                    {
                        if ( DEBUG )
                        {
                            System.out.println("Scan: SC5: u: " + u + " newBase: " + newBase);
                        }
                        insertPair();
                    }
                }
            }                                   
        }
        
        // SC8. 
        nextEdge[newBase] = nextPair[dummyEdge];       
    }
    
    /** Updates numerical bounds for linking paths.
     * Invoked by weigtedMatch
     *
     * Pre-condition:
     *    lastDelta set to bound on delta for the next search.
     */
     private void setBounds()
    {
        if ( DEBUG )
        {
            System.out.println("setBounds: entered: delta: " + delta);
        }
        int del;
        
        // SB1. Examine each vertex.
        for ( v = 1; v <= V; v++ )
        {
            // SB2. Is vertex a linked base?
            if ( link[v] < 0 || base[v] != v )
            {
                // SB8. Update nextDelta.
                nextDelta[v] = lastDelta;
                
                if ( DEBUG )
                {
                    System.out.println(" setBounds: v: " + v + " nextDelta[v]: "
                                + nextDelta[v] + " lastDelta: " + lastDelta);
                } 
                continue;
            }
            
            // SB3. Begin processing linked blossom.
            link[v] = -link[v];
            
            if ( DEBUG )
                {
                    System.out.println(" SB3: LINK[" + v + "]: " + link[v] );
                } 
            
            i = v;
            
            // SB4. Update y in linked blossom.
            // !! discrepancy: dissertation (do-while); Rothberg (while)
            while ( i != dummyVertex )
            {
                y[i] -= delta;
                i = nextVertex[i];
            }
            
            // SB5. Is linked blossom matched?
            f = mate[v];
            if ( f != dummyEdge )
            {
                // SB6. Begin processing unlinked blossom.
                i = bend( f );
                del = slack( f );
                
                // SB7. Update y in unlinked blossom.
                // !! discrepancy: dissertation (do-while); Rothberg (while)
                while ( i != dummyVertex )
                {
                    y[i] -= del;
                    i = nextVertex[i];
                }
            }
            nextDelta[v] = lastDelta;
            
            if ( DEBUG )
            {
                System.out.println(" setBounds: v: " + v + " nextDelta[v]: "
                                + nextDelta[v] + " lastDelta: " + lastDelta);
            } 
        }
    }                    
    
     private void setUp( int[][] costs )
    {        
        int currentEdge = V + 2;
        //System.out.println("setUp: initial currentEdge: " + currentEdge);
        for ( int i = V; i >= 1; i-- )
        for ( int j = i - 1; j >= 1; j-- )
        {
            /* !! in Rothberg, I only understand the SetMatrix function in the
             * file "term.c". 
             * He seems to treat each matrix entry as a directed arc weight in
             * a symmetric graph. Thus, he multiplies its value by 2, 
             * representing the undirected symmetric equivalent.
             *
             * If I deviate from this, I must also change initialize, which also
             * refers to the costs matrix.
             */
            if ( DEBUG )
            {
                System.out.println("setUp: i-1: " + i + " j-1: " + j + " cost: " + costs[i-1][j-1]);
            }
            int cost = 2*costs[i-1][j-1];
            weight[ currentEdge - 1 ] = weight[ currentEdge ] = cost;
            end[ currentEdge - 1 ] = i;
            end[ currentEdge ] = j;
            a[ currentEdge ] = a[i];
            a[i] = currentEdge;
            a[ currentEdge - 1 ] = a[j];
            a[j] = currentEdge - 1;
            /*
            if ( DEBUG )
            {
                System.out.println("setUp: i: " + i + ", j: " + j + 
                ", costs[i-1,j-1]: " + costs[i-1][j-1] + ", currentEdge: " + currentEdge + 
                "\n\t weight: " + weight[currentEdge-1] + " " + weight[currentEdge-1] +
                "\n\t end: " + end[currentEdge-1] +" " + end[currentEdge-1] +
                "\n\t a: " + a[currentEdge-1] +" " + a[currentEdge-1] + 
                "\n\t a[i], a[j]: " + a[i] +" " + a[j]  
             );
            }
             */
            currentEdge += 2;
        }
    }
    
    /** Unlinks subblossoms in a blossom. 
     * Invoked by unpair and unpairAll
     * Pre-conditions:
     *    oldbase is the base of the blossom to be unlinked.
     * unlink preserves the values of the links it undoes, for use by rematch 
     * and unpair.
     *
     * unlink sets the array lastEdge, for use by unpair and unpairAll.
     */
     private void unlink( int oldBase )
    {
        if ( DEBUG )
        {
            System.out.println("unlink: oldBase: " + oldBase );
        }
        
        // UL1. Prepare to unlink paths.
        i = newBase = nextVertex[ oldBase ];
        nextBase = nextVertex[ lastVertex[ newBase ] ];
        e = link[ nextBase ];
        
        // Loop is executed twice, for the 2 paths containing the subblossom.       
        for ( int j = 1; j <= 2; j++ )
        {
            do
            {                
                // UL2. Get next path edge.
                if ( DEBUG )
                {
                    System.out.println("UL2. j: " + j);
                }
                nxtEdge = oppEdge( link[ newBase ] );
                                
                for ( int k = 1; k <= 2; k++ )
                {
                    // UL3. Unlink blossom base.
                    link[ newBase ] = -link[ newBase ];
                    
                    if ( DEBUG )
                    {
                        System.out.println("UL3. LINK[" + newBase + "]: " + link[ newBase ]);
                    }
                    
                    // UL4. Update base array.
                    do
                    {
                        base[i] = newBase;
                        i = nextVertex[i];
                    }
                    while ( i != nextBase );
                    
                    // UL5. Get next vertex.
                    newBase = nextBase;
                    nextBase = nextVertex[ lastVertex[ newBase ] ];
                }
            }
            // UL6. More vertices?
            while ( link[ nextBase ] == nxtEdge );
            
            // UL7. End of path.
            if ( j == 1 )
            {
                lastEdge[1] = nxtEdge;
                nxtEdge = oppEdge( e );
                if ( link[ nextBase ] == nxtEdge )
                {
                    if ( DEBUG )
                    {
                        System.out.println("UL7*. Going to UL2.");
                    }
                    continue; // check the control flow logic.
                }
            }
            break;
        }
        lastEdge[2] = nxtEdge;
        
        // UL8. Update blossom list.
        if ( base[ lastVertex[ oldBase ] ] == oldBase )
        {
            nextVertex[ oldBase ] = newBase;
        }
        else
        {
            nextVertex[ oldBase ] = dummyVertex;
            lastVertex[ oldBase ] = oldBase;
        }
    }
    
    /** Undoes a blossom by unlinking, rematching, and relinking subblossoms.
     * Invoked by weightedMatch
     * Pre-conditions:
     *    oldBase == an unlinked vertex, the base of the blossom to be undone.
     *    oldMate == a linked vertex, the base of the blossom matched to oldBase
     *
     * It uses a local variable newbase.
     */
     private void unpair( int oldBase, int oldMate )
    {       
        int e, newbase, u; // !! Are these global (i.e., )?
        
        if ( DEBUG )
        {
            System.out.println("Unpair oldBase: " + oldBase + ", oldMate: " + oldMate);
        }
        
        // UP1. Unlink vertices.
        unlink( oldBase );
        
        // UP2. Rematch a path.
        newbase = bmate( oldMate );
        if ( newbase != oldBase )
        {
            link[ oldBase ] = -dummyEdge;
            rematch( newbase, mate[ oldBase ] );
            link[secondMate] = (f == lastEdge[1]) ? -lastEdge[2] : -lastEdge[1];            
        }
        
        // UP3. Examine the linking edge.
        e = link[ oldMate ];
        u = bend( oppEdge( e ) );
        if ( u == newbase )
        {            
            // UP7. Relink oldmate.
            pointer( newbase, oldMate, e );
            return;
        }
        link[ bmate( u ) ] = -e;
        // UP4. missing from dissertation.
        do
        {
            // UP5. Relink a vertex
            e = -link[u];
            v = bmate( u );
            pointer( u, v, -link[v] );
            
            // UP6. Get next blossom.
            u = bend( e );
        }
        while ( u != newbase );
        e = oppEdge( e );
        
        // UP7. Relink oldmate
        pointer( newbase, oldMate, e );
    }
    
    /** Undoes all the blossoms, rematching them to get the final matching.
     * Invoked by weightedMatch.
     */
     private void unpairAll()
    {
        int u;
        
        // UA1. Unpair each blossom.
        for ( v = 1; v <= V; v++ )
        {
            if ( base[v] != v || lastVertex[v] == v )
            {
                continue;
            }
            
            // UA2. Prepare to unpair.
            nextU = v;
            nextVertex[ lastVertex[ nextU ] ] = dummyVertex;
            
            while ( true )
            {
                // UA3. Get next blossom to unpair.
                u = nextU;
                nextU = nextVertex[ nextU ];
                
                // UA4. Unlink a blossom.
                unlink( u );
                if ( lastVertex[u] != u )
                {
                    // UA5. List subblossoms to unpair.
                    f = ( lastEdge[2] == oppEdge( e ) ) ? lastEdge[1] : lastEdge[2];
                    nextVertex[ lastVertex[ bend( f ) ] ] = u;
                }
                
                // UA6. Rematch blossom.
                newBase = bmate( bmate( u ) );
                if ( newBase != dummyVertex && newBase != u )
                {
                    link[u] = -dummyEdge;
                    rematch( newBase, mate[u] );
                }
                
                // UA7. Find next blossom to unpair.
                while ( lastVertex[ nextU ] == nextU && nextU != dummyVertex )
                {
                    nextU = nextVertex[ nextU ];
                }
                if ( lastVertex[ nextU ] == nextU && nextU == dummyVertex )
                {
                    break;
                }               
            }
        }
    }
     
    public int[] getMatched( int[] mates )
    {
        /* WeightedMatch.weightedMatch returns mates, indexed and valued
         * 1, ..., V. Shift the index to 0, ... , V-1 and put the values in
         * this range too (i.e., decrement them). 
         */
        int[] matches = new int[ costs.length ];
        System.arraycopy( mates, 1, matches, 0, matches.length );
        for ( int i = 0; i < matches.length; i ++ )
        {
            matches[i]--;
        }   
        
        return matches;
    }
    
     /** Used as a convenient repository of a simple unit test.
      * @param args The number of vertices and the seed for a random Euclidean graph, to use as a
      * unit test.
      */     
    
       public static void test(){
        
        int[][] costs =
        {
            {    0,    6,    8, 1000, 1000, 1000 },
            {    6,    0, 1000,    6,    3, 1000 },
            {    8, 1000,    0,    3,    3, 1000 },
            { 1000,    6,    3,    0, 1000,    8 },
            { 1000,    3,    3, 1000,    0,    6 },
            { 1000, 1000, 1000,    8,    6,    0 },
        };
         
        final int size = 6;
        /*final int size = Integer.parseInt( args[0] );
        
        GraphEuclidean graph = new GraphEuclidean( size, size, 100 );
        
        int[][] costs = graph.getCosts();*/
        
        
        
        
        long startTime = System.currentTimeMillis();
        WeightedMatch weightedMatch = new WeightedMatch( costs );
        
        //int[] mate = weightedMatch.weightedMatch( WeightedMatch.MINIMIZE );
        int[] mate = weightedMatch.weightedMatch( WeightedMatch.MAXIMIZE );
        long stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time (in milliseconds) for " + size + 
                                    " node graph is " + (stopTime - startTime));
        
        for ( int i = 1; i <= costs.length; i++ )
        {
            System.out.println(" " + i + " matches " + mate[i] );
        }
        System.out.println("main: Done.");
    }
    
    public static ArrayList<int[]> getResult(int[][] costs){
		
    	
    	ArrayList<int[]> result = new ArrayList<int[]>();


         WeightedMatch weightedMatch = new WeightedMatch( costs );
         
         //int[] mate = weightedMatch.weightedMatch( WeightedMatch.MAXIMIZE );
         int[] mate = weightedMatch.weightedMatch( WeightedMatch.MAXIMIZE );
         
         
         
         for ( int i = 1; i <= costs.length; i++ )
         {
             //System.out.println(" " + i + " matches " + mate[i] );
        	 int[] match = new int[2];
        	 match[0] = i-1;
        	 match[1] =  mate[i]-1;
        	 
        	 boolean trouve = false;
        	 
        	 int j = 0;
        	 while(j<result.size() && !trouve){
        		 if(
        			(result.get(j)[0]==match[0])||
        			(result.get(j)[1]==match[0])||
        			(result.get(j)[0]==match[1])||
        			(result.get(j)[1]==match[1])
        				 ){
        			 trouve = true;
        		 }
        		 j++;
        	 }
        	 if(trouve){
        		 
        	 }else{
        		 result.add(match);
        	 }
        	
        	 
         }
         //System.out.println("main: Done.");
    	
         
    	return result;

    }


    public static void afficher(int costs[][], String tab){
    	
    	  for(int i = 0; i < costs.length; i++){
   		   
   		   for(int j = 0; j < costs.length; j++){
   			   
   			   System.out.print(costs[i][j]+tab);
   			   
   		   } 
   		   System.out.println();
   	   }
    }
    
    public static void main( String[] args ){
    	//test();
    	test2();
    }
    
public static void test2(){
        
    /*    
	int[][] costs =
        {
            {    0,    6,    8, 1000, 1000, 1000 },
            {    6,    0, 1000,    6,    3, 1000 },
            {    8, 1000,    0,    3,    3, 1000 },
            { 1000,    6,    3,    0, 1000,    8 },
            { 1000,    3,    3, 1000,    0,    6 },
            { 1000, 1000, 1000,    8,    6,    0 },
        };*/

        
	
	   int[][] costs =
	        {
	            {    0,     8, 1000, 1000, 1000 },
	            {    8,     0,    3,    3, 1000 },
	            { 1000,     3,    0, 1000,    8 },
	            { 1000,     3, 1000,    0,    6 },
	            { 1000,  1000,    8,    6,    0 },
	        };
	
	   
	 afficher(costs, "       ");
	   
	   
	   
        ArrayList<int[]> result = getResult(costs);
        for ( int i = 0; i < result.size(); i++ ){
       	 System.out.println(result.get(i)[0]+"   "+result.get(i)[1]);
        }


}




}


