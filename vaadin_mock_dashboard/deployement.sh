export LD_LIBRARY_PATH=;export PATH=/app/list/Data/devops/jdk-17.0.2/bin:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin
#nohup java -Dspring.profiles.active=pt -Djavax.net.ssl.trustStore=ca_keystore.crt -Djavax.net.ssl.trustStorePassword=password  -Denv.database=horsprod  -Dtest-email-only=true -Dtype-metrics=test -Dliste.dba=pascal.thomas@lcl.fr -Dlibelle.message.dba=TEST-ENVOI -Dkeep.referent=true -jar web-visu-2025-4.0.jar --spring.config.location=classpath:/application-pt.properties &
ACTION_MODE=$1 # permet de savoir si c'est nous voullons INSTALLER ou ARRETER l'application
NOM_JAR_FILE=$2 # Nom du fichier a installer

stop_app()
{
  local FONCTIONNALITE=$1
  echo " --------------------- ${FONCTIONNALITE} ... $APP_NAME --------------------------"
      PROCESS_IDS=$(ps ax | grep "$NOM_JAR_FILE" | grep -v grep | awk '{print $1}')
      echo "PROCESS_IDS=$(ps ax | grep "$NOM_JAR_FILE" | grep -v grep | awk '{print $1}')"

      if [[ -n ${PROCESS_IDS} ]];then
          echo "Java application is running is the Process $PROCESS_IDS"
          echo " --------------------- STOPPING ... --------------------------"
          sleep 2
          echo "$PROCESS_IDS" | xargs kill
          echo " --------------------- APPLICATION $APP_NAME STOPPED ... --------------------------"
      else
          echo " --------------------- APPLICATION -> $APP_NAME ALREADY STOPPED--------------------------"
      fi
}

if [[ $ACTION_MODE = "START" ]]
then
    stop_app "RESTARTING"
    echo " --------------------- APPLICATION  $NOM_JAR_FILE --------------------------"
    echo " --------------------- STARTING ... --------------------------"
    APP_NAME="$( cut -d '-' -f 1 <<< "$NOM_JAR_FILE" )"
    APP_LOGS_DIR="${APP_NAME}_LOGS"
    mkdir -p "$APP_LOGS_DIR"
    nohup java -Djavax.net.ssl.trustStore=ca_keystore.crt -Djavax.net.ssl.trustStorePassword=password -jar $NOM_JAR_FILE 1>>"${APP_LOGS_DIR}/${APP_NAME}".log 2>>"${APP_LOGS_DIR}/${APP_NAME}".err &
    sleep 2
    echo " --------------------- STARTING ... --------------------------"
    sleep 2
    echo " --------------------- APPLICATION $APP_NAME STARTED. --------------------------"
else
    stop_app "STOPPING"
fi
