# install all
# bash npm_install.sh 
# install new lib
# bash npm_install.sh new_name

cd docker

if [ "$1" ]
then
  echo "yarn add $1"
  docker-compose run node yarn add "$1" --save
else
  echo "yarn install"
  docker-compose run node yarn install
fi

