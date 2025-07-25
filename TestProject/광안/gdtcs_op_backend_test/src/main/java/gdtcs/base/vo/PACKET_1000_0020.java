package gdtcs.base.vo;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 
 * 
 * @파일명		: 테이블 개정 지시-응답(1000/0020)
 * @프로그램설명	: 기초정보관리 > 개정지시 
 * @작성자		: 박형철
 * @작성일		: 2024. 7. 28.
 * @version 1.0
 * @개정이력
 *
 * <pre>
 *  수정일            수정자              수정내용
 *  -------    --------    ---------------------------
 *	 2022. 10. 18.   박형철     최초생성
 * </pre>
 *
 */
public class PACKET_1000_0020 {
	byte[] data = new byte[44];
	
	public PACKET_1000_0020(){
		
	}
	
	public PACKET_1000_0020( String PACKET_LENGTH, 
			String PACKET_SYSTEM_CODE_SEND, 
			String PACKET_SYSTEM_CODE_RECV, 
			String PACKET_OP_DIV,
			String PACKET_WORK_DIV,
			String PACKET_SEND_DT,
			String PACKET_REPLY_CODE) {
		setStart(new byte[]{0x02});
		setLength(new byte[]{'0','0','3','8'});
		setSYSTEM_CODE_SEND(PACKET_SYSTEM_CODE_SEND.getBytes());
		setSYSTEM_CODE_RECV(PACKET_SYSTEM_CODE_RECV.getBytes());
		setOP_DIV(PACKET_OP_DIV.getBytes());
		setWORK_DIV(PACKET_WORK_DIV.getBytes());
		setSEND_DT(PACKET_SEND_DT.getBytes());
		setREPLY_CODE(PACKET_REPLY_CODE.getBytes());
		setEnd(new byte[]{0x03});
	}
	
	public byte[] getStart(){
		return Arrays.copyOfRange(data, 0, 1);
	}
	
	public void setStart(byte[] value){
		setData(value, 0);
	}
	
	public byte[] getLength(){
		return Arrays.copyOfRange(data, 1, 5);
	}
	
	public void setLength(byte[] value){
		setData(value, 1);
	}
	
	public byte[] getSYSTEM_CODE_SEND(){
		return Arrays.copyOfRange(data, 5, 11);
	}
	
	public void setSYSTEM_CODE_SEND(byte[] value){
		setData(value, 5);
	}
	
	public byte[] getSYSTEM_CODE_RECV(){
		return Arrays.copyOfRange(data, 11, 17);
	}
	
	public void setSYSTEM_CODE_RECV(byte[] value){
		setData(value, 11);
	}
	
	public byte[] getOP_DIV(){
		return Arrays.copyOfRange(data, 17, 21);
	}
	
	public void setOP_DIV(byte[] value){
		setData(value, 17);
	}
	
	public byte[] getWORK_DIV(){
		return Arrays.copyOfRange(data, 21, 25);
	}
	
	public void setWORK_DIV(byte[] value){
		setData(value, 21);
	}
	
	public byte[] getSEND_DT(){
		return Arrays.copyOfRange(data, 25, 39);
	}
	
	public void setSEND_DT(byte[] value){
		setData(value, 25);
	}
	
	public byte[] getREPLY_CODE(){
		return Arrays.copyOfRange(data, 39, 43);
	}
	
	public void setREPLY_CODE(byte[] value){
		setData(value, 39);
	}
	
	public byte[] getEnd(){
		return Arrays.copyOfRange(data, 43, 44);
	}
	
	public void setEnd(byte[] value){
		setData(value, 43);
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	private void setData(byte[] value, final int index_start){
		for(int i=0; i<value.length; i++){
			data[i+index_start] = value[i];
		}
	}
}
