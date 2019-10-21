gitResult=$(git pull)
if [ "${gitResult}" != "Already up-to-date." ]; then
 echo "has new commit"
 mvn clean install -DskipTests
else 
 echo "no new commit"
fi
