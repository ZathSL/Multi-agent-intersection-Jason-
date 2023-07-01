/* beliefs */

last_dir(null). //l'ultimo movimento che ho fatto


/* piani per inizializzare */

+gsize(_,_) : true //S is the simulation Id
    <-
	   +initialized;
       !initialize_end.

/* plans for reached the final position */

+!initialize_end
	: initialized
	<- 
		algs.random_number(Xf,Yf);
		+final_x(Xf);
		+final_y(Yf);
		-initialized;
		!next_step(Xf,Yf).

+!next_step(X,Y)
    : pos(AgX,AgY) & gsize(Dim_height,Dim_width) & final_x(Xf) & final_y(Yf)
    <- algs.get_direction(AgX, AgY, X,Y,D);
       -+last_dir(D);
       do(D);
	   if((AgX == Xf) & (AgY == Yf)){
            		!arrived;
			}else{
            		!next_step(X,Y);
			}.

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
		


/* end of a simulation */

+!arrived : pos(X,Y) & final_x(Xf) & final_y(Yf)
  <- !end_ag;
     .drop_all_desires;
     .print("-- END --. La mia posizione è (",X,",",Y,") e la posizione finale è (",Xf,",",Yf,")").

+!end_ag: my_name(AgName)
   <- 	.print(AgName);
   		algs.end_ag(AgName).