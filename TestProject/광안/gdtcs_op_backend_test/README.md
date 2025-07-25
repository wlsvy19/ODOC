# 광안대교 통행료 수납 시스템 - 영업소 운영단말 시스템 Back-End
Back-End 서버는 DB로부터 값을 읽어와서 Front-End에 제공하고 Front-End의 요청에 따라 DB에 값을 삽입·수정·삭제하는 역할을 합니다.

## 주소
- 운영: http://10.66.1.31:9080/
- 개발: http://10.100.10.148:9080/

## 개발환경
개발환경과 운영환경은 동일하게 설정합니다.

- 전자정부프레임워크 4.1.0
- Spring Boot 3.0.3
- OpenJDK 17.0.10
- Eclipse IDE 2022-03 (4.23.0)
- Oracle Database 19c (19.3.0.0.0)
- Ubuntu 22.04

## 주요 디렉토리

### 운영 서버

### 개발 서버
- /home/gdtcs/gdtcs_backend/

## 배포 서버에 따라 수정해야 하는 파일 목록
- application.yml
- log4j2.xml
- shutdown.sh
- startup.sh
    
## 로그 경로

### 운영 서버
- /home/gdtcs/gdtcs_backend/logs

### 개발 서버
- /home/gdtcs/gdtcs_backend/logs

## 자동 로그 삭제
자동 로그 삭제를 위한 쉘 스크립트는 매일 23시 59분 실행되며 운영 환경에 설정되어 있습니다. 이 스크립트들은 자동 실행 스케줄러(Crontab)에 등록되어 있습니다.

### 운영 서버
- /home/gdtcs/delete_log.sh * 작성 중...

### 개발 서버
- /home/gdtcs/delete_log.sh * 작성 중...

## 실행 및 디버깅

### 운영
root 계정으로 로그인합니다.
- WAS 시작: systemctl start springboot_gdtcs
- WAS 중지: systemctl stop springboot_gdtcs
- WAS 로그 확인: tail -f /home/gdtcs/gdtcs_backend/logs/out.log

차로 통신 서버 로그를 보려면 차로통신서버 터미널에 접속한 후 아래의 명령어를 입력합니다.
- tail -f /data/log/message.log

### 개발
root 계정으로 로그인합니다.
- WAS 시작: systemctl start springboot_gdtcs
- WAS 중지: systemctl stop springboot_gdtcs
- WAS 로그 확인: tail -f /home/gdtcs/gdtcs_backend/logs/out.log

차로 통신 서버 로그를 보려면 차로통신서버 터미널에 접속한 후 아래의 명령어를 입력합니다.
- tail -f /Data/gd_log/message.log
