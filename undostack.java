//Matt Sguerri
//CECS 261
//Tic Tac Toe Project :  The stack that undoes the tic tac 
//toe moves.

public class undostack {
	private int ItemNo, first;
	private int undo_array[] = {0,0,0,0,0,0,0,0,0};
	
	public undostack(){
		ItemNo = 0;
		first = -1; //Stack empty.
	}
	
	public boolean isEmpty(){
		return(ItemNo == 0);
	}
	
	public int size(){
		return ItemNo;
	}
	
	public int getTop(){
		return undo_array[first];
	}
	
	public void push(int toStore){
		if(ItemNo != 9){
			//Stack not full
			first++;
			undo_array[first] = toStore;
			ItemNo++;
		}	
	}
	
	public int pop(){
		if(ItemNo > 0){
			int temp = first;
			first--;
			ItemNo--;
			
			//Return popped item.
			return undo_array[temp];
			
		}
		else
			//Return zero because stack empty.
			return 0;
	}
}
