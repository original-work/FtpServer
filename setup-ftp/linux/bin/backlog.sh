#!/bin/bash
dir=$1
echo $dir
cd $dir
d=`date -d '1 day ago' +%F`
t=`date -d '1 day ago' +%Y%m%d`
echo $d
echo $t
find ./ -name FtpServer.log.$d\* -o -name data\*$d | xargs tar cvzf log.$t.tgz
find ./ -name \*$d\* -exec rm {} \;
#find ./ -name FtpServer.log.$d\* -exec echo rm {} \;
#find ./ -name data\*$d -exec echo rm {} \;
