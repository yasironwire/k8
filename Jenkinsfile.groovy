pipeline {
    agent any 
    
    stages{
        stage("Clone Code"){
            steps {
                echo "Cloning the code"
                git url:"https://github.com/git-yasir/jenkins.git", branch: "main"
                echo "**************************************************************************************************************"
            }
        }
        stage("Build"){
            steps {
                echo "Building the image"
                sh "docker build -t web-app ."
                echo "**************************************************************************************************************"
            }
        }
        stage("Push to Docker Hub"){
            steps {
                echo "Pushing the image to docker hub"
                withCredentials([usernamePassword(credentialsId:"dockerhub",passwordVariable:"dockerHubPass",usernameVariable:"dockerHubUser")]){
                sh "docker tag web-app ${env.dockerHubUser}/web-app:latest"
                sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPass}"
                sh "docker push ${env.dockerHubUser}/web-app:latest"
                echo "**************************************************************************************************************"
                }
            }
        }
        stage("Deploy"){
            steps {
                echo "Deploying the container"
                sh "kubectl create -f deployment.yaml"
                echo "**************************************************************************************************************"
                
            }
        }
    }
}