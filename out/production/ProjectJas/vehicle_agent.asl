/* beliefs and rules*/

last_dir(null). //l'ultimo movimento che ho fatto

all_answers_received
	:-  nb_agents(T) &
		.count(accept[source(_)], NA) &
		.count(refuse[source(_)], NR) &
		T = NA + NR.

/* belief per inizializzare */

+gsize(_,_) : true //S is the simulation Id
    <-
	   +initialized;
       !initialize_end.

/* Mail-box */
/*Devo rispondere a una richiesta di possibile movimento */			
@priority(4) +value(X,Y,P)[source(A)]: priority(My_P) & next_x(MX) & next_y(MY) & my_name(Name)
    <- if((X == MX) & (Y == MY) & (P > My_P)){
		/*Ho più priorità io perché il mio valore di My_P è minore, mando una refuse */
		  .send(A,tell, refuse(Name));
		  -value(X,Y,P)[source(A)];
		}else{
		  .send(A,tell, accept(Name));
		  -value(X,Y,P)[source(A)];
		}.

/* Un agente mi ha detto di non muovermi */	
/*@priority(3) +refuse(Ag_name)[source(_)]: true
	<- +refuse(Ag_name).*/

/* Un agente mi ha detto che per lui è ok */	
/*@priority(4) +accept(Ag_name)[source(_)]: true
	<- +accept(Ag_name).*/

	   

/* plans for reached the final position */

+!initialize_end
	: initialized
	<- 
		algs.random_number(Xf,Yf);
		+final_x(Xf);
		+final_y(Yf);
		.df_register("vehicle");
		.df_subscribe("vehicle");
		-initialized;
		!next_step(Xf,Yf).

@priority(7) +!next_step(X,Y)
    : pos(AgX,AgY) & gsize(Dim_height,Dim_width) & final_x(Xf) & final_y(Yf) & priority(P) & my_name(Name)
    <- algs.get_direction(AgX, AgY, X,Y,D,Next_X,Next_Y);
       -+last_dir(D);
	   -+next_x(Next_X);
	   -+next_y(Next_Y);
	   .df_search("vehicle",V);
	   .delete(Name,V,New_V);
	   .send(New_V,tell, value(Next_X,Next_Y,P));
	   -+nb_agents(.length(New_V));
	   // Attendo fino a 4 secondi per ricevere le risposte
	   !check_msg(X,Y,D).

+!next_step(X,Y) : not pos(_,_) //non so ancora la mia posizione
    <- !next_step(X,Y).



-!next_step(X,Y): pos(X,Y) & final_x(Xf) & final_y(Yf) //failure handling -> start again
    <- 
		if((X == Xf) & (Y == Yf)){
            		!arrived;
			}else{
             	.print(Xf);
				.print(Yf);
				-+last_dir(null);
				!next_step(X,Y);
			}.
@priority(2) +refuse(A): true
	<- true.
			
@priority(3) +accept(A): true
	<- true.
			
	
@priority(6) +!check_msg(X,Y,D): true
	<-  .wait(all_answers_received, 4000, _);
		?refuse(_);
		.abolish(refuse(_)[source(_)]);
		.abolish(accept(_)[source(_)]);
		!next_step(X,Y).
		
		
@priority(5) -!check_msg(X,Y,D): final_x(Xf) & final_y(Yf)
	<- .abolish(refuse(_)[source(_)]);
	   .abolish(accept(_)[source(_)]);
	   do(D);
	   ?pos(X_curr,Y_curr);
	   if((X_curr == Xf) & (Y_curr == Yf)){
            	!arrived;
		}else{
            	!next_step(X,Y);
		}.

		
/* end of a simulation */

@priority(1)+!arrived : pos(X,Y) & final_x(Xf) & final_y(Yf) & my_name(AgName)
  <-
     .drop_all_desires;
	 .df_deregister("vehicle");
     .print("-- END --. La mia posizione è (",X,",",Y,") e la posizione finale era (",Xf,",",Yf,")");
     algs.end_ag(AgName).
