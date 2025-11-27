package gdtcs.common.service.impl;

import gdtcs.common.mapper.CommonMapper;
import gdtcs.common.service.CommonService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService {

    @Resource(name="commonMapper")
    private CommonMapper commonMapper;

    @Override
    public Map<String, Object> getImagePath(Map<String, Object> param) throws Exception {
        return commonMapper.getImagePath(param);
    }
    
	/**
	 *  
	 * @Method Name	: getFare
	 * @Method 설명	: 통행요금을 읽어온다.
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 07. 18.
	 *
	 * @param Map<String, Object> param
	 * @return ResponseEntity<List<Map<String, Object>>>
	 * @throws Exception
	 *
	 */
    @Override
    public List<Map<String, Object>> getFare(Map param) throws Exception{
        return commonMapper.selectFare(param);
    }

    /**
	 *  
	 * @Method Name	: getRevNo
	 * @Method 설명	: 개정번호를 읽어온다.
	 * @작성자 		: 노영재
	 * @작성일 		: 2024. 07. 25.
	 *
	 * @param Map<String, Object> param
	 * @return ResponseEntity<List<Map<String, Object>>>
	 * @throws Exception
	 *
	 */
	@Override
	public List<Map<String, Object>> getRevNo(Map param) throws Exception {
		return commonMapper.selectRevNo(param);
	}

	/**
	 *  
	 * @Method Name	: getLogoImage
	 * @Method 설명	: 로고 이미지를 가져온다.
	 * @작성자 		: 박형철
	 * @작성일 		: 2024. 09. 20.
	 *
	 * @param 
	 * @return File
	 * @throws Exception
	 *
	 */
	@Override
	public File getLogoImage(File file) throws Exception {
		Map<String, Object> imageMap = commonMapper.selectLogoImage();
		byte[] blob = (byte[]) imageMap.get("IMG_DATA");
		FileUtils.writeByteArrayToFile(file, blob);
		return file;
	}
}
