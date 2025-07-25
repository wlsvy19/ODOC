package gdtcs.card.vo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 
 * 
 * @파일명		: 포커스 면제PL 응답
 * @프로그램설명	: 카드관리 > 면제 PL 조회 > 면제 여부 응답 
 * @작성자		: 이지형
 * @작성일		: 2024. 9. 27.
 * @version 1.0
 * @개정이력
 *
 * <pre>
 *  수정일            수정자              수정내용
 *  -------    --------    ---------------------------
 *	 2022. 09. 27.   이지형     최초생성
 * </pre>
 *
 */
public class PACKET_FOCUS_RESPONSE {
	byte[] data = new byte[21];
	 

	public PACKET_FOCUS_RESPONSE(){
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
