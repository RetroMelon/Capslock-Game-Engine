package test.NetworkTest;

public class ServerHandler implements Runnable{

	@Override
	public void run() {
		
		while(!NetworkTestMainClass.quit){
			NetworkTestMainClass.getNewConnections();
		}
		
	}
	
	

}
