package env;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Intersection extends jason.environment.Environment{

        private Logger logger = Logger.getLogger("jasonIntersection.mas2j." + Intersection.class.getName());

        WorldModel model;

        WorldView view;

        int sleep = 0;

        boolean running = true;
        boolean hasGUI = true;

        Term up = Literal.parseLiteral("do(up)");
        Term down = Literal.parseLiteral("do(down)");
        Term right = Literal.parseLiteral("do(right)");
        Term left = Literal.parseLiteral("do(left)");
        Term skip = Literal.parseLiteral("do(skip)");

        public enum Move {
            UP, DOWN, RIGHT, LEFT
        };

        @Override
        public void init(String[] args){
            hasGUI = args[0].equals("yes");
            sleep = Integer.parseInt(args[1]);
            initWorld();
        }
        public void setSleep(int s){ sleep = s;}

        @Override
        public void stop(){
            running = false;
            super.stop();
        }

        @Override
        public  boolean executeAction(String ag, Structure action){
            boolean result = false;
            try{
                if(sleep > 0){
                    Thread.sleep(sleep);
                }

                //prendi l'id dell'agente in base al suo nome
                int agId = getAgIdBasedOnName(ag);

                if(action.equals(up)){
                    result = model.move(Move.UP, agId);
                }else if(action.equals(down)){
                    result = model.move(Move.DOWN, agId);
                }else if(action.equals(right)){
                    result = model.move(Move.RIGHT, agId);
                }else if(action.equals(left)){
                    result = model.move(Move.LEFT, agId);
                }else if(action.equals(skip)){
                    result = true;
                }else{

                    logger.info("executing: "+ action + ", but not implemented!");
                }
                if(result){
                    System.out.println(ag+" mi sto muovendo verso "+ action);
                    updateAgPercept(agId);
                    return true;
                }
            }catch (Exception e){
                logger.log(Level.SEVERE, "error executing "+ action + "for "+ ag, e);
            }
            return false;
        }

        private int getAgIdBasedOnName(String agName){
            return (Integer.parseInt(agName.substring(13))) -1;
        }

        public void initWorld() {
            try {
                model = WorldModel.world();
                clearPercepts();
                addPercept(Literal.parseLiteral("gsize(" + model.getWidth() + "," + model.getHeight() + ")"));

                if (hasGUI) {
                    view = new WorldView(model);
                    view.setEnv(this);
                }
                updateAgsPercept();
                informAgsEnvironmentChanged();
            } catch (Exception e){
                logger.warning("Error creating world "+e);
            }
        }

        public void endSimulation(String agName){
            addPercept(Literal.parseLiteral("end_of_simulation"));
            int id = getAgIdBasedOnName(agName);
            model.remove(id, model.getAgPos(id));
        }


        private void updateAgsPercept(){
            for(int i=0; i<model.getNbOfAgs(); i++){
                updateAgPercept(i);
            }
        }

        private void updateAgPercept(int ag){
            updateAgPercept("vehicle_agent" + (ag + 1), ag);
        }

        private void updateAgPercept(String agName, int ag){
            clearPercepts(agName);
            //la sua posizione
            Location l = model.getAgPos(ag);
            Literal positionLiteral = Literal.parseLiteral("pos("+l.x+","+l.y+")");
            addPercept(agName, positionLiteral);
            addPercept(agName, Literal.parseLiteral("my_name("+agName+")"));
            //posizione degli altri agenti
            for(Location pos : model.getAgsPos()){
                if(!pos.equals(l)){
                    updateAgPercept(agName, pos.x, pos.y);
                }
            }

        }

        private void updateAgPercept(String agName, int x, int y){
            if(model == null )return;
            addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",agent)"));
        }
}
