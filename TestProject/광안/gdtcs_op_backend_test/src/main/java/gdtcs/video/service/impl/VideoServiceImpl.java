package gdtcs.video.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionException;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gdtcs.common.service.ServerInfoService;
import gdtcs.video.mapper.VideoMapper;
import gdtcs.video.service.VideoService;
import gdtcs.video.util.DatabaseSourceVideo;

@Service("videoService")
public class VideoServiceImpl extends EgovAbstractServiceImpl implements VideoService{

    @Autowired
    private VideoMapper videoMapper;
	
	@Autowired
	DatabaseSourceVideo databaseSourceVideo;
	
	/**
	 *  
	 * @Method Name	: selectEtcsImageList
	 * @Method 설명	: ETCS 영상 정보를 가져온다.
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 07. 26.
	 *
	 * @param searchMap
	 * @return List<Map>
	 * @throws SqlSessionException
	 * @throws IOException 
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> selectEtcsImageList(String IC_CODE, String tolling_time, String work_number, String processing_number) throws SqlSessionException, IOException
	{
		databaseSourceVideo.getConnection(IC_CODE);
		return databaseSourceVideo.selectEtcsImageList(IC_CODE, tolling_time, work_number, processing_number);
	}
}
