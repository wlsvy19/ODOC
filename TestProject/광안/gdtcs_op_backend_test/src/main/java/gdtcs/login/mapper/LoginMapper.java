package gdtcs.login.mapper;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("loginMapper")
public interface LoginMapper {
    Map<String, Object> selectLoginInfo(Map<String, Object> param);

    void insertLoginLog(Map<String, Object> param);

    void updateLoginLog(Map<String, Object> param);

    void updateOldLoginLogs();
}
