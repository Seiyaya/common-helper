proc=$(ps -ef|grep "online-website"|grep -v "grep"|awk '{print $2}')
echo "need kill ${proc}"
kill -9 ${proc}
sleep 5
echo "kill success"

java -jar ../jars/online-website-1.0-SNAPSHOT.jar &
echo "重启成功====="