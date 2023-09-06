package algs;

import env.WorldModel;
import env.WorldView;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class end_ag extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception{
        try{
            WorldModel model = WorldModel.get();
            // Ottieni il nome dell'agente da terms[0]
            if (terms.length > 0) {
                String agentName = terms[0].toString();
                System.out.println("L'agente " + agentName + " ha terminato correttamente il suo percorso!");

                // Chiamata all'azione end_ag passando il nome dell'agente
                int id = Integer.parseInt(agentName.substring(13)) -1;
                model.end_ag(id);
                return true;
            } else {
                System.out.println("Nessun nome dell'agente fornito.");
                return false;
            }
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }
    }
}

