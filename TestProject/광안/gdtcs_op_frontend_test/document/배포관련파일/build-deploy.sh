#!/bin/bash

# 경로 설정
PROJECT_DIR=/home/gdtcs/gdtcs_frontend
DIST_DIR=$PROJECT_DIR/dist

# 프로젝트 디렉토리로 이동
cd $PROJECT_DIR

# NPM 빌드 실행
npm install
npm run build

# 기존 dist 폴더 제거 후 새로 빌드된 dist 폴더 복사
rm -rf /var/www/html/gdtcs
mv $DIST_DIR /var/www/html/gdtcs

echo "Build and deployment completed successfully."

# Node.js 서버 재시작
sudo systemctl restart nodejs_gdtcs.service
