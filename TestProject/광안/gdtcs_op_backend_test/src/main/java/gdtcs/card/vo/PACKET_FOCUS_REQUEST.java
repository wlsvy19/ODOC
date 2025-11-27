package gdtcs.card.vo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * 
 * @파일명		: 포커스 면제PL 요청
 * @프로그램설명	: 카드관리 > 면제 PL 조회 > 면제 여부 요청 
 * @작성자		: 이지형
 * @작성일		: 2024. 9. 27.
 * @version 1.0
 * @개정이력
 *
 * <pre>
 *  수정일            수정자              수정내용
 *  -------    --------    ---------------------------
 *	 2024. 9. 27.   이지형     최초생성
 * </pre>
 *
 */
public class PACKET_FOCUS_REQUEST {
	byte[] data = new byte[18];
	
	public PACKET_FOCUS_REQUEST(  //euc-kr하면 한글 byte가 3에서 2로 줄어듬, 패딩이나 나중에 고려 필요
			String PACKET_AUTH_CHAR,
			String PACKET_SORT,
			String PACKET_CAR_NO,
			String PACKET_END_CHAR

			) throws IOException {	
		System.out.println("송신 : "+PACKET_CAR_NO.getBytes("euc-kr").length);
		while (PACKET_CAR_NO.getBytes("euc-kr").length != 10) {
			PACKET_CAR_NO += " ";
			System.out.println("패딩작업중 : "+PACKET_CAR_NO.getBytes("euc-kr").length);		
		}
		setPACKET_AUTH_CHAR(PACKET_AUTH_CHAR.getBytes("euc-kr"));
		setPACKET_SORT(PACKET_SORT.getBytes("euc-kr"));
		setPACKET_CAR_NO(PACKET_CAR_NO.getBytes("euc-kr"));
		setPACKET_END_CHAR(PACKET_END_CHAR.getBytes("euc-kr"));
		
	}
	//set 작업부터 시작
	
	public byte[] getPACKET_AUTH_CHAR(){
		return Arrays.copyOfRange(data, 0, 5);
	}
	
	public void setPACKET_AUTH_CHAR(byte[] value){
		setData(value, 0);
	}
	
	public byte[] getPACKET_SORT(){
		return Arrays.copyOfRange(data, 5, 6);
	}
	
	public void setPACKET_SORT(byte[] value){
		setData(value, 5);
	}
	
	public byte[] getPACKET_CAR_NO(){
		return Arrays.copyOfRange(data, 6, 16);
	}
	
	public void setPACKET_CAR_NO(byte[] value){
		setData(value, 6);
	}
	
	public byte[] getPACKET_END_CHAR(){
		return Arrays.copyOfRange(data, 16, 18);
	}
	
	public void setPACKET_END_CHAR(byte[] value){
		setData(value, 16);
	}
	
	public byte[] getData() {
		System.out.println("REQUEST : "+data);
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
		System.out.println(data);
	}
	
	private void setData(byte[] value, final int index_start){
		for(int i=0; i<value.length; i++){
			data[i+index_start] = value[i];
		}
	}
}
