package algs;

import env.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import java.util.Random;

public class random_number extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception{
        try{
            Random rand = new Random();
            WorldModel model = WorldModel.get();

            int randomNumberX = rand.nextInt(model.getHeight());
            int randomNumberY = rand.nextInt(model.getWidth());

            return (un.unifies(terms[0], new NumberTermImpl(randomNumberX)) && un.unifies(terms[1], new NumberTermImpl(randomNumberY)));
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }
    }
}
