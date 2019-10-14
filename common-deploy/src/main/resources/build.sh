git pull
jar_dir="/data/code/jars"
mvn clean install -DskipTests -Ppdt

if [ ! -d ${jar_dir} ]; then
    mkdir -p ${jar_dir}
fi

jar_name="online-website-1.0-SNAPSHOT.jar"
result_jar="../jars/${jar_name}"
now_time=$(date +"%Y%m%d_%H%M%S")
if [ -f ${result_jar} ]; then
    rename "${jar_name}" "online-website-${now_time}.jar" "../jars/${jar_name}"
fi

cp "./target/${jar_name}" ${result_jar}