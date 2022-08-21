sudo su
wait 3
sudo apt update
wait 15
sudo apt install apt-transport-https ca-certificates curl software-properties-common -y
wait 15
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
wait 5
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
wait 10
sudo apt update
wait 10
apt-cache policy docker-ce
wait 3
sudo apt install docker-ce -y
wait 25