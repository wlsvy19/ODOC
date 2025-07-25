# 배포 방법
설치를 위해 우분투와 계정을 준비하고 아래 경로의 디렉토리를 생성한다.
```
~/gdtcs_op_frontend
``` 
## 1. 배포용 파일 복사
생성한 디렉토리에 배포용 스프링 파일(.jar)과 '/배포관련파일'에 위치한 파일을 복사하고 파일 내용을 텍스트 에디터로 열어서 해당 시스템에 맞게 경로를 수정한다.
## 2. 자바 설치
프로젝트 기준 자바 버전으로 자바를 설치한다.
## 3. 동작 테스트
아래의 스크립트를 실행하여 프론트엔드 서버가 잘 실행되는지 확인한다.
```
./startup.sh
```
웹 브라우저로 접속해서 의도한 페이지가 나타나면 백엔드 서버가 잘 실행된 것이므로 다음 작업을 위해 서버를 중지한다. 서버를 중지하는 스크립트를 아래와 같이 실행한다.
```
./shutdown.sh
```
## 4. 서비스 생성
root 권한으로 아래의 명령어를 입력하여 서비스를 생성한다.

```
vi /etc/systemd/system/springboot_gdtcs_op.service
```

서비스 파일에는 아래의 내용을 입력한다. 경로와 같은 세부내용은 구동 환경에 맞춰서 적절하게 수정한다.

```
[Unit]
Description=GDTCS Operation Server
After=syslog.target network.target

[Service]
Type=forking

#WorkingDirectory=/home/gdtcs/gdtcs_op_backend
Environment=JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
#Environment=PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:$PATH
Environment=BOOT_HOME=/home/gdtcs/gdtcs_op_backend
Environment='CATALINA_OPTS=-Xms256M -Xmx2048M -server -XX:+UseParallelGC'
Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom'

ExecStart=/home/gdtcs/gdtcs_op_backend/startup.sh
ExecStop=/home/gdtcs/gdtcs_op_backend/shutdown.sh
#ExecStop=/bin/kill -15 $MAINPID
WorkingDirectory=/home/gdtcs/gdtcs_op_backend

User=gdtcs
Group=gdtcs
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
```

아래의 명령어를 입력하여 서비스를 등록한다.

```
systemctl daemon-reload
```

아래의 명령어를 입력하여 서비스를 실행한다.

```
systemctl start springboot_gdtcs_op
```

아래의 명령어를 입력하여 서비스를 중지한다.

```
systemctl stop springboot_gdtcs_op
```

아래의 명령어를 입력하여 서비스가 운영체제 부팅 때 자동으로 실행되도록 설정한다.

```
systemctl enable springboot_gdtcs_op
```