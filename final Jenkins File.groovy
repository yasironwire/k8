pipeline {
    agent any 
    
    environment {     
    DOCKERHUB_CREDENTIALS= credentials('dockerhubcredentials')     
    }   
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
                // echo "giving Permission"
                // sh "sudo usermod -aG docker jenkins"
                // echo "Permission assigned"
                echo "Building the image"
                sh "docker build -t web-app ."
                echo "**************************************************************************************************************"
            }
        }

        stage('Login to Docker Hub') {         
            steps{                            
            sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'                 
            echo 'Login Completed'                
            }           
            }               


        stage("Push to Docker Hub"){
            steps {
                echo "Pushing the image to docker hub"
                // withCredentials([string(credentialsId: 'accesstokendocker', variable: 'dockerHubToken')]) {
                sh "docker tag web-app yasirdocker/web-app:latest"
                // sh "docker login -u yasir.butt@hotmail.com -p ${dockerHubToken}"
                sh "docker push yasirdocker/web-app:latest"
                
                
                // echo "Pushing the image to docker hub"
                // withCredentials([string(credentialsId: 'accesstokendocker', variable: 'dockerHubToken')]) {
                // // withCredentials([usernamePassword(credentialsId:"dockerhub",passwordVariable:"dockerHubPass",usernameVariable:"dockerHubUser")]){
                // sh "docker tag web-app yasirdocker/web-app:latest"
                // sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPass}"
                // sh "docker push ${env.dockerHubUser}/web-app:latest"
                echo "**************************************************************************************************************"
                // }
            }
        }
        stage("Deploy"){
            steps {
                echo "Deploying the container"
                sh "sudo -u ubuntu kubectl get pods"
                sh "sudo -u ubuntu kubectl create -f deployment.yaml"
                // sh "kubectl create -f deployment.yaml"
                echo "**************************************************************************************************************"
                
            }
        }
    }
}