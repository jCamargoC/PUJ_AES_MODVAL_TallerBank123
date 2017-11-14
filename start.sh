cd TallerDocker
mvn clean install package -Dmaven.test.skip=true
echo ">>>>>>>>>>>>>>>>>>>>>> TERMINA TallerDocker >>>>>>>>>>>>>>>>>>>>>>"
cd ../routerservice
mvn clean install package -Dmaven.test.skip=true
echo ">>>>>>>>>>>>>>>>>>>>>> TERMINA routerservice >>>>>>>>>>>>>>>>>>>>>>" 
cd ../registryservice
mvn clean install package -Dmaven.test.skip=true
echo ">>>>>>>>>>>>>>>>>>>>>> TERMINA registryservice >>>>>>>>>>>>>>>>>>>>>>"
cd ..
docker-compose up --build
