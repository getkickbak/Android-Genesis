#!/bin/bash

#appPath="/Users/eric/Documents/Aptana Studio 3 Workspace/GetKickBak/public/javascripts/mobile";
appPath="/Users/eric/Documents/GetKickBak/V2.1.0/V2.1.0/public/javascripts/mobile";
PROJECT_DIR="$1"

libPath="lib/touch-2.1.1";

mkdir -p $PROJECT_DIR/www/app/store
mkdir -p $PROJECT_DIR/www/app/profile
mkdir -p $PROJECT_DIR/www/resources/themes/images/v1/android

rsync -pvtrlL --delete --cvs-exclude "$appPath"/app/store/*Client*.json $PROJECT_DIR/www/app/store/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/app/profile/Android.js $PROJECT_DIR/www/app/profile/

rsync -pvtrlL --delete --cvs-exclude "$appPath"/*.wav $PROJECT_DIR/www/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/*.htm $PROJECT_DIR/www/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/client-all.js $PROJECT_DIR/www/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/core.js $PROJECT_DIR/www/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/index_android.html $PROJECT_DIR/www/index.html
rsync -pvtrlL --delete --cvs-exclude "$appPath"/lib/*android.js $PROJECT_DIR/www/lib/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/lib/*min.js $PROJECT_DIR/www/lib/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/lib/core/*nfc*.js $PROJECT_DIR/www/lib/core/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/lib/core/*.android.js $PROJECT_DIR/www/lib/core/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/lib/core/Facebook*.js $PROJECT_DIR/www/lib/core/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/$libPath/sencha-touch-all.js $PROJECT_DIR/www/lib/
#rsync -pvtrlL --delete --cvs-exclude "$appPath"/$libPath/src $PROJECT_DIR/www/$libPath

rsync -pvtrlL --delete --cvs-exclude "$appPath"/resources/css/android*.css $PROJECT_DIR/www/resources/css/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/resources/audio/*.mp3 $PROJECT_DIR/www/resources/audio/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/resources/audio/*.wav $PROJECT_DIR/www/resources/audio/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/resources/themes/images/v1/*facebook* $PROJECT_DIR/www/resources/themes/images/v1/
rsync -pvtrlL --delete --cvs-exclude "$appPath"/resources/themes/images/v1/android/* $PROJECT_DIR/www/resources/themes/images/v1/android
