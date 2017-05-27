public class CustomEvent {
	
	static final int PLAYER_FIELD_CLICKED=2; //(int,int)
	static final int OPPONENT_FIELD_CLICKED=3; //(int,int)
	static final int OPPONENT_FIELD_HIT=5; //(int,int)
	static final int PLAYER_FIELD_HIT=6; //(int,int)
	static final int SHIP_FIRED=7; //void
	static final int SHIP_EXPLODE=8; //(int,int,int,String,String)
	
	static final int GAME_OVER=99;
	
	static final int CHAT_MESSAGE=200; // (string)
	
	static final int VIEW_START_PLACING_SHIP=100; //a dozen of args
	static final int SHIP_PLACED=101; //none?
	
	
	public int evType;
	public Object[] args;
	
	public CustomEvent(int evType, Object... args) {
		this.evType = evType;
		this.args = args;
	}
	
	public int getType() {
		return evType;
	}
	
	public Object getArg(int arg) {
		return args[arg];
	}
	
	public Object[] getArgs() {
		return args;
	}
}
