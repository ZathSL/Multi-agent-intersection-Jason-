package algs;

import busca.*;
import env.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;

public class get_direction extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms){
        try{
            String sAction = "skip";

            WorldModel model = WorldModel.get();

            int iagx = (int)((NumberTerm)terms[0]).solve();
            int iagy = (int)((NumberTerm)terms[1]).solve();
            int itox = (int)((NumberTerm)terms[2]).solve();
            int itoy = (int)((NumberTerm)terms[3]).solve();

            if(model.inGrid(itox,itoy)){
                Busca searchAlg = new AEstrela();
                Location lini = new Location(iagx,iagy);

                Nodo solution = searchAlg.busca(new GridState(lini, lini,new Location(itox, itoy), model, "initial"));
                if(solution != null){
                    Nodo root = solution;
                    Estado prev1 = null;
                    Estado prev2 = null;
                    while(root != null){
                        prev2 = prev1;
                        prev1 = root.getEstado();
                        root = root.getPai();
                    }
                    if(prev2 != null){
                        sAction = ((GridState)prev2).op;
                    }
                }else{
                    System.out.println("No route from (x:"+iagx+",y:"+iagy+") to (x:"+itox+",y:"+itoy+")");
                }
            }
            return un.unifies(terms[4], new Atom(sAction));
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }
    }

    class GridState implements Estado, Heuristica {

        //State information
        Location pos; //current location
        Location from,to;
        String op;
        GridWorldModel model;

        public GridState(Location l, Location from, Location to, GridWorldModel model, String op){
            this.pos = l;
            this.from = from;
            this.to = to;
            this.model = model;
            this.op = op;
        }

        public int custo(){return 1;}

        public boolean ehMeta(){return pos.equals(to);}
        public String getDescricao(){return "Grid search";}
        public int h(){return pos.distance(to);}

        public List<Estado> sucessores() {
            List<Estado> s = new ArrayList<>(4);
            //four directions
            suc(s, new Location(pos.x-1,pos.y), "left");
            suc(s, new Location(pos.x+1,pos.y), "right");
            suc(s, new Location(pos.x,pos.y-1), "up");
            suc(s, new Location(pos.x, pos.y+1), "down");
            return s;
        }

        private void suc(List<Estado> s, Location newl, String op){
            if(model.isFree(newl)){
                s.add(new GridState(newl, from,to,model,op));
            }
        }

        public boolean equals(Object o){
            try{
                GridState m = (GridState)o;
                return pos.equals(m.pos);
            }catch (Exception e){
            }
            return false;
        }

        public int hashCode() {
            return pos.hashCode();
        }

        public String toString() {
            return "(" + pos + "-" + op + ")";
        }
    }
}
