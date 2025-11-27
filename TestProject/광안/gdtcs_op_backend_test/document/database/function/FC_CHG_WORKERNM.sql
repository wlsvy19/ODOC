create or replace FUNCTION         "FC_CHG_WORKERNM" (p_IC_CODE IN VARCHAR2, p_WORKER_NO IN VARCHAR2) 
RETURN VARCHAR2 IS 
    v_rtn VARCHAR2(50); 
BEGIN 
    -- 운영자명 가져오기 
    SELECT WORKER_NM 
      INTO   v_rtn 
      FROM   BASE_WORKERINFO 
      WHERE  IC_CODE = p_IC_CODE AND WORKER_NO = p_WORKER_NO; 
    RETURN v_rtn; 
 END;

