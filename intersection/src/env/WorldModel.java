package env;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.logging.Logger;

public class WorldModel extends GridWorldModel {

    private Logger logger   = Logger.getLogger("jasonIntersection.mas2j." + WorldModel.class.getName());

    private String id = "WorldModel";

    //singleton pattern
    protected static WorldModel model = null;

    synchronized public static WorldModel create(int w, int h, int nbAgs){
        if(model == null){
            model = new WorldModel(w,h,nbAgs);
        }
        return model;
    }

    public static WorldModel get() { return model;}

    public static void destroy() { model = null;}

    private WorldModel(int w, int h, int nbAgs){
        super(w,h,nbAgs);
    }

    public String getId() { return id;}
    public void setId(String id){ this.id = id;}
    public String toString(){return id;}

    public Location[] getAgsPos() {
        Location[] positions = new Location[getNbOfAgs()];
        for(int i=0; i<getNbOfAgs(); i++){
            positions[i] = getAgPos(i);
        }
        return positions;
    }


    /** Azioni **/

    boolean move(Intersection.Move dir, int ag) throws Exception{
        Location l = getAgPos(ag);
        switch (dir){
            case UP:
                if (isFree(l.x, l.y - 1)){
                    setAgPos(ag, l.x,l.y - 1);
                }
                break;
            case DOWN:
                if(isFree(l.x,l.y+1)){
                    setAgPos(ag,l.x,l.y+1);
                }
                break;
            case RIGHT:
                if(isFree(l.x+1,l.y)){
                    setAgPos(ag,l.x+1,l.y);
                }
                break;
            case LEFT:
                if(isFree(l.x-1,l.y)){
                    setAgPos(ag,l.x-1,l.y);
                }
                break;
        }
        return true;
    }

    static WorldModel world() throws Exception {
        WorldModel model = WorldModel.create(21, 21, 4);
        model.setAgPos(0, 1, 0);
        model.setAgPos(1, 20, 0);
        model.setAgPos(2, 3, 20);
        model.setAgPos(3, 20, 20);
        return model;
    }



}
