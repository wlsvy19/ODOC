# 프론트엔드 서버 구축
프론트엔드 실행을 위해 Express 웹 서버를 설치한다. 설치를 위해 우분투와 계정을 준비한다.
## 1. Nodejs 설치 - 버전 20.11.1, nvm 으로 node 버전 관리
```sh
apt update
```
```sh
apt install build-essential libssl-dev curl
```
```sh
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.4/install.sh | bash
```
```sh
export NVM_DIR="$([ -z "${XDG_CONFIG_HOME-}" ] && printf %s "${HOME}/.nvm" || printf %s "${XDG_CONFIG_HOME}/nvm")"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
```
```sh
nvm install 20.11.1
```
```sh
nvm use 20.11.1
```
## 2. NPM 설치 - 버전 10.2.4
```sh
npm install -g npm@10.2.4
```
## 3. EXPRESS 설치
```sh
npm install express
```
## 4. http-proxy 설치
```sh
npm install http-proxy
```
## 5. cors 설치
```sh
npm install cors
```
# 프론트엔드 파일 배포
아래 경로의 디렉토리를 생성한다.
```
~/gdtcs_op_front
``` 
## 1. 배포용 파일 복사
'/배포관련파일'에 위치한 파일을 복사하고 파일 내용을 텍스트 에디터로 열어서 해당 시스템에 맞게 경로를 수정한다.
## 2. 프론트엔드 파일 복사
프론트엔드 배포용 파일을 생성한다(생성 방법을 README에 정리해주시면 좋겠습니다). '~/gdtcs_front/gdtcs_op_frontend'에 프론트엔드 배포용 파일을 넣는다.
## 3. 의존성 패키지 설치
'~/gdtcs_front'에서 아래의 명령어를 실행한다.
```
npm install
```
## 4. 동작 테스트
아래의 스크립트를 실행하여 프론트엔드 서버가 잘 실행되는지 확인한다.
```
./startup.sh
```
웹 브라우저로 접속해서 의도한 페이지가 나타나면 프론트 엔드 서버가 잘 실행된 것이므로 다음 작업을 위해 서버를 중지한다. 서버를 중지하는 스크립트를 아래와 같이 실행한다.
```
./shutdown.sh
```
## 5. 서비스 생성
root 권한으로 아래의 명령어를 입력하여 서비스를 생성한다.

```
vi /etc/systemd/system/nodejs_gdtcs.service
```

서비스 파일에는 아래의 내용을 입력한다. 경로와 같은 세부내용은 구동 환경에 맞춰서 적절하게 수정한다.

```
[Unit]
Description='Node.js Server for Front-End'
After=syslog.target network.target

[Service]
ExecStart=/home/gdtcs/gdtcs_frontend/startup.sh
ExecStop=/home/gdtcs/gdtcs_frontend/shutdown.sh
WorkingDirectory=/home/gdtcs/gdtcs_frontend
Restart=always
RestartSec=10
User=jenkins
Group=jenkins
Environment=PATH=/usr/bin:/usr/local/bin
Environment=NODE_ENV=production
Environment=PORT=9081

[Install]
WantedBy=multi-user.target
```

아래의 명령어를 입력하여 서비스를 등록한다.

```
systemctl daemon-reload
```

아래의 명령어를 입력하여 서비스를 실행한다.

```
systemctl start nodejs_gdtcs
```

아래의 명령어를 입력하여 서비스를 중지한다.

```
systemctl stop nodejs_gdtcs
```

아래의 명령어를 입력하여 서비스가 운영체제 부팅 때 자동으로 실행되도록 설정한다.

```
systemctl enable nodejs_gdtcs
```