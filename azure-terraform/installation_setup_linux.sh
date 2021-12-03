#!/bin/bash

echo "Install Azure CLI...."
#Get packages needed for the install process:
sudo apt-get update
sudo apt-get install ca-certificates curl apt-transport-https lsb-release gnupg
	
#Download and install the Microsoft signing key:
curl -sL https://packages.microsoft.com/keys/microsoft.asc |
gpg --dearmor |
sudo tee /etc/apt/trusted.gpg.d/microsoft.gpg > /dev/null
	
#Add the Azure CLI software repository
AZ_REPO=$(lsb_release -cs)
echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ $AZ_REPO main" |
sudo tee /etc/apt/sources.list.d/azure-cli.list
	
#Update repository information and install the azure-cli package
sudo apt-get update
sudo apt-get install azure-cli
    
echo "az version"
az --version

echo "Install the Kubectl...."
	
az aks install-cli
kubectl version --client
	
echo "Install helm package..."
curl https://baltocdn.com/helm/signing.asc | sudo apt-key add -
sudo apt-get install apt-transport-https --yes
echo "deb https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update
sudo apt-get install helm
sudo helm version
	
#terraform Installation
echo "Install Terraform..."
sudo apt-get update && sudo apt-get install -y gnupg software-properties-common curl
curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo apt-key add -
sudo apt-add-repository "deb [arch=amd64] https://apt.releases.hashicorp.com $(lsb_release -cs) main"
sudo apt-get update && sudo apt-get install terraform
	
#terraform -help
terraform version
	
#terraform destroy
#terraform destroy --auto-approve	

	