import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SyncTools extends Thread{
	private Connection connection = null;
	private String sql = null;
	private RedisClient client = null;
	static int j = 0;
	public SyncTools(){
		client = new RedisClient();

	}
	@Override
	public void run(){
		while(true){
		try {
			update();		
			SyncTools.sleep(1000*2L);//每隔2s同步一次数据库
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			break;
		}	catch(SQLException e){
			e.printStackTrace();
			break;
		}
		}
	}
	
	private void update() throws SQLException{
		connection = DButils.getConnection();		
		
		for(int i = 1;i<101;i++){
			
			String count = client.shardedJedis.hget("Amounts",String.valueOf(i));
			sql = "update food set stock="+ count+ " where id="+String.valueOf(i);
//			System.out.println(++j+"sync:"+sql);
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.executeUpdate();
			connection.commit();		
		}		
		connection.close();
	}
	

	
}
